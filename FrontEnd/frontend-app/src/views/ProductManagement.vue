<template>
  <div class="product-search">
    <div class="search-container">
      <h1>Search Products Provided By All Organizations</h1>
      
      <div class="search-form">
        <div class="form-group">
          <label for="searchBy">Search By:</label>
          <select id="searchBy" v-model="searchBy" @change="updateSearchCriteria">
            <option value="name">Name</option>
            <option value="category">Category</option>
            <option value="id">ID</option>
          </select>
        </div>

        <div class="form-group">
          <label for="keyword">Enter keyword:</label>
          <input 
            id="keyword" 
            v-model="keyword" 
            type="text" 
            :placeholder="getPlaceholder()"
            @keyup.enter="searchProducts"
          />
        </div>

        <button @click="searchProducts" class="search-btn" :disabled="!keyword.trim()">
          Search
        </button>
      </div>

      <!-- 显示当前搜索条件 -->
      <div v-if="hasSearched" class="search-criteria">
        <h3>Display the different search criteria</h3>
        <div class="criteria-display">
          <strong>Search By:</strong> {{ searchBy.charAt(0).toUpperCase() + searchBy.slice(1) }}
          <br>
          <strong>Keyword:</strong> {{ lastSearchKeyword }}
        </div>
      </div>

      <!-- 搜索结果 -->
      <div v-if="hasSearched" class="search-results">
        <h3>搜索结果 ({{ products.length }} 条)</h3>
        
        <div v-if="loading" class="loading">
          搜索中...
        </div>

        <div v-else-if="products.length === 0" class="no-results">
          <p>未找到相关产品</p>
        </div>

        <div v-else class="products-grid">
          <div v-for="product in products" :key="product.id" class="product-card">
            <div class="product-header">
              <h4>{{ product.name || '无名称' }}</h4>
              <span class="product-id">#{{ product.id }}</span>
            </div>
            <div class="product-info">
              <p><strong>类别:</strong> {{ product.category || '无类别' }}</p>
              <p><strong>描述:</strong> {{ product.description || '无描述' }}</p>
            </div>
            <!-- 操作按钮：修改和删除 -->
            <div class="product-actions">
              <button 
                @click="openEditModal(product)" 
                class="edit-btn"
                :disabled="updating"
              >
                {{ updating ? '修改中...' : '修改' }}
              </button>
              <button 
                @click="deleteProduct(product.id)" 
                class="delete-btn"
                :disabled="deleting"
              >
                {{ deleting ? '删除中...' : '删除' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改产品弹窗 -->
    <div v-if="showEditModal" class="modal-overlay" @click="closeEditModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>修改产品信息</h3>
          <button @click="closeEditModal" class="close-btn">&times;</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>产品ID:</label>
            <input type="text" :value="editingProduct.id" disabled class="disabled-input" />
          </div>
          <div class="form-group">
            <label>产品名称:</label>
            <input 
              type="text" 
              v-model="editingProduct.name" 
              placeholder="请输入产品名称"
              class="modal-input"
            />
          </div>
          <div class="form-group">
            <label>产品类别:</label>
            <input 
              type="text" 
              v-model="editingProduct.category" 
              placeholder="请输入产品类别"
              class="modal-input"
            />
          </div>
        </div>
        <div class="modal-footer">
          <button @click="closeEditModal" class="cancel-btn">取消</button>
          <button @click="updateProduct" class="confirm-btn" :disabled="updating">
            {{ updating ? '保存中...' : '确认修改' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'

// 全局配置axios
axios.defaults.headers.post['Content-Type'] = 'application/json'
axios.defaults.headers.common['Accept'] = 'application/json'

// 响应式数据
const searchBy = ref('name')
const keyword = ref('')
const products = ref([])
const loading = ref(false)
const deleting = ref(false)
const updating = ref(false) // 新增：修改状态
const hasSearched = ref(false)
const lastSearchKeyword = ref('')

// 修改弹窗相关数据
const showEditModal = ref(false)
const editingProduct = ref({
  id: null,
  name: '',
  category: ''
})

// 计算属性
const getPlaceholder = () => {
  switch (searchBy.value) {
    case 'name':
      return '请输入产品名称'
    case 'category':
      return '请输入产品类别'
    case 'id':
      return '请输入产品ID'
    default:
      return '请输入关键词'
  }
}

// 打开修改弹窗
const openEditModal = (product) => {
  editingProduct.value = {
    id: product.id,
    name: product.name,
    category: product.category
  }
  showEditModal.value = true
}

// 关闭修改弹窗
const closeEditModal = () => {
  showEditModal.value = false
  editingProduct.value = {
    id: null,
    name: '',
    category: ''
  }
}

// 修改产品
const updateProduct = async () => {
  if (!editingProduct.value.name.trim()) {
    alert('请输入产品名称')
    return
  }
  
  if (!editingProduct.value.category.trim()) {
    alert('请输入产品类别')
    return
  }

  updating.value = true

  try {
    const apiBase = 'http://172.26.235.205:8080/api/products'
    console.log('修改产品:', editingProduct.value)
    
    // 发送修改请求
    const response = await axios.post(`${apiBase}/productchange`, {
      id: editingProduct.value.id,
      name: editingProduct.value.name.trim(),
      category: editingProduct.value.category.trim()
    })
    
    console.log('修改响应:', response.data)

    if (response.data.code === 200) {
      alert('产品修改成功')
      
      // 更新本地产品列表中的数据
      const index = products.value.findIndex(p => p.id === editingProduct.value.id)
      if (index !== -1) {
        products.value[index].name = editingProduct.value.name.trim()
        products.value[index].category = editingProduct.value.category.trim()
      }
      
      closeEditModal()
      
      
    } else {
      alert(response.data.message || '修改失败')
    }

  } catch (error) {
    console.error('修改失败:', error)
    
    if (error.response?.status === 404) {
      alert('产品不存在，无法修改')
    } else if (error.response?.status === 500) {
      alert('服务器错误，修改失败')
    } else {
      alert('网络错误，修改失败')
    }
  } finally {
    updating.value = false
  }
}

// 删除产品方法
const deleteProduct = async (productId) => {
  if (!confirm(`确定要删除产品ID为 ${productId} 的产品吗？`)) {
    return
  }

  deleting.value = true

  try {
    const apiBase = 'http://172.26.235.205:8080/api/products'
    console.log('删除产品ID:', productId)
    
    // 发送删除请求
    const response = await axios.post(`${apiBase}/productdeletebyid`, {
      id: productId
    })
    
    console.log('删除响应:', response.data)

    if (response.data.code === 200) {
      alert('产品删除成功')
      
      // 从当前显示的产品列表中移除已删除的产品
      products.value = products.value.filter(product => product.id !== productId)
      
    } else {
      alert(response.data.message || '删除失败')
    }

  } catch (error) {
    console.error('删除失败:', error)
    
    if (error.response?.status === 404) {
      alert('产品不存在或已被删除')
      // 从列表中移除（可能已经被其他方式删除了）
      products.value = products.value.filter(product => product.id !== productId)
    } else if (error.response?.status === 500) {
      alert('服务器错误，删除失败')
    } else {
      alert('网络错误，删除失败')
    }
  } finally {
    deleting.value = false
  }
}

// 搜索方法
const searchProducts = async () => {
  if (!keyword.value.trim()) {
    alert('请输入搜索关键词')
    return
  }

  loading.value = true
  lastSearchKeyword.value = keyword.value

  try {
    let response
    const apiBase = 'http://172.26.235.205:8080/api/products'
    
    switch (searchBy.value) {
      case 'name':
        console.log('发送name请求:', keyword.value)
        response = await axios.post(`${apiBase}/productsfindbyname`, {
          name: keyword.value
        })
        console.log(response.data)
        break
      case 'category':
        console.log('发送category请求:', keyword.value)
        response = await axios.post(`${apiBase}/productsfindbycategory`, {
          category: keyword.value
        })
        console.log(response.data)
        break
      case 'id':
        const id = parseInt(keyword.value)
        if (isNaN(id)) {
          alert('ID必须是数字')
          loading.value = false
          return
        }
        console.log('发送id请求:', id)
        response = await axios.post(`${apiBase}/productsfindbyid`, {
          id: id
        })
        products.value = response.data.code === 200 && response.data.data ? [response.data.data] : []
        hasSearched.value = true
        loading.value = false
        return
      default:
        throw new Error('未知的搜索类型')
    }

    // 处理响应
    if (response.data.code === 200) {
      console.log('搜索结果:', response.data.data)
      products.value = response.data.data || []
    } else {
      products.value = []
      alert(response.data.message || '搜索失败')
    }

  } catch (error) {
    console.error('搜索失败:', error)
    products.value = []
    
    if (error.message && error.message.includes('CORS')) {
      alert('跨域请求被阻止')
    } else if (error.response?.status === 404) {
      alert('未找到相关产品')
    } else if (error.response?.status === 500) {
      alert('服务器错误，请稍后重试')
    } else {
      alert('搜索失败，请检查网络连接')
    }
  } finally {
    hasSearched.value = true
    loading.value = false
  }
}

// 更新搜索条件时清空结果
const updateSearchCriteria = () => {
  keyword.value = ''
  hasSearched.value = false
  products.value = []
}
</script>

<style scoped>
.product-search {
  max-width: 1000px;
  margin: 0 auto;
  padding: 30px 20px;
  font-family: Arial, sans-serif;
}

.search-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  padding: 30px;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 1.8rem;
}

.search-form {
  background: #f8f9fa;
  padding: 25px;
  border-radius: 8px;
  margin-bottom: 25px;
  border: 2px solid #e9ecef;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
  font-size: 1.1rem;
}

.form-group select,
.form-group input {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
  box-sizing: border-box;
}

.form-group select:focus,
.form-group input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0,123,255,0.1);
}

.search-btn {
  background: linear-gradient(135deg, #007bff, #0056b3);
  color: white;
  padding: 12px 30px;
  border: none;
  border-radius: 6px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 100%;
}

.search-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #0056b3, #004085);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}

.search-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.search-criteria {
  background: #e7f3ff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 25px;
  border-left: 4px solid #007bff;
}

.search-criteria h3 {
  margin-top: 0;
  color: #0056b3;
  font-size: 1.2rem;
}

.criteria-display {
  font-size: 1rem;
  line-height: 1.6;
  color: #333;
}

.search-results h3 {
  color: #333;
  margin-bottom: 20px;
  font-size: 1.3rem;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 1.1rem;
  color: #666;
}

.no-results {
  text-align: center;
  padding: 40px;
  color: #666;
  background: #f8f9fa;
  border-radius: 8px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.product-card {
  background: white;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s ease;
}

.product-card:hover {
  border-color: #007bff;
  box-shadow: 0 4px 12px rgba(0,123,255,0.1);
  transform: translateY(-2px);
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.product-header h4 {
  margin: 0;
  color: #333;
  font-size: 1.1rem;
}

.product-id {
  background: #007bff;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: bold;
}

.product-info p {
  margin: 8px 0;
  color: #555;
  line-height: 1.4;
}

.product-info strong {
  color: #333;
}

/* 产品操作按钮样式 */
.product-actions {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #e9ecef;
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.edit-btn {
  background: linear-gradient(135deg, #28a745, #20a83a);
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.edit-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #20a83a, #1e7e34);
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(40,167,69,0.3);
}

.delete-btn {
  background: linear-gradient(135deg, #dc3545, #c82333);
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.delete-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #c82333, #a02622);
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(220,53,69,0.3);
}

.edit-btn:disabled,
.delete-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0,0,0,0.3);
}

.modal-header {
  padding: 20px;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  color: #333;
  font-size: 1.2rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #666;
  cursor: pointer;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  color: #333;
}

.modal-body {
  padding: 20px;
}

.modal-input {
  width: 100%;
  padding: 10px 12px;
  border: 2px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
  box-sizing: border-box;
}

.modal-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0,123,255,0.1);
}

.disabled-input {
  background-color: #f8f9fa;
  color: #6c757d;
  cursor: not-allowed;
}

.modal-footer {
  padding: 20px;
  border-top: 1px solid #e9ecef;
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.cancel-btn {
  background: #6c757d;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.cancel-btn:hover {
  background: #5a6268;
}

.confirm-btn {
  background: linear-gradient(135deg, #007bff, #0056b3);
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.confirm-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #0056b3, #004085);
}

.confirm-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .product-search {
    padding: 20px 15px;
  }
  
  .search-container {
    padding: 20px;
  }
  
  h1 {
    font-size: 1.4rem;
  }
  
  .products-grid {
    grid-template-columns: 1fr;
  }

  .product-actions {
    flex-direction: column;
  }

  .modal-content {
    width: 95%;
    margin: 20px;
  }
}
</style>