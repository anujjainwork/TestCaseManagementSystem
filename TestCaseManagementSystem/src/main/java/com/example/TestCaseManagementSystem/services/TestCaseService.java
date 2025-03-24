package com.example.TestCaseManagementSystem.services;

import com.example.TestCaseManagementSystem.dtos.TestCaseDto;
import com.example.TestCaseManagementSystem.dtos.TestCaseRequestDto;
import com.example.TestCaseManagementSystem.entities.TestCase;
import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import com.example.TestCaseManagementSystem.repositories.TestCaseRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TestCaseService {

    private final TestCaseRepository repository;
    private final ModelMapper modelMapper;

    public Page<TestCaseDto> getTestCases(TestStatus status, TestPriority priority, Pageable pageable) {
        Page<TestCase> cases;
        if (status != null && priority != null) {
            cases = repository.findByStatusAndPriority(status, priority, pageable);
        } else if(status != null) {
            cases = repository.findByStatus(status,pageable);
        } else if(priority != null){
            cases = repository.findByPriority(priority,pageable);
        } else {
            cases = repository.findAll(pageable);
        }
        return cases.map(testCase -> modelMapper.map(testCase, TestCaseDto.class));
    }

    public TestCaseDto getTestCaseById(String id) {
        TestCase testCase = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test case not found"));
        return modelMapper.map(testCase, TestCaseDto.class);
    }

    public TestCaseDto createTestCase(TestCaseRequestDto dto) {
        TestCase testCase = modelMapper.map(dto, TestCase.class);
        testCase.setCreatedAt(LocalDateTime.now());
        testCase.setUpdatedAt(LocalDateTime.now());
        TestCase saved = repository.save(testCase);
        return modelMapper.map(saved, TestCaseDto.class);
    }

    public TestCaseDto updateTestCase(String id, TestCaseRequestDto dto) {
        TestCase existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test case not found"));
        modelMapper.map(dto, existing);
        existing.setUpdatedAt(LocalDateTime.now());
        TestCase updated = repository.save(existing);
        return modelMapper.map(updated, TestCaseDto.class);
    }

    public void deleteTestCase(String id) {
        repository.deleteById(id);
    }
}
