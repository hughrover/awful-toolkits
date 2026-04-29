#!/bin/bash

# 确保脚本在出错时停止
set -e

echo "🚀 开始重新构建和重启工程..."

# 1. 构建后端
echo "📦 正在构建后端 (Maven)..."
cd toolkits
mvn clean package -DskipTests
cd ..

# 2. 构建前端
echo "📦 正在构建前端 (npm)..."
cd toolkits-frontend
# 如果 node_modules 不存在则安装依赖
if [ ! -d "node_modules" ]; then
    npm install
fi
npm run build-only
cd ..

# 3. 重启 Docker 容器
echo "🐳 正在重启 Docker 镜像..."
docker-compose down

# 重新构建并启动
docker-compose up --build -d

echo "✅ 重构与重启完成！"
echo "🌐 前端访问地址: http://localhost"
echo "📊 后端 API 地址: http://localhost:8080/api/v1"

# 显示容器状态
docker-compose ps
