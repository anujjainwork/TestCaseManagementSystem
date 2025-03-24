package com.example.TestCaseManagementSystem.controllers;

import com.example.TestCaseManagementSystem.dtos.TestCaseRequestDto;
import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestCaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Test GET /api/testcases without filters (default)
    @Test
    void testGetTestCases_Default() throws Exception {
        mockMvc.perform(get("/api/testcases")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respMsg", is("Test cases retrieved successfully")));
    }

    // Test GET /api/testcases with status filter
    @Test
    void testGetTestCases_FilterByStatus() throws Exception {
        mockMvc.perform(get("/api/testcases")
                        .param("status", "PENDING")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respMsg", is("Test cases retrieved successfully")));
    }

    // Test GET /api/testcases with priority filter
    @Test
    void testGetTestCases_FilterByPriority() throws Exception {
        mockMvc.perform(get("/api/testcases")
                        .param("priority", "HIGH")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respMsg", is("Test cases retrieved successfully")));
    }

    // Test GET /api/testcases/{id}
    // Note: For integration tests, you must ensure that a test case with the given ID exists.
    // You might need to preload the test data before running this test. Or else it will give 404 instead of 200.
    @Test
    void testGetTestCaseById() throws Exception {
        mockMvc.perform(get("/api/testcases/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respMsg", is("Test case retrieved successfully")));
    }

    // Test POST /api/testcases/create
    @Test
    void testCreateTestCase() throws Exception {
        TestCaseRequestDto requestDto = new TestCaseRequestDto();
        requestDto.setTitle("Integration Test Case");
        requestDto.setDescription("Integration test description");
        requestDto.setStatus(TestStatus.PENDING);
        requestDto.setPriority(TestPriority.HIGH);

        mockMvc.perform(post("/api/testcases/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.respMsg", is("Test case created successfully")));
    }

    // Test PUT /api/testcases/{id}
    @Test
    void testUpdateTestCase() throws Exception {
        TestCaseRequestDto requestDto = new TestCaseRequestDto();
        requestDto.setTitle("Updated Test Case");
        requestDto.setDescription("Updated description");
        requestDto.setStatus(TestStatus.PASSED);
        requestDto.setPriority(TestPriority.MEDIUM);

        mockMvc.perform(put("/api/testcases/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respMsg", is("Test case updated successfully")));
    }

    // Test DELETE /api/testcases/{id}
    @Test
    void testDeleteTestCase() throws Exception {
        mockMvc.perform(delete("/api/testcases/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respMsg", is("Test case deleted successfully")));
    }
}
