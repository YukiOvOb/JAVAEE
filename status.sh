#!/bin/bash
# filepath: /root/JAVAEE/status.sh

echo "📊 Git Repository Status Report | Git仓库状态报告"

# Set color output | 设置颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

echo "=========================================="

# Basic information | 基本信息
echo -e "${BLUE}📁 Repository Information | 仓库信息：${NC}"
echo "   Current directory | 当前目录: $(pwd)"
echo "   Current branch | 当前分支: $(git branch --show-current)"
echo "   Remote repository | 远程仓库: $(git remote get-url origin 2>/dev/null || echo 'Not set | 未设置')"

echo ""

# Branch information | 分支信息
echo -e "${BLUE}🌿 Branch Information | 分支信息：${NC}"
git branch -a

echo ""

# Working directory status | 工作区状态
echo -e "${BLUE}💼 Working Directory Status | 工作区状态：${NC}"
if [ -z "$(git status --porcelain)" ]; then
    echo -e "${GREEN}✅ Working directory clean | 工作区干净${NC}"
else
    git status --short
fi

echo ""

# Recent commits | 最近提交
echo -e "${BLUE}📝 Recent 5 Commits | 最近5次提交：${NC}"
git log --oneline -5

echo ""

# Unpushed commits | 未推送的提交
echo -e "${BLUE}⬆️  Unpushed Commits | 未推送的提交：${NC}"
unpushed=$(git log --oneline origin/$(git branch --show-current)..HEAD 2>/dev/null)
if [ -z "$unpushed" ]; then
    echo -e "${GREEN}✅ No unpushed commits | 没有未推送的提交${NC}"
else
    echo "$unpushed"
fi

echo "=========================================="