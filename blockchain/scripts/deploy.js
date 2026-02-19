const hre = require("hardhat");

async function main() {
  console.log("🚀 Deploying W3Social smart contracts...");

  // Get deployer account
  const [deployer] = await hre.ethers.getSigners();
  console.log("Deploying contracts with account:", deployer.address);

  // Check balance
  const balance = await hre.ethers.provider.getBalance(deployer.address);
  console.log("Account balance:", hre.ethers.formatEther(balance), "ETH");

  // Deploy W3Social Token
  console.log("\n📦 Deploying W3SocialToken...");
  const W3SocialToken = await hre.ethers.getContractFactory("W3SocialToken");
  const initialSupply = 1000000; // 1 million tokens
  const token = await W3SocialToken.deploy(initialSupply);
  await token.waitForDeployment();
  const tokenAddress = await token.getAddress();
  console.log("✅ W3SocialToken deployed to:", tokenAddress);

  // Deploy W3Social Platform
  console.log("\n📦 Deploying W3SocialPlatform...");
  const W3SocialPlatform = await hre.ethers.getContractFactory("W3SocialPlatform");
  const platform = await W3SocialPlatform.deploy(tokenAddress);
  await platform.waitForDeployment();
  const platformAddress = await platform.getAddress();
  console.log("✅ W3SocialPlatform deployed to:", platformAddress);

  // Set token address in platform (platform already has tokenAddress from constructor)
  console.log("\n🔗 Token and platform connected via constructor");

  // Verify contracts (only for testnet/mainnet)
  if (hre.network.name !== "hardhat" && hre.network.name !== "localhost") {
    console.log("\n⏳ Waiting for block confirmations before verification...");
    await token.deploymentTransaction().wait(6);
    await platform.deploymentTransaction().wait(6);

    console.log("\n🔍 Verifying contracts on Etherscan...");
    try {
      await hre.run("verify:verify", {
        address: tokenAddress,
        constructorArguments: [initialSupply]
      });
      console.log("✅ W3SocialToken verified");

      await hre.run("verify:verify", {
        address: platformAddress,
        constructorArguments: [tokenAddress]
      });
      console.log("✅ W3SocialPlatform verified");
    } catch (error) {
      console.log("⚠️ Contract verification failed:", error.message);
    }
  }

  // Print deployment info
  console.log("\n" + "=".repeat(50));
  console.log("📋 DEPLOYMENT SUMMARY");
  console.log("=".repeat(50));
  console.log("Network:", hre.network.name);
  console.log("Deployer:", deployer.address);
  console.log("W3SocialToken:", tokenAddress);
  console.log("W3SocialPlatform:", platformAddress);
  console.log("=".repeat(50));

  // Save deployment info
  const fs = require("fs");
  const deploymentInfo = {
    network: hre.network.name,
    deployer: deployer.address,
    tokenAddress: tokenAddress,
    platformAddress: platformAddress,
    deployedAt: new Date().toISOString()
  };

  const deploymentsDir = "./deployments";
  if (!fs.existsSync(deploymentsDir)) {
    fs.mkdirSync(deploymentsDir);
  }

  fs.writeFileSync(
    `${deploymentsDir}/${hre.network.name}.json`,
    JSON.stringify(deploymentInfo, null, 2)
  );
  console.log("\n💾 Deployment info saved to:", `${deploymentsDir}/${hre.network.name}.json`);

  return { tokenAddress, platformAddress };
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
