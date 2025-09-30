#!/bin/bash
# filepath: /root/JAVAEE/setup-all.sh

echo "🔧 Creating Git management scripts... | 创建Git管理脚本..."

# Add execution permissions to all scripts | 给所有脚本添加执行权限
chmod +x setup-git.sh
chmod +x commit-push.sh
chmod +x pull.sh
chmod +x status.sh

echo "✅ All scripts created and execution permissions set | 所有脚本已创建并设置执行权限"
echo ""
echo "📖 Usage Instructions | 使用说明："
echo "  ./setup-git.sh     - Initialize Git repository (first run) | 初始化Git仓库（首次运行）"
echo "  ./commit-push.sh   - Commit and push code | 提交并推送代码"
echo "  ./pull.sh          - Pull remote code | 拉取远程代码" 
echo "  ./status.sh        - Check repository status | 查看仓库状态"
echo ""
echo "💡 Quick Start | 快速开始："
echo "  1. ./setup-git.sh     # First-time initialization | 首次初始化"
echo "  2. ./commit-push.sh   # Commit and push code | 提交推送代码"
echo "  3. ./status.sh        # Check status | 查看状态"
echo ""
echo "🌟 Example Usage | 使用示例："
echo "  ./commit-push.sh \"Add new features\"   # With commit message | 带提交信息"
echo "  ./commit-push.sh                       # Interactive mode | 交互模式"
echo ""
echo -e "\033[1;33mPress SPACE or ENTER to exit... | 按空格键或回车键退出...\033[0m"
while true; do
    read -rsn1 key
    if [ "$key" = " " ] || [ "$key" = "" ]; then
        break
    fi
done