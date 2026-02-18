This is a well-structured technical README for a Web3 project. Here is the professional English translation, optimized for a GitHub repository or technical documentation.

---

# W3SOCIAL — Decentralized Social Network

A microservices-based social networking platform built on Web3 technology.

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
│   └── notification-service/  # Notification System (port: 8084)
├── frontend/                  # React UI (port: 5173)
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

---

## Web3 Features

* **Wallet Authentication:** Passwordless login using MetaMask.
* **Ethereum Signature Verification:** Secure cryptographic signature validation.
* **Nonce-based Auth:** Protection against replay attacks.
* **JWT Token:** Session management after successful blockchain authentication.

---

## Installation & Setup

### Prerequisites

* Java 17+
* Maven
* Docker Desktop
* Node.js 18+
* MetaMask browser extension

### Running with Docker

```bash
# Build all services
docker-compose build

# Start the environment
docker-compose up -d

# Verify status
docker ps

```

### Frontend Setup

```bash
cd frontend
npm install
npm run dev

```

* **Frontend:** `http://localhost:5173`
* **Eureka Dashboard:** `http://localhost:8761`

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

---

## Kafka Topics

| Topic | Publisher | Consumer | Description |
| --- | --- | --- | --- |
| `notifications` | post-service, user-service | notification-service | Triggers for likes, comments, and follows |

---

## Roadmap

* [ ] **IPFS Integration** — Store post content on IPFS.
* [ ] **Smart Contracts** — Hash posts on the Ethereum blockchain.
* [ ] **Token Reward System** — Incentivize active users with tokens.
* [ ] **NFT Profile Pictures** — Support for verified NFT avatars.
* [ ] **Feed Service** — Personalize feeds based on following lists.
* [ ] **Redis Caching** — Performance optimization.
* [ ] **Kubernetes** — Orchestration for production deployment.

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