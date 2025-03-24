package com.example.TestCaseManagementSystem.dtos;

import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestCaseDto {

    @NotBlank(message = "Id is required")
    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private TestStatus status;
    private TestPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
