<template>
  <div class="create-product">
    <div class="create-container">
      <h1>创建新产品</h1>
      
      <div class="create-form">
        <form @submit.prevent="createProduct">
          <div class="form-group">
            <label for="productName">产品名称 <span class="required">*</span></label>
            <input 
              id="productName"
              v-model="formData.name" 
              type="text" 
              placeholder="请输入产品名称"
              required
              :disabled="creating"
            />
          </div>

          <div class="form-group">
            <label for="productCategory">产品类别 <span class="required">*</span></label>
            <input 
              id="productCategory"
              v-model="formData.category" 
              type="text" 
              placeholder="请输入产品类别"
              required
              :disabled="creating"
            />
          </div>

          <div class="form-group">
            <label for="productDescription">产品描述</label>
            <textarea 
              id="productDescription"
              v-model="formData.description" 
              placeholder="请输入产品描述（可选）"
              rows="4"
              :disabled="creating"
            ></textarea>
          </div>

          <div class="form-actions">
            <button 
              type="button" 
              @click="resetForm" 
              class="reset-btn"
              :disabled="creating"
            >
              重置
            </button>
            <button 
              type="submit" 
              class="create-btn"
              :disabled="creating || !isFormValid"
            >
              {{ creating ? '创建中...' : '创建产品' }}
            </button>
          </div>
        </form>
      </div>

      <!-- 创建结果显示 -->
      <div v-if="createdProduct" class="result-section">
        <h2>✅ 产品创建成功！</h2>
        <div class="product-display">
          <div class="product-card">
            <div class="product-header">
              <h3>{{ createdProduct.name }}</h3>
              <span class="product-id">#{{ createdProduct.id }}</span>
            </div>
            <div class="product-info">
              <p><strong>类别:</strong> {{ createdProduct.category }}</p>
              <p><strong>描述:</strong> {{ createdProduct.description || '无描述' }}</p>
              <p><strong>创建时间:</strong> {{ formatDate(createdProduct.createdAt) }}</p>
            </div>
          </div>
        </div>
        
        <div class="result-actions">
          <button @click="createAnother" class="create-another-btn">
            创建另一个产品
          </button>
          <button @click="goToSearch" class="go-search-btn">
            前往搜索页面
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

// 路由
const router = useRouter()

// 响应式数据
const creating = ref(false)
const createdProduct = ref(null)

// 表单数据
const formData = ref({
  name: '',
  category: '',
  description: ''
})

// 计算属性：表单验证
const isFormValid = computed(() => {
  return formData.value.name.trim() && formData.value.category.trim()
})

// 创建产品方法
const createProduct = async () => {
  if (!isFormValid.value) {
    alert('请填写必填字段（产品名称和类别）')
    return
  }

  creating.value = true
  createdProduct.value = null

  try {
    const apiBase = 'http://172.26.235.205:8080/api/products'
    
    // 准备发送的数据
    const requestData = {
      name: formData.value.name.trim(),
      category: formData.value.category.trim(),
      description: formData.value.description.trim() || null
    }
    
    console.log('发送创建请求:', requestData)
    
    // 发送POST请求
    const response = await axios.post(`${apiBase}/productcreate`, requestData, {
      headers: {
        'Content-Type': 'application/json'  
      }
    })
    
    console.log('创建响应:', response.data)

    if (response.data.code === 200) {
      createdProduct.value = response.data.data
      
      // 显示成功消息
      alert('产品创建成功！')
      
      // 滚动到结果区域
      setTimeout(() => {
        const resultSection = document.querySelector('.result-section')
        if (resultSection) {
          resultSection.scrollIntoView({ behavior: 'smooth' })
        }
      }, 100)
      
    } else {
      alert(response.data.message || '创建失败')
    }

  } catch (error) {
    console.error('创建失败:', error)
    
    if (error.response?.status === 400) {
      alert('请求数据格式错误，请检查输入内容')
    } else if (error.response?.status === 500) {
      alert('服务器错误，创建失败')
    } else if (error.response?.data?.message) {
      alert('创建失败：' + error.response.data.message)
    } else {
      alert('网络错误，创建失败')
    }
  } finally {
    creating.value = false
  }
}

// 重置表单
const resetForm = () => {
  formData.value = {
    name: '',
    category: '',
    description: ''
  }
  createdProduct.value = null
}

// 创建另一个产品
const createAnother = () => {
  resetForm()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 跳转到搜索页面
const goToSearch = () => {
  router.push('/product-management')
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '未知'
  
  try {
    const date = new Date(dateString)
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (e) {
    return dateString
  }
}
</script>

<style scoped>
.create-product {
  max-width: 800px;
  margin: 0 auto;
  padding: 30px 20px;
  font-family: Arial, sans-serif;
}

.create-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  padding: 40px;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 40px;
  font-size: 2rem;
  font-weight: 600;
}

.create-form {
  background: #f8f9fa;
  padding: 30px;
  border-radius: 8px;
  margin-bottom: 30px;
  border: 2px solid #e9ecef;
}

.form-group {
  margin-bottom: 25px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
  font-size: 1.1rem;
}

.required {
  color: #dc3545;
  font-weight: bold;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
  box-sizing: border-box;
  font-family: inherit;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0,123,255,0.1);
}

.form-group input:disabled,
.form-group textarea:disabled {
  background-color: #f8f9fa;
  color: #6c757d;
  cursor: not-allowed;
}

textarea {
  resize: vertical;
  min-height: 100px;
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: flex-end;
  margin-top: 30px;
}

.reset-btn {
  background: #6c757d;
  color: white;
  padding: 12px 24px;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.reset-btn:hover:not(:disabled) {
  background: #5a6268;
  transform: translateY(-1px);
}

.create-btn {
  background: linear-gradient(135deg, #28a745, #20a83a);
  color: white;
  padding: 12px 24px;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.create-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #20a83a, #1e7e34);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(40,167,69,0.3);
}

.create-btn:disabled,
.reset-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* 结果展示区域 */
.result-section {
  margin-top: 40px;
  padding-top: 30px;
  border-top: 2px solid #e9ecef;
}

.result-section h2 {
  text-align: center;
  color: #28a745;
  margin-bottom: 30px;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.product-display {
  margin-bottom: 30px;
}

.product-card {
  background: #f8f9fa;
  border: 2px solid #28a745;
  border-radius: 8px;
  padding: 25px;
  transition: all 0.3s ease;
}

.product-card:hover {
  box-shadow: 0 6px 16px rgba(40,167,69,0.2);
  transform: translateY(-2px);
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.product-header h3 {
  margin: 0;
  color: #333;
  font-size: 1.3rem;
}

.product-id {
  background: #28a745;
  color: white;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: bold;
}

.product-info p {
  margin: 12px 0;
  color: #555;
  line-height: 1.5;
  font-size: 1rem;
}

.product-info strong {
  color: #333;
}

.result-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  flex-wrap: wrap;
}

.create-another-btn,
.go-search-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.create-another-btn {
  background: linear-gradient(135deg, #007bff, #0056b3);
  color: white;
}

.create-another-btn:hover {
  background: linear-gradient(135deg, #0056b3, #004085);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,123,255,0.3);
}

.go-search-btn {
  background: linear-gradient(135deg, #6f42c1, #563d7c);
  color: white;
}

.go-search-btn:hover {
  background: linear-gradient(135deg, #563d7c, #452a5c);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(111,66,193,0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .create-product {
    padding: 20px 15px;
  }
  
  .create-container {
    padding: 30px 20px;
  }
  
  h1 {
    font-size: 1.6rem;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .result-actions {
    flex-direction: column;
  }
  
  .product-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}

/* 动画效果 */
.result-section {
  animation: slideUp 0.5s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>