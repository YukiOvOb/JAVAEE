#!/bin/bash
# filepath: /root/JAVAEE/commit-push.sh

echo "📝 Starting code commit and push... | 开始提交和推送代码..."

# Set color output | 设置颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Show current status | 显示当前状态
echo -e "${BLUE}📊 Current repository status | 当前仓库状态：${NC}"
git status --short

# Check if there are changes | 检查是否有更改
if [ -z "$(git status --porcelain)" ]; then
    echo -e "${YELLOW}⚠️  No changes to commit | 没有需要提交的更改${NC}"
    exit 0
fi

# Show detailed changes | 显示详细更改
echo -e "${BLUE}📋 Detailed changes | 详细更改：${NC}"
git status

# Confirm to continue | 确认是否继续
read -p "Do you want to commit these changes? (y/n) | 是否继续提交这些更改？(y/n): " confirm
if [ "$confirm" != "y" ] && [ "$confirm" != "Y" ]; then
    echo "❌ Operation cancelled | 操作已取消"
    exit 0
fi

# Add files | 添加文件
echo "➕ Adding files to staging area... | 添加文件到暂存区..."
git add .

# Get commit message | 获取提交信息
if [ -z "$1" ]; then
    echo "💬 Please enter commit message | 请输入提交信息："
    read -p "Commit message | 提交信息: " commit_message
    if [ -z "$commit_message" ]; then
        # Generate default commit message | 生成默认提交信息
        current_date=$(date "+%Y-%m-%d %H:%M")
        commit_message="Update: $current_date"
        echo -e "${YELLOW}Using default commit message | 使用默认提交信息: $commit_message${NC}"
    fi
else
    commit_message="$1"
fi

# Commit | 提交
echo "💾 Committing changes... | 提交更改..."
git commit -m "$commit_message"

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Commit successful | 提交成功${NC}"
    
    # Push to remote repository | 推送到远程仓库
    echo "🚀 Pushing to remote repository... | 推送到远程仓库..."
    
    # Check current branch | 检查当前分支
    current_branch=$(git branch --show-current)
    
    # Check if upstream branch is set | 检查是否设置了上游分支
    if git rev-parse --abbrev-ref "$current_branch@{upstream}" >/dev/null 2>&1; then
        git push
    else
        echo -e "${YELLOW}⚠️  First push, setting upstream branch... | 首次推送，设置上游分支...${NC}"
        git push -u origin "$current_branch"
    fi
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}🎉 Push successful! | 推送成功！${NC}"
        echo "🌐 View repository | 查看仓库: $(git remote get-url origin)"
        echo ""
        echo -e "${YELLOW}Press SPACE to exit... | 按空格键退出...${NC}"
        while true; do
            read -rsn1 key
            if [ "$key" = " " ]; then
                break
            fi
        done
    else
        echo -e "${RED}❌ Push failed | 推送失败${NC}"
        echo -e "${YELLOW}Press SPACE or ENTER to exit... | 按空格键或回车键退出...${NC}"
        while true; do
            read -rsn1 key
            if [ "$key" = " " ] || [ "$key" = "" ]; then
                break
            fi
        done
        exit 1
    fi
else
    echo -e "${RED}❌ Commit failed | 提交失败${NC}"
    echo -e "${YELLOW}Press SPACE or ENTER to exit... | 按空格键或回车键退出...${NC}"
    while true; do
        read -rsn1 key
        if [ "$key" = " " ] || [ "$key" = "" ]; then
            break
        fi
    done
    exit 1
fi