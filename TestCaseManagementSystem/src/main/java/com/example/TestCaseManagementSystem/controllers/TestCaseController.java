package com.example.TestCaseManagementSystem.controllers;

import com.example.TestCaseManagementSystem.apiresponses.StandardApiResponse;
import com.example.TestCaseManagementSystem.dtos.TestCaseDto;
import com.example.TestCaseManagementSystem.dtos.TestCaseRequestDto;
import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import com.example.TestCaseManagementSystem.services.TestCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/testcases")
@RequiredArgsConstructor
@Validated
@Slf4j
public class TestCaseController {

    private final TestCaseService service;

    @GetMapping
    public ResponseEntity<StandardApiResponse<Page<TestCaseDto>>> getTestCases(
            @RequestParam(required = false) TestStatus status,
            @RequestParam(required = false) TestPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching test cases with status: {} and priority: {} at page: {} with size: {}", status, priority, page, size);
        Page<TestCaseDto> testCases = service.getTestCases(status, priority, PageRequest.of(page, size));
        log.debug("Retrieved {} test cases", testCases.getTotalElements());
        StandardApiResponse<Page<TestCaseDto>> response = new StandardApiResponse<>(
                (int) testCases.getTotalElements(),
                HttpStatus.OK.value(),
                "Test cases retrieved successfully",
                testCases
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardApiResponse<TestCaseDto>> getTestCase(@PathVariable String id) {
        log.info("Fetching test case with id: {}", id);
        TestCaseDto testCase = service.getTestCaseById(id);
        log.debug("Fetched test case: {}", testCase);
        StandardApiResponse<TestCaseDto> response = new StandardApiResponse<>(
                1,
                HttpStatus.OK.value(),
                "Test case retrieved successfully",
                testCase
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<StandardApiResponse<TestCaseDto>> createTestCase(@RequestBody @Valid TestCaseRequestDto requestDTO) {
        log.info("Creating test case with title: {}", requestDTO.getTitle());
        TestCaseDto createdTestCase = service.createTestCase(requestDTO);
        log.debug("Created test case: {}", createdTestCase);
        StandardApiResponse<TestCaseDto> response = new StandardApiResponse<>(
                1,
                HttpStatus.CREATED.value(),
                "Test case created successfully",
                createdTestCase
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardApiResponse<TestCaseDto>> updateTestCase(@PathVariable String id, @RequestBody @Valid TestCaseRequestDto requestDTO) {
        log.info("Updating test case with id: {}", id);
        TestCaseDto updatedTestCase = service.updateTestCase(id, requestDTO);
        log.debug("Updated test case: {}", updatedTestCase);
        StandardApiResponse<TestCaseDto> response = new StandardApiResponse<>(
                1,
                HttpStatus.OK.value(),
                "Test case updated successfully",
                updatedTestCase
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardApiResponse<Void>> deleteTestCase(@PathVariable String id) {
        log.info("Deleting test case with id: {}", id);
        service.deleteTestCase(id);
        log.debug("Deleted test case with id: {}", id);
        StandardApiResponse<Void> response = new StandardApiResponse<>(
                0,
                HttpStatus.OK.value(),
                "Test case deleted successfully",
                null
        );
        return ResponseEntity.ok(response);
    }
}