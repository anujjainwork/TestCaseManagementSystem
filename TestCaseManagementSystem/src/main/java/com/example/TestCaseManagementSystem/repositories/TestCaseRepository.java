package com.example.TestCaseManagementSystem.repositories;

import com.example.TestCaseManagementSystem.entities.TestCase;
import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TestCaseRepository extends MongoRepository<TestCase, String> {

    Page<TestCase> findByStatusAndPriority(TestStatus status, TestPriority testPriority, Pageable pageable);

    Page<TestCase> findByStatus(TestStatus status, Pageable pageable);

    Page<TestCase> findByPriority(TestPriority testPriority, Pageable pageable);
}
