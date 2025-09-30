import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import ProductManagement from './pages/ProductManagement';
import CreateProduct from './pages/CreateProduct';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        {/* 导航栏 */}
        <nav className="navbar">
          <div className="nav-container">
            <Link to="/" className="nav-logo">
              Product Management System
            </Link>
            <ul className="nav-menu">
              <li>
                <Link to="/products" className="nav-link">
                  产品搜索
                </Link>
              </li>
              <li>
                <Link to="/create" className="nav-link">
                  创建产品
                </Link>
              </li>
            </ul>
          </div>
        </nav>

        {/* 主要内容区域 */}
        <main className="main-content">
          <Routes>
            <Route path="/" element={<ProductManagement />} />
            <Route path="/products" element={<ProductManagement />} />
            <Route path="/create" element={<CreateProduct />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;