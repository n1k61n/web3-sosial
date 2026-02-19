// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/**
 * @title W3Social Token
 * @dev ERC-20 token for rewarding users on the platform
 */
contract W3SocialToken {
    string public name = "W3Social Token";
    string public symbol = "W3S";
    uint8 public decimals = 18;
    uint256 public totalSupply;
    
    mapping(address => uint256) public balanceOf;
    mapping(address => mapping(address => uint256)) public allowance;
    
    // Rewards configuration
    uint256 public constant REWARD_PER_POST = 10 * 10**18; // 10 W3S
    uint256 public constant REWARD_PER_LIKE = 1 * 10**18; // 1 W3S
    uint256 public constant REWARD_PER_COMMENT = 2 * 10**18; // 2 W3S
    uint256 public constant REWARD_PER_FOLLOW = 5 * 10**18; // 5 W3S
    
    // Admin address
    address public admin;
    
    // Minting limit per user per day
    mapping(address => uint256) public dailyMinted;
    mapping(address => uint256) public lastMintDay;
    uint256 public constant DAILY_LIMIT = 100 * 10**18; // 100 W3S per day
    
    event Transfer(address indexed from, address indexed to, uint256 value);
    event Approval(address indexed owner, address indexed spender, uint256 value);
    event Reward(address indexed user, string indexed action, uint256 amount);
    event Mint(address indexed to, uint256 amount);
    
    modifier onlyAdmin() {
        require(msg.sender == admin, "Only admin can call this function");
        _;
    }
    
    constructor(uint256 _initialSupply) {
        totalSupply = _initialSupply * 10**uint256(decimals);
        balanceOf[msg.sender] = totalSupply;
        admin = msg.sender;
        emit Transfer(address(0), msg.sender, totalSupply);
    }
    
    function transfer(address _to, uint256 _value) public returns (bool success) {
        require(balanceOf[msg.sender] >= _value, "Insufficient balance");
        require(_to != address(0), "Invalid address");
        
        balanceOf[msg.sender] -= _value;
        balanceOf[_to] += _value;
        
        emit Transfer(msg.sender, _to, _value);
        return true;
    }
    
    function approve(address _spender, uint256 _value) public returns (bool success) {
        allowance[msg.sender][_spender] = _value;
        emit Approval(msg.sender, _spender, _value);
        return true;
    }
    
    function transferFrom(address _from, address _to, uint256 _value) public returns (bool success) {
        require(_value <= balanceOf[_from], "Insufficient balance");
        require(_value <= allowance[_from][msg.sender], "Allowance exceeded");
        require(_to != address(0), "Invalid address");
        
        balanceOf[_from] -= _value;
        balanceOf[_to] += _value;
        allowance[_from][msg.sender] -= _value;
        
        emit Transfer(_from, _to, _value);
        return true;
    }
    
    /**
     * @dev Reward users for platform actions
     */
    function rewardUser(address _user, string memory _action) public onlyAdmin returns (bool) {
        uint256 reward;
        
        if (keccak256(bytes(_action)) == keccak256(bytes("post"))) {
            reward = REWARD_PER_POST;
        } else if (keccak256(bytes(_action)) == keccak256(bytes("like"))) {
            reward = REWARD_PER_LIKE;
        } else if (keccak256(bytes(_action)) == keccak256(bytes("comment"))) {
            reward = REWARD_PER_COMMENT;
        } else if (keccak256(bytes(_action)) == keccak256(bytes("follow"))) {
            reward = REWARD_PER_FOLLOW;
        } else {
            revert("Invalid action type");
        }
        
        _mintWithLimit(_user, reward);
        emit Reward(_user, _action, reward);
        return true;
    }
    
    /**
     * @dev Mint tokens with daily limit
     */
    function _mintWithLimit(address _to, uint256 _amount) internal {
        uint256 currentDay = block.timestamp / 1 days;
        
        if (lastMintDay[_to] != currentDay) {
            dailyMinted[_to] = 0;
            lastMintDay[_to] = currentDay;
        }
        
        require(dailyMinted[_to] + _amount <= DAILY_LIMIT, "Daily limit exceeded");
        
        totalSupply += _amount;
        balanceOf[_to] += _amount;
        dailyMinted[_to] += _amount;
        
        emit Transfer(address(0), _to, _amount);
        emit Mint(_to, _amount);
    }
    
    /**
     * @dev Admin can withdraw remaining tokens
     */
    function withdraw(uint256 _amount) public onlyAdmin {
        require(balanceOf[admin] >= _amount, "Insufficient admin balance");
        balanceOf[admin] -= _amount;
        emit Transfer(admin, address(0), _amount);
    }
}
