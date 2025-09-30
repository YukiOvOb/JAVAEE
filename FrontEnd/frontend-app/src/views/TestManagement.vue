<!-- filepath: /root/JAVAEE/FrontEnd/frontend-app/src/views/TestManagement.vue -->
<template>
  <div class="test-management">
    <h1>测试数据管理</h1>
    
    <!-- 添加表单 -->
    <div class="add-form">
      <h2>添加新数据</h2>
      <form @submit.prevent="addTest">
        <div class="form-group">
          <label>名称:</label>
          <input v-model="newTest.name" type="text" placeholder="请输入名称" required />
        </div>
        <div class="form-group">
          <label>值:</label>
          <input v-model="newTest.value" type="text" placeholder="请输入值" />
        </div>
        <button type="submit">保存</button>
      </form>
    </div>

    <!-- 显示列表 -->
    <div class="test-list">
      <h2>已保存的数据</h2>
      <div v-if="tests.length === 0" class="empty">
        暂无数据
      </div>
      <div v-else>
        <div v-for="test in tests" :key="test.id" class="test-item">
          <span><strong>{{ test.name }}:</strong> {{ test.value || '无值' }}</span>
          <button @click="deleteTest(test.id)" class="delete-btn">删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const tests = ref([])
const newTest = ref({ name: '', value: '' })

// 加载数据
const loadTests = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/tests')
    tests.value = response.data
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

// 添加测试数据
const addTest = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/tests', newTest.value)
    tests.value.push(response.data)
    newTest.value = { name: '', value: '' }
    alert('添加成功!')
  } catch (error) {
    console.error('添加失败:', error)
    alert('添加失败!')
  }
}

// 删除测试数据
const deleteTest = async (id) => {
  if (!confirm('确定要删除吗？')) return
  
  try {
    await axios.delete(`http://localhost:8080/api/tests/${id}`)
    tests.value = tests.value.filter(test => test.id !== id)
    alert('删除成功!')
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败!')
  }
}

onMounted(() => {
  loadTests()
})
</script>

<style scoped>
.test-management {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.add-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  background: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background: #0056b3;
}

.test-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border: 1px solid #eee;
  margin-bottom: 10px;
  border-radius: 4px;
}

.delete-btn {
  background: #dc3545;
  padding: 5px 10px;
  font-size: 12px;
}

.delete-btn:hover {
  background: #c82333;
}

.empty {
  text-align: center;
  padding: 40px;
  color: #666;
}
</style>