# 检查当前Node.js版本
node --version

# 使用nvm升级到最新LTS版本
nvm install --lts
nvm use --lts
nvm alias default node

# 验证升级
node --version  # 应该显示 18.x 或 20.x
npm --version

# 安装依赖（如果还没安装）
npm install

# 启动开发服务器
npm run dev