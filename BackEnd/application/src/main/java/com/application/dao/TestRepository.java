package com.application.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.application.entity.Test;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    boolean existsByName(String name);
    Optional<Test> findByName(String name);
}