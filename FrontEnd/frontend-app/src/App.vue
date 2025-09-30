<!-- filepath: /root/JAVAEE/FrontEnd/frontend-app/src/App.vue -->
<template>
  <div id="app">
    <!-- 简单的导航菜单 -->
    <nav class="nav-menu">
      <router-link to="/">首页</router-link>
      <router-link to="/product-management">数据管理</router-link>
      <router-link to="/create-product">创建</router-link>
    </nav>

    <!-- 这里显示不同的页面内容 -->
    <router-view />
  </div>
</template>

<style>
#app {
  font-family: Arial, sans-serif;
}

.nav-menu {
  background: #f0f0f0;
  padding: 20px;
  display: flex;
  gap: 20px;
}

.nav-menu a {
  text-decoration: none;
  color: #333;
  padding: 10px 20px;
  border-radius: 4px;
}

.nav-menu a:hover,
.nav-menu a.router-link-active {
  background: #007bff;
  color: white;
}
</style>