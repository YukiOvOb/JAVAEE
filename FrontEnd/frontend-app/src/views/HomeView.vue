<!-- filepath: /root/JAVAEE/FrontEnd/frontend-app/src/views/HomeView.vue -->
<template>
  <div class="home">
    <h1>欢迎来到首页</h1>
    <div class="welcome-content">
      <HelloWorld msg="You did it!" />
      <TheWelcome />
    </div>
  </div>
</template>

<script setup>
// import HelloWorld from '@/components/HelloWorld.vue'
// import TheWelcome from '@/components/TheWelcome.vue'
</script>

<style scoped>
.home {
  padding: 2rem;
}

.welcome-content {
  margin-top: 2rem;
}

h1 {
  color: #2c3e50;
  text-align: center;
  margin-bottom: 2rem;
}
</style>