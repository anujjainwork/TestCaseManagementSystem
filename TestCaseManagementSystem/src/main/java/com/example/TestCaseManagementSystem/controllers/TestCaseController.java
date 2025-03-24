package com.example.TestCaseManagementSystem.controllers;

import com.example.TestCaseManagementSystem.apiresponses.StandardApiResponse;
import com.example.TestCaseManagementSystem.dtos.TestCaseDto;
import com.example.TestCaseManagementSystem.dtos.TestCaseRequestDto;
import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import com.example.TestCaseManagementSystem.services.TestCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class TestCaseController {

    private final TestCaseService service;

    @GetMapping
    public ResponseEntity<StandardApiResponse<Page<TestCaseDto>>> getTestCases(
            @RequestParam(required = false) TestStatus status,
            @RequestParam(required = false) TestPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TestCaseDto> testCases = service.getTestCases(status, priority, PageRequest.of(page, size));

        return ResponseEntity.ok(new StandardApiResponse<>(
                (int) testCases.getTotalElements(),
                HttpStatus.OK.value(),
                "Test cases retrieved successfully",
                testCases
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardApiResponse<TestCaseDto>> getTestCase(@PathVariable String id) {
        TestCaseDto testCase = service.getTestCaseById(id);

        return ResponseEntity.ok(new StandardApiResponse<>(
                1,
                HttpStatus.OK.value(),
                "Test case retrieved successfully",
                testCase
        ));
    }

    @PostMapping("/create")
    public ResponseEntity<StandardApiResponse<TestCaseDto>> createTestCase(@RequestBody @Valid TestCaseRequestDto requestDTO) {
        TestCaseDto createdTestCase = service.createTestCase(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new StandardApiResponse<>(
                1,
                HttpStatus.CREATED.value(),
                "Test case created successfully",
                createdTestCase
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardApiResponse<TestCaseDto>> updateTestCase(@PathVariable String id, @RequestBody @Valid TestCaseRequestDto requestDTO) {
        TestCaseDto updatedTestCase = service.updateTestCase(id, requestDTO);

        return ResponseEntity.ok(new StandardApiResponse<>(
                1,
                HttpStatus.OK.value(),
                "Test case updated successfully",
                updatedTestCase
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardApiResponse<Void>> deleteTestCase(@PathVariable String id) {
        service.deleteTestCase(id);

        return ResponseEntity.ok(new StandardApiResponse<>(
                0,
                HttpStatus.OK.value(),
                "Test case deleted successfully",
                null
        ));
    }
}
