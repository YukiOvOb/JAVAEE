import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/HomeView.vue')
  },
  {
    path: '/test-management',
    name: 'TestManagement',
    component: () => import('../views/TestManagement.vue')
  },
  {
    path: '/product-management',
    name: 'ProductManagement',
    component: () => import('../views/ProductManagement.vue')
  },
  {
    path: '/create-product',
    name: 'CreateProduct',
    component: () => import('../views/CreateProduct.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router