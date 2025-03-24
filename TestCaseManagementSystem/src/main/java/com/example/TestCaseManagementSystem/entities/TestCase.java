package com.example.TestCaseManagementSystem.entities;

import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "test_cases")
public class TestCase {

    @Id
    String id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Indexed
    private TestStatus status;

    @Indexed
    private TestPriority priority;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
