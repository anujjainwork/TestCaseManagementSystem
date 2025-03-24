package com.example.TestCaseManagementSystem.configurations;

import com.example.TestCaseManagementSystem.entities.TestCase;
import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import com.example.TestCaseManagementSystem.repositories.TestCaseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(TestCaseRepository repository) {
        return args -> {
            repository.deleteAll(); // Clear existing data (optional)

            List<TestCase> testCases = List.of(
                    TestCase.builder()
                            .title("Login Test")
                            .description("Verify user login functionality")
                            .status(TestStatus.PENDING)
                            .priority(TestPriority.HIGH)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),

                    TestCase.builder()
                            .title("Registration Test")
                            .description("Ensure registration works correctly")
                            .status(TestStatus.IN_PROGRESS)
                            .priority(TestPriority.MEDIUM)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),

                    TestCase.builder()
                            .title("Logout Test")
                            .description("Test user logout functionality")
                            .status(TestStatus.PASSED)
                            .priority(TestPriority.LOW)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),

                    TestCase.builder()
                            .title("Payment Processing Test")
                            .description("Ensure payment transactions are processed correctly")
                            .status(TestStatus.FAILED)
                            .priority(TestPriority.HIGH)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),

                    TestCase.builder()
                            .title("Profile Update Test")
                            .description("Verify that users can update their profile details")
                            .status(TestStatus.IN_PROGRESS)
                            .priority(TestPriority.MEDIUM)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),

                    TestCase.builder()
                            .title("Password Reset Test")
                            .description("Check if users can reset their password")
                            .status(TestStatus.PENDING)
                            .priority(TestPriority.HIGH)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),

                    TestCase.builder()
                            .title("Search Functionality Test")
                            .description("Ensure that the search feature returns correct results")
                            .status(TestStatus.PASSED)
                            .priority(TestPriority.LOW)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
            );

            repository.saveAll(testCases);
        };
    }
}

