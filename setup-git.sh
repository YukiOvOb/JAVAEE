#!/bin/bash
# filepath: /root/JAVAEE/setup-git.sh

echo "🚀 Starting Git Repository Initialization... | 开始Git仓库初始化..."

# Set color output | 设置颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check if already a Git repository | 检查是否已经是Git仓库
if [ -d ".git" ]; then
    echo -e "${YELLOW}⚠️  Directory is already a Git repository | 当前目录已经是Git仓库${NC}"
else
    echo "📁 Initializing Git repository... | 初始化Git仓库..."
    git init
fi

# Set Git user information | 设置Git用户信息
echo "👤 Setting up Git user information... | 设置Git用户信息..."
read -p "Enter your name | 请输入你的姓名: " user_name
read -p "Enter your email | 请输入你的邮箱: " user_email

git config --global user.name "$user_name"
git config --global user.email "$user_email"

echo -e "${GREEN}✅ User information setup completed | 用户信息设置完成${NC}"
echo "   Name | 姓名: $user_name"
echo "   Email | 邮箱: $user_email"

# Set line ending handling | 设置换行符处理
git config --global core.autocrlf input
echo -e "${GREEN}✅ Line ending configuration completed | 换行符配置完成${NC}"

# Check remote repository | 检查远程仓库
if git remote | grep -q "origin"; then
    echo -e "${YELLOW}⚠️  Remote repository already exists | 远程仓库已存在${NC}"
    git remote -v
else
    echo "🌐 Adding remote repository... | 添加远程仓库..."
    git remote add origin "https://github.com/YukiOvOb/JAVAEE.git"
    echo -e "${GREEN}✅ Remote repository added successfully | 远程仓库添加完成${NC}"
fi

# Create .gitignore file | 创建.gitignore文件
if [ ! -f ".gitignore" ]; then
    echo "📝 Creating .gitignore file... | 创建.gitignore文件..."
    cat > .gitignore << EOF
# IDE
.vscode/
.idea/
*.swp
*.swo

# OS
.DS_Store
Thumbs.db

# Logs
*.log
logs/

# Runtime data
pids/
*.pid
*.seed

# Node.js
node_modules/
npm-debug.log*

# Java
*.class
target/
*.jar
*.war

# Spring Boot
application-*.properties
application-*.yml

# Database
*.db
*.sqlite3

# Temporary files
tmp/
temp/
EOF
    echo -e "${GREEN}✅ .gitignore file created successfully | .gitignore文件创建完成${NC}"
fi

echo -e "${GREEN}🎉 Git repository initialization completed! | Git仓库初始化完成！${NC}"
echo "Now you can use the following scripts for daily operations | 现在你可以使用以下脚本进行日常操作："
echo "  - ./commit-push.sh : Commit and push code | 提交并推送代码"
echo "  - ./pull.sh : Pull latest code | 拉取最新代码"
echo "  - ./status.sh : Check repository status | 查看仓库状态"
echo ""
echo -e "${YELLOW}Press SPACE to exit... | 按空格键退出...${NC}"
while true; do
    read -rsn1 key
    if [ "$key" = " " ]; then
        break
    fi
done