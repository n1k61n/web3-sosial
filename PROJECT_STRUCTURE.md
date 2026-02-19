# 📁 W3Social Project Structure

## Quick Navigation

| File/Directory | Description |
|----------------|-------------|
| [`start.bat`](start.bat) | **Windows** - Start all services with one click |
| [`start.sh`](start.sh) | **Linux/Mac** - Start all services |
| [`QUICKSTART.md`](QUICKSTART.md) | Detailed setup guide |
| [`BLOCKCHAIN.md`](BLOCKCHAIN.md) | Blockchain integration documentation |
| [`docker-compose.yml`](docker-compose.yml) | Docker configuration for all services |

---

## 📂 Directory Structure

```
web3-sosial/
├── 📁 backend/                    # Java Spring Boot Microservices
│   ├── eureka-server/            # Service Discovery (Port 8761)
│   ├── gateway-service/          # API Gateway (Port 8080)
│   ├── auth-service/             # Authentication (Port 8081)
│   ├── user-service/             # User Management (Port 8082)
│   ├── post-service/             # Posts & Comments (Port 8083)
│   ├── notification-service/     # Notifications (Port 8084)
│   └── blockchain-service/       # Web3 Integration (Port 8085)
│
├── 📁 frontend/                   # React 18 + Vite + ethers.js
│   ├── src/                      # React components
│   ├── Dockerfile                # Frontend Docker image
│   └── nginx.conf                # Nginx configuration
│
├── 📁 blockchain/                 # Smart Contracts (Solidity)
│   ├── contracts/
│   │   ├── W3SocialToken.sol     # ERC-20 Reward Token
│   │   └── W3SocialPlatform.sol  # Social Interactions
│   ├── scripts/
│   │   └── deploy.js             # Deployment script
│   ├── test/
│   │   └── W3Social.test.js      # Contract tests
│   └── hardhat.config.js         # Hardhat configuration
│
├── 📁 .github/
│   └── workflows/
│       ├── backend-ci.yml        # Backend CI pipeline
│       ├── frontend-ci.yml       # Frontend CI pipeline
│       ├── docker-build.yml      # Docker image build
│       └── cd-deploy.yml         # CD deployment
│
├── 📄 docker-compose.yml          # All services orchestration
├── 📄 init.sql                    # Database initialization
├── 📄 .env                        # Environment variables
├── 📄 start.bat                   # Windows startup script
├── 📄 start.sh                    # Linux/Mac startup script
├── 📄 QUICKSTART.md               # Quick start guide
├── 📄 BLOCKCHAIN.md               # Blockchain documentation
└── 📄 README.md                   # Main documentation
```

---

## 🌐 Service Ports

| Service | Port | URL |
|---------|------|-----|
| Frontend | 5173 | http://localhost:5173 |
| API Gateway | 8080 | http://localhost:8080 |
| Auth Service | 8081 | http://localhost:8081 |
| User Service | 8082 | http://localhost:8082 |
| Post Service | 8083 | http://localhost:8083 |
| Notification Service | 8084 | http://localhost:8084 |
| Blockchain Service | 8085 | http://localhost:8085 |
| Eureka Server | 8761 | http://localhost:8761 |
| Ganache (Blockchain) | 8545 | http://localhost:8545 |
| IPFS Gateway | 8081 | http://localhost:8081 |
| PostgreSQL | 5432 | http://localhost:5432 |
| Kafka | 9092 | http://localhost:9092 |

---

## 🛠️ Tech Stack

### Backend
- Java 17 + Spring Boot 3.x
- Spring Cloud (Eureka, Gateway)
- Spring Security + JWT
- Web3j (Ethereum integration)
- Apache Kafka
- PostgreSQL

### Frontend
- React 18 + Vite
- ethers.js (Web3)
- TailwindCSS
- React Query
- React Router

### Blockchain
- Solidity 0.8
- Hardhat
- OpenZeppelin Contracts
- IPFS (Decentralized storage)
- Ganache (Local blockchain)

### Infrastructure
- Docker + Docker Compose
- GitHub Actions (CI/CD)
- Nginx

---

## 📝 Key Features

✅ **Wallet Authentication** - Login with MetaMask  
✅ **Token Rewards** - Earn W3S tokens for activities  
✅ **Decentralized Storage** - Content stored on IPFS  
✅ **Smart Contracts** - On-chain social interactions  
✅ **Microservices** - Scalable architecture  
✅ **CI/CD** - Automated testing and deployment  

---

## 🚀 Getting Started

1. **Clone repository**
   ```bash
   git clone https://github.com/mamedov/web3-sosial.git
   cd web3-sosial
   ```

2. **Start all services**
   - Windows: Double-click `start.bat`
   - Linux/Mac: `./start.sh`

3. **Access application**
   - Frontend: http://localhost:5173
   - Eureka: http://localhost:8761

---

## 📞 Support

- **Quick Start:** [`QUICKSTART.md`](QUICKSTART.md)
- **Blockchain Guide:** [`BLOCKCHAIN.md`](BLOCKCHAIN.md)
- **CI/CD Docs:** [`.github/CI_CD.md`](.github/CI_CD.md)
