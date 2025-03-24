package com.example.TestCaseManagementSystem.dtos;

import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestCaseRequestDto {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private TestStatus status;

    private TestPriority priority;
}