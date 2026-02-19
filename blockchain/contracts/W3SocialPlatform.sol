// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/**
 * @title W3Social Platform
 * @dev Main smart contract for decentralized social media platform
 * Stores posts, profiles, and interactions on-chain
 */
contract W3SocialPlatform {
    address public admin;
    address public tokenAddress;
    
    // Post structure
    struct Post {
        uint256 id;
        address author;
        string contentHash; // IPFS hash of post content
        uint256 timestamp;
        bool exists;
        uint256 likeCount;
        uint256 commentCount;
    }
    
    // Profile structure
    struct Profile {
        address wallet;
        string username;
        string bioHash; // IPFS hash of profile data
        uint256 followerCount;
        uint256 followingCount;
        uint256 postCount;
        bool exists;
        uint256 createdAt;
    }
    
    // Storage
    mapping(uint256 => Post) public posts;
    mapping(address => Profile) public profiles;
    mapping(uint256 => mapping(address => bool)) public postLikes;
    mapping(address => mapping(address => bool)) public followers;
    mapping(address => uint256[]) public userPosts;
    
    // Counters
    uint256 public postCount;
    
    // Events
    event ProfileCreated(address indexed user, string username, uint256 timestamp);
    event ProfileUpdated(address indexed user, string bioHash, uint256 timestamp);
    event PostCreated(uint256 indexed postId, address indexed author, string contentHash, uint256 timestamp);
    event PostLiked(uint256 indexed postId, address indexed liker, uint256 timestamp);
    event PostUnliked(uint256 indexed postId, address indexed liker, uint256 timestamp);
    event CommentAdded(uint256 indexed postId, address indexed commenter, string contentHash, uint256 timestamp);
    event Follow(address indexed follower, address indexed following, uint256 timestamp);
    event Unfollow(address indexed follower, address indexed following, uint256 timestamp);
    
    modifier onlyAdmin() {
        require(msg.sender == admin, "Only admin can call this function");
        _;
    }
    
    modifier profileExists(address _user) {
        require(profiles[_user].exists, "Profile does not exist");
        _;
    }
    
    modifier postExists(uint256 _postId) {
        require(posts[_postId].exists, "Post does not exist");
        _;
    }
    
    constructor(address _tokenAddress) {
        admin = msg.sender;
        tokenAddress = _tokenAddress;
    }
    
    /**
     * @dev Create user profile
     */
    function createProfile(string memory _username, string memory _bioHash) public {
        require(!profiles[msg.sender].exists, "Profile already exists");
        require(bytes(_username).length > 0, "Username required");
        
        profiles[msg.sender] = Profile({
            wallet: msg.sender,
            username: _username,
            bioHash: _bioHash,
            followerCount: 0,
            followingCount: 0,
            postCount: 0,
            exists: true,
            createdAt: block.timestamp
        });
        
        emit ProfileCreated(msg.sender, _username, block.timestamp);
    }
    
    /**
     * @dev Update profile bio
     */
    function updateProfile(string memory _bioHash) public profileExists(msg.sender) {
        profiles[msg.sender].bioHash = _bioHash;
        emit ProfileUpdated(msg.sender, _bioHash, block.timestamp);
    }
    
    /**
     * @dev Create new post
     */
    function createPost(string memory _contentHash) public profileExists(msg.sender) returns (uint256) {
        postCount++;
        
        posts[postCount] = Post({
            id: postCount,
            author: msg.sender,
            contentHash: _contentHash,
            timestamp: block.timestamp,
            exists: true,
            likeCount: 0,
            commentCount: 0
        });
        
        profiles[msg.sender].postCount++;
        userPosts[msg.sender].push(postCount);
        
        emit PostCreated(postCount, msg.sender, _contentHash, block.timestamp);
        return postCount;
    }
    
    /**
     * @dev Like a post
     */
    function likePost(uint256 _postId) public profileExists(msg.sender) postExists(_postId) {
        require(!postLikes[_postId][msg.sender], "Already liked");
        require(posts[_postId].author != msg.sender, "Cannot like own post");
        
        postLikes[_postId][msg.sender] = true;
        posts[_postId].likeCount++;
        
        emit PostLiked(_postId, msg.sender, block.timestamp);
    }
    
    /**
     * @dev Unlike a post
     */
    function unlikePost(uint256 _postId) public profileExists(msg.sender) postExists(_postId) {
        require(postLikes[_postId][msg.sender], "Not liked yet");
        
        postLikes[_postId][msg.sender] = false;
        posts[_postId].likeCount--;
        
        emit PostUnliked(_postId, msg.sender, block.timestamp);
    }
    
    /**
     * @dev Add comment to post (creates a post reference)
     */
    function addComment(uint256 _postId, string memory _contentHash) 
        public 
        profileExists(msg.sender) 
        postExists(_postId) 
        returns (uint256) 
    {
        posts[_postId].commentCount++;
        
        emit CommentAdded(_postId, msg.sender, _contentHash, block.timestamp);
        return posts[_postId].commentCount;
    }
    
    /**
     * @dev Follow a user
     */
    function follow(address _user) public profileExists(msg.sender) {
        require(_user != msg.sender, "Cannot follow yourself");
        require(profiles[_user].exists, "User does not exist");
        require(!followers[msg.sender][_user], "Already following");
        
        followers[msg.sender][_user] = true;
        profiles[msg.sender].followingCount++;
        profiles[_user].followerCount++;
        
        emit Follow(msg.sender, _user, block.timestamp);
    }
    
    /**
     * @dev Unfollow a user
     */
    function unfollow(address _user) public profileExists(msg.sender) {
        require(followers[msg.sender][_user], "Not following");
        
        followers[msg.sender][_user] = false;
        profiles[msg.sender].followingCount--;
        profiles[_user].followerCount--;
        
        emit Unfollow(msg.sender, _user, block.timestamp);
    }
    
    /**
     * @dev Get user's posts
     */
    function getUserPosts(address _user) public view returns (uint256[] memory) {
        return userPosts[_user];
    }
    
    /**
     * @dev Check if user is following another user
     */
    function isFollowing(address _follower, address _following) public view returns (bool) {
        return followers[_follower][_following];
    }
    
    /**
     * @dev Check if address has liked a post
     */
    function hasLiked(uint256 _postId, address _user) public view returns (bool) {
        return postLikes[_postId][_user];
    }
    
    /**
     * @dev Update token address (admin only)
     */
    function setTokenAddress(address _tokenAddress) public onlyAdmin {
        tokenAddress = _tokenAddress;
    }
    
    /**
     * @dev Get profile data
     */
    function getProfile(address _user) public view returns (
        string memory username,
        string memory bioHash,
        uint256 followerCount,
        uint256 followingCount,
        uint256 postCount,
        uint256 createdAt
    ) {
        Profile memory profile = profiles[_user];
        require(profile.exists, "Profile does not exist");
        
        return (
            profile.username,
            profile.bioHash,
            profile.followerCount,
            profile.followingCount,
            profile.postCount,
            profile.createdAt
        );
    }
    
    /**
     * @dev Get post data
     */
    function getPost(uint256 _postId) public view returns (
        address author,
        string memory contentHash,
        uint256 timestamp,
        uint256 likeCount,
        uint256 commentCount
    ) {
        Post memory post = posts[_postId];
        require(post.exists, "Post does not exist");
        
        return (
            post.author,
            post.contentHash,
            post.timestamp,
            post.likeCount,
            post.commentCount
        );
    }
}
