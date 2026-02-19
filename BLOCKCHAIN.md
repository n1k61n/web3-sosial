# Blockchain Integration Guide

## Overview

W3Social is now a **fully decentralized social network** with blockchain integration. This guide explains the blockchain architecture and how to use it.

---

## 🏗️ Blockchain Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    W3Social Platform                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Frontend (React + ethers.js)                               │
│       │                                                      │
│       ▼                                                      │
│  Gateway Service (API Gateway)                              │
│       │                                                      │
│       ├──────────────┬──────────────┬───────────────┐       │
│       ▼              ▼              ▼               ▼       │
│   Auth Service   User Service   Post Service   Blockchain  │
│                                       │        Service     │
│                                       │         │          │
│                                       │         ▼          │
│                                       │    ┌────┴────┐     │
│                                       │    │ Kafka   │     │
│                                       │    └────┬────┘     │
│                                       │         │          │
│                                       ▼         ▼          │
│                              ┌──────────────────────────┐  │
│                              │   Smart Contracts        │  │
│                              │   ┌──────────────────┐   │  │
│                              │   │ W3SocialToken    │   │  │
│                              │   │ (ERC-20 Rewards) │   │  │
│                              │   └──────────────────┘   │  │
│                              │   ┌──────────────────┐   │  │
│                              │   │ W3SocialPlatform │   │  │
│                              │   │ (Posts/Profiles) │   │  │
│                              │   └──────────────────┘   │  │
│                              └──────────────────────────┘  │
│                                       │                     │
│                                       ▼                     │
│                              ┌──────────────────┐          │
│                              │ IPFS (Content)   │          │
│                              │ - Post Content   │          │
│                              │ - Profile Data   │          │
│                              └──────────────────┘          │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 Smart Contracts

### 1. W3SocialToken (ERC-20)

**Purpose:** Reward users for platform activities

**Features:**
- Token symbol: `W3S`
- Rewards for actions (posts, likes, comments, follows)
- Daily minting limit (100 W3S per user)
- Admin-controlled reward distribution

**Reward Structure:**
| Action | Reward |
|--------|--------|
| Create Post | 10 W3S |
| Like Post | 1 W3S |
| Comment | 2 W3S |
| Follow User | 5 W3S |

### 2. W3SocialPlatform

**Purpose:** Store social interactions on-chain

**Features:**
- Profile creation and management
- Post creation with IPFS hashes
- Like/unlike functionality
- Follow/unfollow system
- Event emission for off-chain indexing

---

## 🚀 Quick Start

### 1. Start Local Blockchain

```bash
# Start all services including Ganache (local Ethereum)
docker-compose up -d

# Check Ganache is running
curl http://localhost:8545 -X POST -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"eth_blockNumber","params":[],"id":1}'
```

### 2. Deploy Smart Contracts

```bash
cd blockchain

# Install dependencies
npm install

# Compile contracts
npm run compile

# Deploy to local Ganache
npm run deploy:local

# Deploy to Sepolia testnet
npm run deploy:sepolia
```

### 3. Configure Environment

Create `.env` file in `blockchain/` directory:

```env
# Private key for deployment (Ganache default)
PRIVATE_KEY=0x2a3b... (from Ganache output)

# For Sepolia deployment
ALCHEMY_SEPOLIA_URL=https://eth-sepolia.g.alchemy.com/v2/YOUR_KEY
ETHERSCAN_API_KEY=your_api_key
```

---

## 📡 API Endpoints

### Blockchain Service (Port 8085)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/blockchain/verify-signature` | Verify Ethereum signature |
| POST | `/api/blockchain/posts?wallet=0x...` | Create post on IPFS |
| POST | `/api/blockchain/profiles?wallet=0x...` | Create profile on IPFS |
| POST | `/api/blockchain/posts/{id}/like` | Like post + trigger reward |
| GET | `/api/blockchain/tokens/balance/{wallet}` | Get W3S token balance |
| GET | `/api/blockchain/rewards/{wallet}` | Get reward summary |
| GET | `/api/blockchain/balance/{wallet}` | Get ETH balance |
| GET | `/api/blockchain/block-number` | Get current block number |

---

## 🔐 Authentication Flow (Web3)

```
1. User clicks "Connect Wallet"
   └─> MetaMask opens

2. Frontend requests nonce from Auth Service
   └─> GET /api/auth/nonce/{wallet}

3. User signs message with MetaMask
   └─> Message: "Sign to login: {nonce}"

4. Frontend sends signature to backend
   └─> POST /api/auth/authenticate

5. Blockchain Service verifies signature
   └─> ECDSA signature recovery

6. JWT token issued
   └─> User authenticated
```

---

## 📝 Creating Posts (Decentralized)

```javascript
// Frontend code example
const createPost = async (content, walletAddress) => {
  // 1. Upload to IPFS via backend
  const response = await fetch(
    `http://localhost:8085/api/blockchain/posts?wallet=${walletAddress}`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ content })
    }
  );
  
  const result = await response.json();
  
  // 2. result.ipfsHash contains the IPFS hash
  // 3. result.ipfsUrl contains the gateway URL
  // 4. Event sent to blockchain for on-chain storage
  
  return result;
};
```

---

## 💰 Token Rewards System

### How It Works

1. User performs action (post, like, comment, follow)
2. Service publishes event to Kafka topic `blockchain-events`
3. Blockchain Service consumes event
4. Smart contract mints and transfers W3S tokens
5. User's wallet balance updates

### Checking Rewards

```bash
# Get user's token balance
curl http://localhost:8085/api/blockchain/tokens/balance/0xYOUR_WALLET

# Get detailed reward summary
curl http://localhost:8085/api/blockchain/rewards/0xYOUR_WALLET
```

---

## 🌐 IPFS Integration

### Content Storage

All user-generated content is stored on IPFS:

- **Posts:** Content + metadata (author, timestamp)
- **Profiles:** Username + bio + metadata
- **Comments:** Comment text + author

### IPFS Configuration

```yaml
# docker-compose.yml
ipfs:
  image: ipfs/kubo:latest
  ports:
    - "5001:5001"  # API
    - "8081:8080"  # Gateway
```

### Accessing IPFS Content

```bash
# Via IPFS gateway
curl http://localhost:8081/ipfs/{HASH}

# Via IPFS CLI
ipfs cat {HASH}
```

---

## 🔧 Development

### Local Testing

```bash
# Start local blockchain
docker-compose up ganache

# In another terminal, deploy contracts
cd blockchain
npm run deploy:local

# Run tests
npm test
```

### Testnet Deployment (Sepolia)

1. Get test ETH from faucet: https://sepoliafaucet.com/
2. Configure `.env` with Sepolia details
3. Deploy: `npm run deploy:sepolia`
4. Verify on Etherscan: `npm run verify`

### Mainnet Deployment

```bash
# Configure for Ethereum mainnet
export ALCHEMY_MAINNET_URL=https://eth-mainnet.g.alchemy.com/v2/YOUR_KEY
export PRIVATE_KEY=your_private_key

# Deploy
npx hardhat run scripts/deploy.js --network mainnet
```

---

## 📊 Contract Addresses

After deployment, addresses are saved to:

```
blockchain/deployments/
├── localhost.json
├── sepolia.json
└── polygon.json
```

---

## 🔍 Monitoring

### Blockchain Events

Listen to smart contract events:

```javascript
const ethers = require('ethers');

// Connect to provider
const provider = new ethers.providers.WebSocketProvider('ws://localhost:8545');

// Contract ABI (from artifacts)
const contract = new ethers.Contract(
  PLATFORM_ADDRESS,
  PLATFORM_ABI,
  provider
);

// Listen for PostCreated events
contract.on('PostCreated', (postId, author, hash, timestamp) => {
  console.log('New post:', { postId, author, hash });
});
```

### Kafka Topics

| Topic | Publisher | Consumer |
|-------|-----------|----------|
| `blockchain-events` | All services | blockchain-service |
| `notifications` | post-service, user-service | notification-service |

---

## 🛡️ Security Considerations

1. **Private Keys:** Never commit private keys to git
2. **Rate Limiting:** Implement rate limiting on blockchain endpoints
3. **Gas Optimization:** Batch transactions when possible
4. **Signature Replay:** Use nonces to prevent replay attacks
5. **Access Control:** Admin functions restricted to contract owner

---

## 📚 Resources

- [Ethereum Documentation](https://ethereum.org/developers/)
- [Web3j Documentation](https://docs.web3j.io/)
- [IPFS Documentation](https://docs.ipfs.tech/)
- [Hardhat Documentation](https://hardhat.org/docs)
- [OpenZeppelin Contracts](https://docs.openzeppelin.com/contracts/)

---

## 🐛 Troubleshooting

### Ganache Connection Issues

```bash
# Check if Ganache is running
docker ps | grep ganache

# Restart Ganache
docker-compose restart ganache

# Check logs
docker-compose logs ganache
```

### Contract Deployment Fails

```bash
# Check account balance
curl http://localhost:8545 -X POST -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"eth_getBalance","params":["0xYOUR_ADDRESS","latest"],"id":1}'

# Increase gas limit in hardhat.config.js
```

### IPFS Upload Fails

```bash
# Check IPFS is running
docker-compose ps ipfs

# Test IPFS connection
curl http://localhost:5001/api/v0/ping
```

---

## 🎯 Next Steps

1. Deploy contracts to testnet (Sepolia/Mumbai)
2. Integrate smart contract calls in frontend
3. Implement token swap functionality
4. Add NFT profile pictures
5. Create governance DAO for platform decisions
