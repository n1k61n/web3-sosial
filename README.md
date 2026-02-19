# W3SOCIAL — Decentralized Social Network

A fully **blockchain-based** social networking platform with token rewards, decentralized storage, and Web3 authentication.

## 🚀 Quick Start

### Windows

```bash
# Just double-click start.bat
start.bat
```

### Linux/Mac

```bash
chmod +x start.sh
./start.sh
```

That's it! All services will start automatically. Access the app at **http://localhost:5173**

For detailed setup instructions, see [QUICKSTART.md](QUICKSTART.md).

---

## CI/CD Status

[![Backend CI](https://github.com/mamedov/web3-sosial/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/mamedov/web3-sosial/actions/workflows/backend-ci.yml)
[![Frontend CI](https://github.com/mamedov/web3-sosial/actions/workflows/frontend-ci.yml/badge.svg)](https://github.com/mamedov/web3-sosial/actions/workflows/frontend-ci.yml)
[![Docker Build](https://github.com/mamedov/web3-sosial/actions/workflows/docker-build.yml/badge.svg)](https://github.com/mamedov/web3-sosial/actions/workflows/docker-build.yml)
[![CD Deploy](https://github.com/mamedov/web3-sosial/actions/workflows/cd-deploy.yml/badge.svg)](https://github.com/mamedov/web3-sosial/actions/workflows/cd-deploy.yml)

## Technologies

### Backend

* **Java 17** + **Spring Boot 3.x**
* **Spring Cloud Netflix Eureka** — Service discovery and registry
* **Spring Cloud Gateway** — API Gateway
* **Spring Security** + **JWT** — Authentication & Authorization
* **Spring Data JPA** + **Hibernate** — ORM and Database management
* **Apache Kafka** — Asynchronous messaging
* **PostgreSQL** — Relational database
* **Web3j** — Ethereum blockchain integration
* **Lombok** — Boilerplate code reduction

### Frontend

* **React 18** + **Vite**
* **TailwindCSS** — Utility-first styling
* **ethers.js** — Web3 wallet integration
* **React Query** — Server state management
* **React Router** — Client-side navigation
* **Axios** — HTTP client

### Blockchain & Web3

* **Solidity 0.8** — Smart contracts
* **Hardhat** — Ethereum development environment
* **OpenZeppelin** — Secure contract standards
* **IPFS** — Decentralized content storage
* **Ganache** — Local blockchain for testing
* **MetaMask** — Wallet integration
* **ERC-20** — W3S token for rewards

### Infrastructure

* **Docker** + **Docker Compose** — Containerization
* **MetaMask** — Wallet-based authentication

---

## Architecture

```text
web3-social/
├── backend/
│   ├── eureka-server/         # Service Registry (port: 8761)
│   ├── gateway-service/       # API Gateway (port: 8080)
│   ├── auth-service/          # Authentication (port: 8081)
│   ├── user-service/          # User Management (port: 8082)
│   ├── post-service/          # Post Management (port: 8083)
│   ├── notification-service/  # Notification System (port: 8084)
│   └── blockchain-service/    # Blockchain Integration (port: 8085)
├── frontend/                  # React UI (port: 5173)
├── blockchain/                # Smart Contracts (Solidity)
│   ├── contracts/
│   │   ├── W3SocialToken.sol     # ERC-20 reward token
│   │   └── W3SocialPlatform.sol  # Social interactions
│   ├── scripts/
│   └── test/
├── docker-compose.yml
└── init.sql

```

## Microservices Overview

| Service | Port | Description |
| --- | --- | --- |
| **Eureka Server** | 8761 | Service discovery and registration |
| **Gateway Service** | 8080 | Single entry point for all requests |
| **Auth Service** | 8081 | Wallet authentication and JWT issuance |
| **User Service** | 8082 | Profile management, follow/unfollow logic |
| **Post Service** | 8083 | Post creation, likes, and comments |
| **Notification Service** | 8084 | Real-time notifications via Kafka |
| **Blockchain Service** | 8085 | Smart contracts, IPFS, token rewards |

---

## Blockchain Features

### 🪙 Token Reward System (W3S Token)

Users earn **W3S tokens** for platform activities:

| Action | Reward |
|--------|--------|
| Create Post | 10 W3S |
| Like Post | 1 W3S |
| Comment | 2 W3S |
| Follow User | 5 W3S |

**Daily Limit:** 100 W3S per user

### 📦 Decentralized Storage (IPFS)

All content is stored on IPFS:
- Post content and metadata
- Profile information
- Comments

### ⛓️ Smart Contracts

- **W3SocialToken:** ERC-20 token for rewards
- **W3SocialPlatform:** On-chain social interactions

For detailed blockchain documentation, see [BLOCKCHAIN.md](BLOCKCHAIN.md).

---

## Installation & Setup

### Prerequisites

* Java 17+
* Maven
* Docker Desktop
* Node.js 18+
* MetaMask browser extension
* npm (for blockchain deployment)

### Running with Docker (Full Stack + Blockchain)

```bash
# Build all services including blockchain components
docker-compose build

# Start the environment (includes Ganache, IPFS, Kafka)
docker-compose up -d

# Verify status
docker ps

# Check blockchain services
curl http://localhost:8545  # Ganache (local Ethereum)
curl http://localhost:5001/api/v0/ping  # IPFS
```

### Deploy Smart Contracts

```bash
cd blockchain

# Install dependencies
npm install

# Compile contracts
npm run compile

# Deploy to local Ganache
npm run deploy:local

# View deployment info
cat deployments/localhost.json
```

### Frontend Setup

```bash
cd frontend
npm install
npm run dev

```

* **Frontend:** `http://localhost:5173`
* **Eureka Dashboard:** `http://localhost:8761`
* **Blockchain Service:** `http://localhost:8085`
* **IPFS Gateway:** `http://localhost:8081`

### Building Individual Services

```bash
cd backend/auth-service
mvn clean package -DskipTests
cd ../..
docker-compose build auth-service
docker-compose up -d auth-service

```

---

## API Endpoints

### Auth Service

| Method | URL | Description |
| --- | --- | --- |
| GET | `/api/auth/nonce/{wallet}` | Retrieve a unique nonce |
| POST | `/api/auth/authenticate` | Login with wallet signature |
| GET | `/api/auth/user/{wallet}` | Get auth user details |

### User Service

| Method | URL | Description |
| --- | --- | --- |
| GET | `/api/users/{wallet}` | Get profile |
| POST | `/api/users/{wallet}` | Create profile |
| PUT | `/api/users/{wallet}` | Update profile |
| POST | `/api/users/follow` | Follow a user |
| POST | `/api/users/unfollow` | Unfollow a user |
| GET | `/api/users/{wallet}/followers` | Get followers list |
| GET | `/api/users/{wallet}/following` | Get following list |

### Post Service

| Method | URL | Description |
| --- | --- | --- |
| GET | `/api/posts` | Fetch all posts |
| POST | `/api/posts` | Create a new post |
| GET | `/api/posts/{id}` | Get specific post |
| GET | `/api/posts/user/{wallet}` | Get posts by user |
| DELETE | `/api/posts/{id}/{wallet}` | Delete a post |
| POST | `/api/posts/{id}/like/{wallet}` | Like a post |
| DELETE | `/api/posts/{id}/like/{wallet}` | Unlike a post |
| POST | `/api/posts/{id}/comments` | Add a comment |
| GET | `/api/posts/{id}/comments` | Get all comments |

### Notification Service

| Method | URL | Description |
| --- | --- | --- |
| GET | `/api/notifications/{wallet}` | Get all notifications |
| GET | `/api/notifications/{wallet}/unread` | Get unread notifications |
| GET | `/api/notifications/{wallet}/count` | Get unread count |
| PUT | `/api/notifications/{id}/read` | Mark as read |
| PUT | `/api/notifications/{wallet}/read-all` | Mark all as read |

### Blockchain Service (Port 8085)

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/blockchain/verify-signature` | Verify Ethereum signature |
| POST | `/api/blockchain/posts?wallet=0x...` | Create post on IPFS |
| POST | `/api/blockchain/profiles?wallet=0x...` | Create profile on IPFS |
| POST | `/api/blockchain/posts/{id}/like` | Like post + trigger reward |
| GET | `/api/blockchain/tokens/balance/{wallet}` | Get W3S token balance |
| GET | `/api/blockchain/rewards/{wallet}` | Get reward summary |
| GET | `/api/blockchain/balance/{wallet}` | Get ETH balance |
| GET | `/api/blockchain/block-number` | Get current block number |

---

## Kafka Topics

| Topic | Publisher | Consumer | Description |
| --- | --- | --- | --- |
| `notifications` | post-service, user-service | notification-service | Triggers for likes, comments, and follows |
| `blockchain-events` | All services | blockchain-service | Blockchain transactions and rewards |

---

## Roadmap

### ✅ Completed

* [x] **IPFS Integration** — Store post content on IPFS.
* [x] **Smart Contracts** — Posts and profiles on Ethereum blockchain.
* [x] **Token Reward System** — W3S ERC-20 token for user rewards.
* [x] **Wallet Authentication** — MetaMask login with signature verification.
* [x] **CI/CD Pipeline** — GitHub Actions for automated deployment.

### 🚧 In Progress

* [ ] **NFT Profile Pictures** — Support for verified NFT avatars.
* [ ] **Feed Service** — Personalize feeds based on following lists.
* [ ] **Redis Caching** — Performance optimization.

### 📋 Planned

* [ ] **Kubernetes** — Orchestration for production deployment.
* [ ] **Governance DAO** — Community-driven platform decisions.
* [ ] **Cross-chain Support** — Polygon, BSC integration.
* [ ] **Staking** — Stake W3S tokens for premium features.

---

## CI/CD Pipeline

This project uses GitHub Actions for automated build, test, and deployment.

### Workflows

| Workflow | Description |
|----------|-------------|
| **Backend CI** | Builds, tests, and runs code quality checks on all microservices |
| **Frontend CI** | Builds, lints, and audits the React application |
| **Docker Build** | Builds and pushes Docker images to GitHub Container Registry |
| **CD Deploy** | Deploys to staging (auto) and production (manual) environments |

### Quick Start

```bash
# All workflows trigger automatically on push to main/develop
# Manual deployment can be triggered from Actions tab
```

For detailed CI/CD documentation, see [`.github/CI_CD.md`](.github/CI_CD.md).

---

## Authentication Flow

1. User clicks the **"Login with MetaMask"** button.
2. Frontend requests a unique **nonce** from `/api/auth/nonce` using the wallet address.
3. Server returns a unique, time-stamped nonce.
4. User signs the nonce via MetaMask.
5. Frontend sends the resulting signature to `/api/auth/authenticate`.
6. Server verifies the **Ethereum signature** against the nonce and wallet address.
7. Upon successful verification, a **JWT token** is issued.
8. The user can now access protected endpoints using the JWT.

---

## License

Distributed under the MIT License.