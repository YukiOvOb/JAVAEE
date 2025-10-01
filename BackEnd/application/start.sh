#!/bin/bash
# filepath: /root/JAVAEE/BackEnd/application/start.sh

echo "🚀 Starting JAVAEE Application..."

# Load environment variables | 加载环境变量
source ./load-env.sh

# Start MySQL service | 启动MySQL服务
echo "🔍 Checking MySQL service..."
if ! systemctl is-active --quiet mysql; then
    echo "📦 Starting MySQL service..."
    sudo systemctl start mysql
fi

# Start the application | 启动应用
echo "🌟 Starting Spring Boot application with environment variables..."
./mvnw spring-boot:run