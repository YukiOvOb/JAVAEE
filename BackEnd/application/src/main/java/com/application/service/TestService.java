package com.application.service;

import com.application.dao.TestRepository;
import com.application.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public Test createTest(Test test) {
        // 业务逻辑：检查测试项是否已存在
        if (testRepository.existsByName(test.getName())) {
            throw new RuntimeException("测试项已存在");
        }
        
        // 直接保存 Test 实体
        Test savedTest = testRepository.save(test);
        
        return savedTest;
    }
    
    // 获取所有测试项
    public List<Test> getAllTests() {
        return testRepository.findAll();
    }
    
    // 根据ID获取测试项
    public Test getTestById(Long id) {
        return testRepository.findById(id).orElse(null);
    }
    
    // 更新测试项
    public Test updateTest(Long id, Test test) {
        Test existingTest = testRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("测试项不存在"));
        
        existingTest.setName(test.getName());
        existingTest.setValue(test.getValue());
        
        return testRepository.save(existingTest);
    }
    
    // 删除测试项
    public void deleteTest(Long id) {
        testRepository.deleteById(id);
    }
}