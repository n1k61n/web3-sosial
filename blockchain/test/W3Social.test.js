const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("W3SocialToken", function () {
  let token;
  let owner;
  let addr1;
  let addr2;

  beforeEach(async function () {
    [owner, addr1, addr2] = await ethers.getSigners();

    const W3SocialToken = await ethers.getContractFactory("W3SocialToken");
    token = await W3SocialToken.deploy(1000000);
    await token.waitForDeployment();
  });

  it("Should initialize with correct supply", async function () {
    const totalSupply = await token.totalSupply();
    expect(totalSupply).to.equal(ethers.parseEther("1000000"));
    
    const ownerBalance = await token.balanceOf(owner.address);
    expect(ownerBalance).to.equal(totalSupply);
  });

  it("Should transfer tokens between accounts", async function () {
    await token.transfer(addr1.address, ethers.parseEther("1000"));
    const addr1Balance = await token.balanceOf(addr1.address);
    expect(addr1Balance).to.equal(ethers.parseEther("1000"));
  });

  it("Should reward users for actions", async function () {
    const initialBalance = await token.balanceOf(addr1.address);
    
    await token.rewardUser(addr1.address, "post");
    const afterPostBalance = await token.balanceOf(addr1.address);
    expect(afterPostBalance).to.equal(initialBalance + ethers.parseEther("10"));
    
    await token.rewardUser(addr1.address, "like");
    const afterLikeBalance = await token.balanceOf(addr1.address);
    expect(afterLikeBalance).to.equal(afterPostBalance + ethers.parseEther("1"));
  });

  it("Should enforce daily mint limit", async function () {
    // Try to mint more than daily limit
    const dailyLimit = await token.DAILY_LIMIT();
    const largeAmount = dailyLimit + ethers.parseEther("1");
    
    await expect(token.rewardUser(addr1.address, "post"))
      .to.not.be.reverted; // First reward should work
    
    // Keep rewarding until limit is reached
    for (let i = 0; i < 10; i++) {
      try {
        await token.rewardUser(addr1.address, "post");
      } catch (error) {
        break;
      }
    }
    
    // Should fail when limit exceeded
    await expect(token.rewardUser(addr1.address, "post"))
      .to.be.revertedWith("Daily limit exceeded");
  });
});

describe("W3SocialPlatform", function () {
  let platform;
  let token;
  let owner;
  let user1;
  let user2;

  beforeEach(async function () {
    [owner, user1, user2] = await ethers.getSigners();

    const W3SocialToken = await ethers.getContractFactory("W3SocialToken");
    token = await W3SocialToken.deploy(1000000);
    const tokenAddress = await token.getAddress();

    const W3SocialPlatform = await ethers.getContractFactory("W3SocialPlatform");
    platform = await W3SocialPlatform.deploy(tokenAddress);
  });

  it("Should create profile", async function () {
    const tx = await platform.connect(user1).createProfile("user1", "ipfs://hash1");
    await tx.wait();

    const profile = await platform.getProfile(user1.address);
    expect(profile.username).to.equal("user1");
    expect(profile.exists).to.be.true;
  });

  it("Should create post", async function () {
    await platform.connect(user1).createProfile("user1", "ipfs://hash1");
    
    const tx = await platform.connect(user1).createPost("ipfs://post1");
    const receipt = await tx.wait();
    
    const postCount = await platform.postCount();
    expect(postCount).to.equal(1);
  });

  it("Should like post", async function () {
    await platform.connect(user1).createProfile("user1", "ipfs://hash1");
    await platform.connect(user2).createProfile("user2", "ipfs://hash2");
    
    await platform.connect(user1).createPost("ipfs://post1");
    await platform.connect(user2).likePost(1);
    
    const post = await platform.getPost(1);
    expect(post.likeCount).to.equal(1);
  });

  it("Should follow/unfollow users", async function () {
    await platform.connect(user1).createProfile("user1", "ipfs://hash1");
    await platform.connect(user2).createProfile("user2", "ipfs://hash2");
    
    await platform.connect(user1).follow(user2.address);
    
    const user1Profile = await platform.getProfile(user1.address);
    const user2Profile = await platform.getProfile(user2.address);
    
    expect(user1Profile.followingCount).to.equal(1);
    expect(user2Profile.followerCount).to.equal(1);
    
    await platform.connect(user1).unfollow(user2.address);
    
    const user1ProfileAfter = await platform.getProfile(user1.address);
    expect(user1ProfileAfter.followingCount).to.equal(0);
  });

  it("Should prevent duplicate likes", async function () {
    await platform.connect(user1).createProfile("user1", "ipfs://hash1");
    await platform.connect(user2).createProfile("user2", "ipfs://hash2");
    
    await platform.connect(user1).createPost("ipfs://post1");
    await platform.connect(user2).likePost(1);
    
    await expect(platform.connect(user2).likePost(1))
      .to.be.revertedWith("Already liked");
  });
});
