# Awful Toolkits Platform

A decoupled work toolkit web application providing project budget management and personnel management functions.

## Features

- **Project Budget Management**: Project CRUD, WBS (Work Breakdown Structure) feature decomposition (up to 5 levels), budget item entry, and aggregate calculations.
- **Personnel Management**: Personnel CRUD, monthly salary management and history, development role management, and project team allocation.

## Tech Stack

| Layer | Technology |
|------|------|
| Frontend | Vue 3 + TypeScript + Vite + Element Plus |
| Backend | Spring Boot 3.5 + MyBatis-Plus |
| Database | MySQL 8.x |
| Deployment | Docker + Docker Compose + Nginx |

## Project Structure

```
├── toolkits/                    # Backend Spring Boot multi-module project
│   ├── toolkits-bootstrap/      # Bootstrap module (Spring Boot entry point)
│   ├── toolkits-dal/            # Data Access Layer (MyBatis-Plus Mappers)
│   ├── toolkits-model/          # Entity models
│   ├── toolkits-service/        # Business logic layer
│   └── toolkits-web/            # Web/Controller layer (REST APIs)
├── toolkits-frontend/           # Frontend Vue project
├── docker-compose.yml           # Docker orchestration configuration
└── .env                         # Environment variables configuration
```

## Prerequisites

- JDK 17+
- Node.js 20+
- Maven 3.8+
- Docker & Docker Compose

## Build and Run

### 1. Build Backend

```bash
cd toolkits
mvn clean package -DskipTests
```

### 2. Build Frontend

```bash
cd toolkits-frontend
npm install
npm run build-only
```

### 3. Configure Environment Variables

Edit the `.env` file in the project root:

```env
# Database
MYSQL_ROOT_PASSWORD=${your_password}
MYSQL_DB_NAME=toolkits
MYSQL_USERNAME={your_username}
MYSQL_PASSWORD=${your_password}
DB_PORT=3306

# Backend
BACKEND_PORT=8080

# Frontend
FRONTEND_PORT=80

# AI (Optional)
DASH_SCOPE_API_KEY=${your_dashscope_api_key_here}
```

### 4. Start with Docker

```bash
docker-compose up -d
```

Three containers will start in order of dependency: MySQL → Backend → Frontend.

### 5. Access the Application

- Frontend: http://localhost
- Backend API: http://localhost:8080/api/v1

### Stop Services

```bash
docker-compose down
```

Data is persisted in a Docker volume and will not be lost after a restart.

## API Endpoints

| Module | Path | Description |
|------|------|------|
| Project | `GET/POST /api/v1/projects` | Project list / Create project |
| Project | `GET/PUT/DELETE /api/v1/projects/{id}` | Project details / Edit / Delete |
| Features | `GET/POST /api/v1/projects/{id}/features` | WBS tree / Add feature |
| Budget | `GET/POST /api/v1/features/{id}/budget-items` | Budget items list / Add new |
| Personnel| `GET/POST /api/v1/personnel` | Personnel list / Add new |
| Salary | `GET/POST /api/v1/personnel/{id}/salaries` | Salary history / Adjust salary |
| Roles | `GET/POST /api/v1/roles` | Roles list / Add new |
| Team | `GET/POST /api/v1/projects/{id}/team` | Project team / Allocate members |
