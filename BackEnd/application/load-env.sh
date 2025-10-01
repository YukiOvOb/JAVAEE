#!/bin/bash
# filepath: /root/JAVAEE/BackEnd/application/load-env.sh

echo "🔧 Loading environment variables from .env file..."

if [ -f .env ]; then
    # Export all variables from .env file | 从.env文件导出所有变量
    export $(cat .env | grep -v '^#' | grep -v '^$' | xargs)
    echo "✅ Environment variables loaded successfully"
    
    # Display loaded variables (hide sensitive data) | 显示已加载的变量（隐藏敏感数据）
    echo "📋 Loaded variables:"
    echo "   DB_HOST: $DB_HOST"
    echo "   DB_PORT: $DB_PORT"
    echo "   DB_NAME: $DB_NAME"
    echo "   DB_USERNAME: $DB_USERNAME"
    echo "   DB_PASSWORD: [HIDDEN]"
    echo "   MAIL_HOST: $MAIL_HOST"
    echo "   MAIL_PORT: $MAIL_PORT"
    echo "   MAIL_USERNAME: $MAIL_USERNAME"
    echo "   MAIL_PASSWORD: [HIDDEN]"
    echo "   APP_NAME: $APP_NAME"
    echo "   SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"
else
    echo "❌ .env file not found!"
    echo "💡 Please copy .env.example to .env and configure your settings"
    exit 1
fi