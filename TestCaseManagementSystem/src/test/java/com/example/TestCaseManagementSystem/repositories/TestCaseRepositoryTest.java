package com.example.TestCaseManagementSystem.repositories;

import com.example.TestCaseManagementSystem.entities.TestCase;
import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class TestCaseRepositoryTest {

    @Autowired
    private TestCaseRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll(); // Clean up before each test

        TestCase testCase1 = TestCase.builder()
                .title("Test Case 1")
                .status(TestStatus.PASSED)
                .priority(TestPriority.HIGH)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TestCase testCase2 = TestCase.builder()
                .title("Test Case 2")
                .status(TestStatus.FAILED)
                .priority(TestPriority.MEDIUM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TestCase testCase3 = TestCase.builder()
                .title("Test Case 3")
                .status(TestStatus.PENDING)
                .priority(TestPriority.LOW)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        repository.saveAll(List.of(testCase1, testCase2, testCase3)); // Save test data
    }

    @Test
    void testFindByStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestCase> result = repository.findByStatus(TestStatus.PASSED, pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(TestStatus.PASSED, result.getContent().get(0).getStatus());
    }

    @Test
    void testFindByPriority() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestCase> result = repository.findByPriority(TestPriority.MEDIUM, pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(TestPriority.MEDIUM, result.getContent().get(0).getPriority());
    }

    @Test
    void testFindByStatusAndPriority() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestCase> result = repository.findByStatusAndPriority(TestStatus.FAILED, TestPriority.MEDIUM, pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(TestStatus.FAILED, result.getContent().get(0).getStatus());
        assertEquals(TestPriority.MEDIUM, result.getContent().get(0).getPriority());
    }

    @Test
    void testPagination_MultiplePages() {
        // Clear repository to ensure known state
        repository.deleteAll();

        // Add exactly 15 test cases (to simulate a known total)
        for (int i = 0; i < 15; i++) {
            repository.save(TestCase.builder()
                    .title("Paginated Test " + i)
                    .status(TestStatus.PENDING)
                    .priority(TestPriority.LOW)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build());
        }

        // Request page 2 (0-based indexing: page 1 means second page) with 5 items per page
        Pageable pageable = PageRequest.of(1, 5);
        Page<TestCase> result = repository.findByStatus(TestStatus.PENDING, pageable);

        assertNotNull(result);
        // Page 2 should have exactly 5 items because 15 total elements yield pages: [5, 5, 5]
        assertEquals(5, result.getContent().size(), "Expected page size of 5");
        // Total elements should be 15
        assertEquals(15, result.getTotalElements(), "Expected total elements to be 15");
    }
}
