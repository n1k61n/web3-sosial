# 🚀 W3Social - Quick Start Guide

## Tələblər (Prerequisites)

- **Docker Desktop** (Windows/Mac) və ya **Docker + Docker Compose** (Linux)
- **Node.js 18+** (blockchain üçün)
- **Git**

---

## 📀 Windows-də İşə Salma

### Variant 1: Start.bat faylı ilə (Tövsiyə olunur)

```bash
# Sadəcə start.bat faylını iki dəfə klikləyin
start.bat
```

### Variant 2: Əl ilə

```powershell
# 1. Terminalı administrator kimi açın

# 2. Blockchain contract-lərini quraşdırın
cd blockchain
npm install
npm run compile

# 3. Docker image-lərini build edin
cd ..
docker-compose build

# 4. Servisləri işə salın
docker-compose up -d

# 5. Smart contract-ləri deploy edin (opsional)
deploy-contracts.bat
```

---

## 🐧 Linux/Mac-də İşə Salma

```bash
# 1. Start skriptini işə salın
chmod +x start.sh
./start.sh

# Və ya əl ilə:
docker-compose build
docker-compose up -d

# Smart contract-ləri deploy etmək üçün:
docker-compose run --rm ganache-deploy
```

---

## ✅ Yoxlama

Servislərin işlədiyini yoxlayın:

```bash
docker-compose ps
```

Və ya brauzerdə açın:

| Servis | URL |
|--------|-----|
| Frontend | http://localhost:5173 |
| API Gateway | http://localhost:8080 |
| Eureka Dashboard | http://localhost:8761 |
| Blockchain Service | http://localhost:8085 |
| IPFS Gateway | http://localhost:8081 |

---

## 🛑 Dayandırma

```bash
# Windows
stop.bat

# Linux/Mac
./stop.sh

# Və ya
docker-compose down
```

---

## 🔧 Problemlər və Həllər

### Docker işə düşmür
```bash
# Docker Desktop-u yenidən başladın
# Windows: Docker Desktop-u bağlayın və yenidən açın
```

### Port artıq istifadə olunur
```bash
# Portu işlədən prosesi tapın
netstat -ano | findstr :8080

# Prosesi dayandırın (Windows)
taskkill /PID <PID> /F

# Və ya docker-compose.yml-də portu dəyişin
```

### Blockchain deploy xətası
```bash
cd blockchain
npm install
npm run compile
npm run deploy:local
```

### IPFS xətası
```bash
# IPFS container-i yenidən başladın
docker-compose restart ipfs
```

---

## 📝 Faydalı Komandalar

```bash
# Bütün log-lara baxmaq
docker-compose logs -f

# Konkret servisin log-ları
docker-compose logs -f blockchain-service
docker-compose logs -f ganache

# Servislərin statusu
docker-compose ps

# Container-ə daxil olmaq
docker-compose exec ganache sh

# Təmizləmə (bütün image-ləri silmə)
docker-compose down -v
docker system prune -a
```

---

## 🎯 Növbəti Addımlar

1. Frontend-i açın: http://localhost:5173
2. MetaMask-ı qoşun
3. İlk postunuzu yaradın və W3S token qazanın!

---

## 📞 Kömək

Ətraflı məlumat üçün:
- [BLOCKCHAIN.md](BLOCKCHAIN.md) - Blockchain inteqrasiya
- [.github/CI_CD.md](.github/CI_CD.md) - CI/CD pipeline
