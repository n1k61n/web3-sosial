# CI/CD Pipeline Documentation

## Overview

This project uses **GitHub Actions** for continuous integration and deployment. The pipeline consists of 4 main workflows:

1. **Backend CI** - Build, test, and code quality for Java microservices
2. **Frontend CI** - Build, test, and lint React application
3. **Docker Build & Push** - Build and push Docker images to GHCR
4. **CD Deploy** - Deploy to staging/production environments

---

## Workflows

### 1. Backend CI (`backend-ci.yml`)

**Triggers:**
- Push to `main` or `develop` branches (backend changes only)
- Pull requests targeting `main` or `develop`

**Jobs:**
| Job | Description |
|-----|-------------|
| `build` | Builds and tests each microservice independently |
| `code-quality` | Runs Spotless formatting check and PMD analysis |
| `security-scan` | OWASP dependency check for vulnerabilities |

**Artifacts:**
- Test reports (retained for 7 days)
- JAR files (retained for 7 days)

---

### 2. Frontend CI (`frontend-ci.yml`)

**Triggers:**
- Push to `main` or `develop` branches (frontend changes only)
- Pull requests targeting `main` or `develop`

**Jobs:**
| Job | Description |
|-----|-------------|
| `build` | Installs dependencies, runs lint, builds production bundle |
| `code-quality` | ESLint and TypeScript checks |
| `security-audit` | npm audit for vulnerable dependencies |

**Artifacts:**
- Production build in `frontend/dist/` (retained for 7 days)

---

### 3. Docker Build & Push (`docker-build.yml`)

**Triggers:**
- Push to `main` or `develop` branches
- Version tags (e.g., `v1.0.0`)
- Pull requests to `main`

**Images Built:**
- `ghcr.io/mamedov/web3-sosial/eureka-server`
- `ghcr.io/mamedov/web3-sosial/gateway-service`
- `ghcr.io/mamedov/web3-sosial/auth-service`
- `ghcr.io/mamedov/web3-sosial/user-service`
- `ghcr.io/mamedov/web3-sosial/post-service`
- `ghcr.io/mamedov/web3-sosial/notification-service`
- `ghcr.io/mamedov/web3-sosial/frontend`

**Tags:**
- Branch name (e.g., `main`, `develop`)
- Semantic version (e.g., `1.0.0`, `1.0`)
- SHA hash (e.g., `sha-abc123`)
- `latest` (only for `main` branch)

---

### 4. CD Deploy (`cd-deploy.yml`)

**Triggers:**
- Successful completion of Docker Build workflow
- Manual trigger via `workflow_dispatch`
- Version tags for production deployment

**Environments:**

| Environment | Branch | URL |
|-------------|--------|-----|
| Staging | `develop` | https://staging.web3social.example.com |
| Production | `main` / tags | https://web3social.example.com |

**Features:**
- Automatic health checks
- Database backup (production only)
- Rolling updates (production only)
- Automatic rollback on failure

---

## Setup Instructions

### 1. Configure GitHub Secrets

Go to **Repository Settings → Secrets and variables → Actions** and add:

#### Required Secrets:

| Secret | Description | Example |
|--------|-------------|---------|
| `DB_PASSWORD` | Staging database password | `staging_secure_pass_123` |
| `JWT_SECRET` | Staging JWT signing key | `staging_jwt_secret_key` |
| `PROD_DB_PASSWORD` | Production database password | `prod_secure_pass_456` |
| `PROD_JWT_SECRET` | Production JWT signing key | `prod_jwt_secret_key` |

#### Optional Secrets:

| Secret | Description |
|--------|-------------|
| `DOCKERHUB_USERNAME` | Docker Hub username (if using Docker Hub) |
| `DOCKERHUB_TOKEN` | Docker Hub access token |

---

### 2. Configure Environments

Go to **Repository Settings → Environments** and create:

#### Staging Environment:
- Name: `staging`
- URL: `https://staging.web3social.example.com`
- (Optional) Required reviewers for approval

#### Production Environment:
- Name: `production`
- URL: `https://web3social.example.com`
- **Recommended:** Add required reviewers for deployment approval

---

### 3. Manual Deployment

To manually trigger deployment:

1. Go to **Actions** tab
2. Select **"CD - Deploy to Environment"**
3. Click **"Run workflow"**
4. Choose environment (staging/production)
5. Click **"Run workflow"**

---

## Pipeline Visualization

```
┌─────────────┐
│   Code      │
│   Push      │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────────┐
│  Parallel CI Workflows                  │
│  ┌──────────────┐  ┌─────────────────┐  │
│  │  Backend CI  │  │  Frontend CI    │  │
│  │  - Build     │  │  - Build        │  │
│  │  - Test      │  │  - Lint         │  │
│  │  - Quality   │  │  - Audit        │  │
│  └──────┬───────┘  └────────┬────────┘  │
└─────────┼───────────────────┼───────────┘
          │                   │
          └────────┬──────────┘
                   │
                   ▼
          ┌────────────────┐
          │ Docker Build   │
          │ & Push to GHCR │
          └───────┬────────┘
                  │
                  ▼
          ┌────────────────┐
          │ CD Deploy      │
          │ - Staging      │
          │ - Production   │
          └────────────────┘
```

---

## Local Testing

### Test Backend CI Locally

```bash
# Build all services
mvn clean package -DskipTests

# Run tests
mvn test

# Code quality check
mvn spotless:check
mvn pmd:check
```

### Test Frontend CI Locally

```bash
cd frontend

# Install dependencies
npm ci

# Run lint
npm run lint

# Build
npm run build
```

### Test Docker Build Locally

```bash
# Build specific service
docker build -t web3social/auth-service ./backend/auth-service

# Build frontend
docker build -t web3social/frontend ./frontend

# Build all with docker-compose
docker-compose build
```

---

## Troubleshooting

### Build Fails

1. Check **Actions** tab for detailed logs
2. Download test artifacts for failure analysis
3. Reproduce locally using commands above

### Docker Push Fails

1. Verify GitHub token permissions
2. Check package write permissions in repository settings
3. Ensure GHCR is accessible

### Deployment Fails

1. Check environment secrets are configured
2. Verify Docker images exist in GHCR
3. Check service health endpoints
4. Review deployment logs for specific errors

---

## Best Practices

1. **Never commit secrets** - Use GitHub Secrets only
2. **Review PRs before merge** - CI runs on PRs
3. **Monitor build times** - Optimize slow steps
4. **Keep artifacts clean** - Retention set to 7 days
5. **Use environments** - Separate staging/production
6. **Require approvals** - For production deployments
7. **Tag releases** - Use semantic versioning (v1.0.0)

---

## Monitoring

### GitHub Actions Status Badges

Add these to your README:

```markdown
[![Backend CI](https://github.com/mamedov/web3-sosial/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/mamedov/web3-sosial/actions/workflows/backend-ci.yml)
[![Frontend CI](https://github.com/mamedov/web3-sosial/actions/workflows/frontend-ci.yml/badge.svg)](https://github.com/mamedov/web3-sosial/actions/workflows/frontend-ci.yml)
[![Docker Build](https://github.com/mamedov/web3-sosial/actions/workflows/docker-build.yml/badge.svg)](https://github.com/mamedov/web3-sosial/actions/workflows/docker-build.yml)
[![CD Deploy](https://github.com/mamedov/web3-sosial/actions/workflows/cd-deploy.yml/badge.svg)](https://github.com/mamedov/web3-sosial/actions/workflows/cd-deploy.yml)
```

---

## Future Enhancements

- [ ] Add SonarQube integration for code quality gates
- [ ] Implement canary deployments
- [ ] Add Slack/Discord notifications
- [ ] Integrate with Kubernetes for production
- [ ] Add performance testing stage
- [ ] Implement blue-green deployment strategy
