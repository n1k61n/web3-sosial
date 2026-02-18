# BlockBuzz

This is a web3 social media platform built with a microservices architecture. The backend is built with Java and Spring Boot, and the frontend is a single-page application built with React.

## Technologies Used

*   **Backend:** Java, Spring Boot, Spring Cloud, Eureka, Kafka, PostgreSQL
*   **Frontend:** React, Vite, Ethers.js, TanStack Query, Tailwind CSS
*   **Deployment:** Docker, Docker Compose

## Architecture

The application is composed of the following services:

*   **`postgres`**: The main database for the application.
*   **`kafka`**: A messaging system for asynchronous communication between services.
*   **`eureka-server`**: A service discovery server that allows services to find each other.
*   **`auth-service`**: Handles user authentication and authorization.
*   **`user-service`**: Manages user profiles and relationships.
*   **`post-service`**: Manages posts and user feeds.
*   **`notification-service`**: Sends notifications to users.
*   **`gateway-service`**: An API gateway that provides a single entry point for all client requests.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

*   [Docker](https://docs.docker.com/get-docker/)
*   [Docker Compose](https://docs.docker.com/compose/install/)
*   [Node.js and npm](https://nodejs.org/en/)

### Installation

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/your-username/web3-sosial.git
    cd web3-sosial
    ```

2.  **Run the backend services:**

    ```bash
    docker-compose up --build
    ```

    This command will build the Docker images for each backend service and start the containers.

3.  **Run the frontend application:**

    In a separate terminal, navigate to the `frontend` directory:

    ```bash
    cd frontend
    npm install
    npm run dev
    ```

4.  **Database Initialization:**

    The `postgres` service is initialized with the `init.sql` script, which creates the necessary databases for the services.

## Service Endpoints

Once the application is running, the services will be available at the following ports on your local machine:

*   **Frontend:** `http://localhost:5173`
*   **API Gateway:** `http://localhost:8080`
*   **Eureka Server:** `http://localhost:8761`
*   **Auth Service:** `http://localhost:8081`
*   **User Service:** `http://localhost:8082`
*   **Post Service:** `http://localhost:8083`
*   **Notification Service:** `http://localhost:8084`

The frontend development server is configured to proxy API requests from `/api` to the API gateway at `http://localhost:8080`.
