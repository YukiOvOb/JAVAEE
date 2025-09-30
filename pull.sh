#!/bin/bash
# filepath: /root/JAVAEE/pull.sh

echo "⬇️  Starting to pull remote code... | 开始拉取远程代码..."

# Set color output | 设置颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Check for uncommitted local changes | 检查本地是否有未提交的更改
if [ -n "$(git status --porcelain)" ]; then
    echo -e "${YELLOW}⚠️  Uncommitted changes detected | 检测到未提交的更改：${NC}"
    git status --short
    read -p "Do you want to stash these changes first? (y/n) | 是否先暂存这些更改？(y/n): " stash_confirm
    
    if [ "$stash_confirm" = "y" ] || [ "$stash_confirm" = "Y" ]; then
        git stash push -m "Auto stash before pull $(date)"
        echo -e "${GREEN}✅ Changes stashed | 更改已暂存${NC}"
        STASHED=true
    fi
fi

# Pull remote changes | 拉取远程更改
echo "📥 Pulling remote changes... | 拉取远程更改..."
current_branch=$(git branch --show-current)
git pull origin "$current_branch"

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Pull successful | 拉取成功${NC}"
    
    # If there are stashed changes, ask to restore | 如果有暂存的更改，询问是否恢复
    if [ "$STASHED" = "true" ]; then
        read -p "Do you want to restore stashed changes? (y/n) | 是否恢复暂存的更改？(y/n): " restore_confirm
        if [ "$restore_confirm" = "y" ] || [ "$restore_confirm" = "Y" ]; then
            git stash pop
            echo -e "${GREEN}✅ Stashed changes restored | 暂存的更改已恢复${NC}"
        fi
    fi
    
    # Show latest commits | 显示最新的提交
    echo -e "${GREEN}📋 Latest commits | 最新提交：${NC}"
    git log --oneline -5
    echo ""
    echo -e "${YELLOW}Press SPACE to exit... | 按空格键退出...${NC}"
    while true; do
        read -rsn1 key
        if [ "$key" = " " ]; then
            break
        fi
    done
else
    echo -e "${RED}❌ Pull failed | 拉取失败${NC}"
    echo -e "${YELLOW}Press SPACE to exit... | 按空格键退出...${NC}"
    while true; do
        read -rsn1 key
        if [ "$key" = " " ]; then
            break
        fi
    done
    exit 1
fi