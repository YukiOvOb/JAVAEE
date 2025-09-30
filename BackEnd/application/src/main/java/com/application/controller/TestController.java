package com.application.controller;

import com.application.service.TestService;
import com.application.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@CrossOrigin
public class TestController {
    @Autowired
    private TestService testService;
    
    // 获取所有测试项
    @GetMapping
    public List<Test> getAllTests() {
        return testService.getAllTests();
    }
    
    // 根据ID获取测试项
    @GetMapping("/{id}")
    public ResponseEntity<Test> getTestById(@PathVariable Long id) {
        Test test = testService.getTestById(id);
        return test != null ? ResponseEntity.ok(test) : ResponseEntity.notFound().build();
    }
    
    // 创建测试项
    @PostMapping
    public ResponseEntity<Test> createTest(@RequestBody Test test) {
        try {
            Test savedTest = testService.createTest(test);
            return ResponseEntity.ok(savedTest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 更新测试项
    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable Long id, @RequestBody Test test) {
        try {
            Test updatedTest = testService.updateTest(id, test);
            return ResponseEntity.ok(updatedTest);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 删除测试项
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.ok().build();
    }
}