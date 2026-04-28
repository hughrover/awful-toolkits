# 工作工具集平台

一个前后端分离的工作工具集 Web 应用，提供项目预算管理和人员管理功能。

## 功能模块

- **项目预算管理**：项目 CRUD、WBS 功能拆解（最多 5 层）、预算项填写与汇总计算
- **项目人员管理**：人员 CRUD、月薪管理与历史记录、开发角色管理、项目团队分配

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3 + TypeScript + Vite + Element Plus |
| 后端 | Spring Boot 3.5 + MyBatis-Plus |
| 数据库 | MySQL 8.x |
| 部署 | Docker + Docker Compose + Nginx |

## 项目结构

```
├── toolkits/                    # 后端 Spring Boot 多模块项目
│   ├── toolkits-bootstrap/      # 启动模块（Spring Boot 入口）
│   ├── toolkits-dal/            # 数据访问层（MyBatis-Plus Mapper）
│   ├── toolkits-model/          # 实体模型
│   ├── toolkits-service/        # 业务逻辑层
│   └── toolkits-web/            # 控制器层（REST API）
├── toolkits-frontend/           # 前端 Vue 项目
├── docker-compose.yml           # Docker 编排配置
└── .env                         # 环境变量配置
```

## 前置要求

- JDK 17+
- Node.js 20+
- Maven 3.8+
- Docker & Docker Compose

## 构建与运行

### 1. 构建后端

```bash
cd toolkits
mvn clean package -DskipTests
```

### 2. 构建前端

```bash
cd toolkits-frontend
npm install
npm run build-only
```

### 3. 配置环境变量

编辑项目根目录 `.env` 文件：

```env
# 数据库
MYSQL_ROOT_PASSWORD=root123
MYSQL_DB_NAME=toolkits
MYSQL_USERNAME=root
MYSQL_PASSWORD=root123
DB_PORT=3306

# 后端
BACKEND_PORT=8080

# 前端
FRONTEND_PORT=80
```

### 4. 一键启动

```bash
docker-compose up -d
```

三个容器会按依赖顺序启动：MySQL → Backend → Frontend。

### 5. 访问应用

- 前端：http://localhost
- 后端 API：http://localhost:8080/api/v1

### 停止服务

```bash
docker-compose down
```

数据保留在 Docker volume 中，重新启动后数据不丢失。

## API 接口

| 模块 | 路径 | 说明 |
|------|------|------|
| 项目 | `GET/POST /api/v1/projects` | 项目列表 / 新建项目 |
| 项目 | `GET/PUT/DELETE /api/v1/projects/{id}` | 项目详情 / 编辑 / 删除 |
| 功能拆解 | `GET/POST /api/v1/projects/{id}/features` | WBS 树 / 添加功能 |
| 预算项 | `GET/POST /api/v1/features/{id}/budget-items` | 预算项列表 / 新增 |
| 人员 | `GET/POST /api/v1/personnel` | 人员列表 / 新增 |
| 薪资 | `GET/POST /api/v1/personnel/{id}/salaries` | 薪资历史 / 调薪 |
| 角色 | `GET/POST /api/v1/roles` | 角色列表 / 新增 |
| 团队 | `GET/POST /api/v1/projects/{id}/team` | 项目团队 / 分配成员 |
