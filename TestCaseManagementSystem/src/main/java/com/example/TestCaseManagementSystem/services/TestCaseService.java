package com.example.TestCaseManagementSystem.services;

import com.example.TestCaseManagementSystem.dtos.TestCaseDto;
import com.example.TestCaseManagementSystem.dtos.TestCaseRequestDto;
import com.example.TestCaseManagementSystem.entities.TestCase;
import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import com.example.TestCaseManagementSystem.exceptions.ResourceNotFoundException;
import com.example.TestCaseManagementSystem.repositories.TestCaseRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestCaseService {

    private final TestCaseRepository repository;
    private final ModelMapper modelMapper;

    public Page<TestCaseDto> getTestCases(TestStatus status, TestPriority priority, Pageable pageable) {
        log.info("Fetching test cases with status {} and priority {} for page {}", status, priority, pageable.getPageNumber());
        Page<TestCase> cases;
        if (status != null && priority != null) {
            cases = repository.findByStatusAndPriority(status, priority, pageable);
        } else if(status != null) {
            cases = repository.findByStatus(status, pageable);
        } else if(priority != null) {
            cases = repository.findByPriority(priority, pageable);
        } else {
            cases = repository.findAll(pageable);
        }
        log.debug("Retrieved {} test cases", cases.getTotalElements());
        return cases.map(testCase -> modelMapper.map(testCase, TestCaseDto.class));
    }

    public TestCaseDto getTestCaseById(String id) {
        log.info("Fetching test case with id {}", id);
        TestCase testCase = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Test case not found with id {}", id);
                    return new ResourceNotFoundException("Test case not found");
                });
        log.debug("Found test case with id {}", id);
        return modelMapper.map(testCase, TestCaseDto.class);
    }

    public TestCaseDto createTestCase(TestCaseRequestDto dto) {
        log.info("Creating new test case with title {}", dto.getTitle());
        TestCase testCase = modelMapper.map(dto, TestCase.class);
        testCase.setCreatedAt(LocalDateTime.now());
        testCase.setUpdatedAt(LocalDateTime.now());
        TestCase saved = repository.save(testCase);
        log.debug("Created test case with id {}", saved.getId());
        return modelMapper.map(saved, TestCaseDto.class);
    }

    public TestCaseDto updateTestCase(String id, TestCaseRequestDto dto) {
        log.info("Updating test case with id {}", id);
        TestCase existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Test case not found with id {}", id);
                    return new ResourceNotFoundException("Test case not found");
                });
        modelMapper.map(dto, existing);
        existing.setUpdatedAt(LocalDateTime.now());
        TestCase updated = repository.save(existing);
        log.debug("Updated test case with id {}", updated.getId());
        return modelMapper.map(updated, TestCaseDto.class);
    }

    public void deleteTestCase(String id) {
        log.info("Deleting test case with id {}", id);
        repository.deleteById(id);
        log.debug("Deleted test case with id {}", id);
    }
}
