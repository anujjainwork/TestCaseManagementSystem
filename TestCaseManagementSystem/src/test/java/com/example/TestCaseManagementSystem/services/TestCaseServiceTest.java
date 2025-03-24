package com.example.TestCaseManagementSystem.services;

import com.example.TestCaseManagementSystem.dtos.TestCaseDto;
import com.example.TestCaseManagementSystem.dtos.TestCaseRequestDto;
import com.example.TestCaseManagementSystem.entities.TestCase;
import com.example.TestCaseManagementSystem.enums.TestPriority;
import com.example.TestCaseManagementSystem.enums.TestStatus;
import com.example.TestCaseManagementSystem.exceptions.ResourceNotFoundException;
import com.example.TestCaseManagementSystem.repositories.TestCaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TestCaseServiceTest {

    @Mock
    private TestCaseRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TestCaseService service;

    // Test for getTestCaseById when not found
    @Test
    void testGetTestCaseById_NotFound() {
        when(repository.findById("nonexistentId")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getTestCaseById("nonexistentId"));
        verify(repository, times(1)).findById("nonexistentId");
    }

    // Test for getTestCaseById when found
    @Test
    void testGetTestCaseById_Found() {
        String id = "1";
        TestCase testCase = TestCase.builder()
                .id(id)
                .title("Found Test")
                .status(TestStatus.PENDING)
                .priority(TestPriority.HIGH)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        TestCaseDto testCaseDto = new TestCaseDto();

        when(repository.findById(id)).thenReturn(Optional.of(testCase));
        when(modelMapper.map(testCase, TestCaseDto.class)).thenReturn(testCaseDto);

        TestCaseDto result = service.getTestCaseById(id);
        assertNotNull(result);
        verify(repository, times(1)).findById(id);
    }

    // Test for createTestCase
    @Test
    void testCreateTestCase() {
        // Prepare request DTO
        TestCaseRequestDto requestDto = new TestCaseRequestDto();
        requestDto.setTitle("Sample Test");
        requestDto.setDescription("Test description");
        requestDto.setStatus(TestStatus.PENDING);
        requestDto.setPriority(TestPriority.HIGH);

        // Prepare entity and saved entity
        TestCase testCase = TestCase.builder()
                .title("Sample Test")
                .description("Test description")
                .status(TestStatus.PENDING)
                .priority(TestPriority.HIGH)
                .build();
        TestCase savedTestCase = TestCase.builder()
                .id("1")
                .title("Sample Test")
                .description("Test description")
                .status(TestStatus.PENDING)
                .priority(TestPriority.HIGH)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        TestCaseDto expectedDto = new TestCaseDto();

        // Setup expectations
        when(modelMapper.map(requestDto, TestCase.class)).thenReturn(testCase);
        when(repository.save(testCase)).thenReturn(savedTestCase);
        when(modelMapper.map(savedTestCase, TestCaseDto.class)).thenReturn(expectedDto);

        TestCaseDto result = service.createTestCase(requestDto);
        assertNotNull(result);
        verify(repository, times(1)).save(testCase);
    }

    // Test for updateTestCase when found
    @Test
    void testUpdateTestCase_Found() {
        String id = "1";
        TestCaseRequestDto dto = new TestCaseRequestDto();
        dto.setTitle("Updated Test");
        dto.setDescription("Updated description");
        dto.setStatus(TestStatus.PASSED);
        dto.setPriority(TestPriority.MEDIUM);

        TestCase existingTestCase = TestCase.builder()
                .id(id)
                .title("Old Test")
                .description("Old description")
                .status(TestStatus.PENDING)
                .priority(TestPriority.HIGH)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();
        TestCase updatedTestCase = TestCase.builder()
                .id(id)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .createdAt(existingTestCase.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        TestCaseDto expectedDto = new TestCaseDto();

        when(repository.findById(id)).thenReturn(Optional.of(existingTestCase));
        // When mapping from DTO to entity, the mapper updates existingTestCase fields
        doNothing().when(modelMapper).map(dto, existingTestCase);
        when(repository.save(existingTestCase)).thenReturn(updatedTestCase);
        when(modelMapper.map(updatedTestCase, TestCaseDto.class)).thenReturn(expectedDto);

        TestCaseDto result = service.updateTestCase(id, dto);
        assertNotNull(result);
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existingTestCase);
    }

    // Test for updateTestCase when not found
    @Test
    void testUpdateTestCase_NotFound() {
        String id = "1";
        TestCaseRequestDto dto = new TestCaseRequestDto();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.updateTestCase(id, dto));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(TestCase.class));
    }

    // Test for deleteTestCase
    @Test
    void testDeleteTestCase() {
        String id = "1";
        // For delete, no return value is expected
        doNothing().when(repository).deleteById(id);
        service.deleteTestCase(id);
        verify(repository, times(1)).deleteById(id);
    }

    // Test for getTestCases with no filters (pagination test: page 1)
    @Test
    void testGetTestCases_Pagination_NoFilters_Page1() {
        Pageable pageable = PageRequest.of(0, 10);
        TestCase testCase = TestCase.builder()
                .id("1")
                .title("Paginated Test")
                .status(TestStatus.PENDING)
                .priority(TestPriority.HIGH)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Page<TestCase> page = new PageImpl<>(Collections.singletonList(testCase), pageable, 1);
        when(repository.findAll(pageable)).thenReturn(page);
        TestCaseDto testCaseDto = new TestCaseDto();
        when(modelMapper.map(testCase, TestCaseDto.class)).thenReturn(testCaseDto);

        Page<TestCaseDto> result = service.getTestCases(null, null, pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    // Test for getTestCases with only status provided (pagination: page 2)
    @Test
    void testGetTestCases_FilterByStatus_Page2() {
        TestStatus status = TestStatus.PENDING;
        Pageable pageable = PageRequest.of(1, 10); // page index 1 is page 2
        TestCase testCase = TestCase.builder()
                .id("2")
                .title("Status Filter Test")
                .status(status)
                .priority(TestPriority.MEDIUM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Page<TestCase> page = new PageImpl<>(Collections.singletonList(testCase), pageable, 11); // total 11 elements so page 2 exists
        when(repository.findByStatus(status, pageable)).thenReturn(page);
        TestCaseDto testCaseDto = new TestCaseDto();
        when(modelMapper.map(testCase, TestCaseDto.class)).thenReturn(testCaseDto);

        Page<TestCaseDto> result = service.getTestCases(status, null, pageable);
        assertNotNull(result);
        assertEquals(11, result.getTotalElements());
        verify(repository, times(1)).findByStatus(status, pageable);
    }

    @Test
    void testGetTestCases_FilterByPriority_Page2() {
        TestPriority priority = TestPriority.HIGH;
        Pageable pageable = PageRequest.of(1, 10); // Page 2
        TestCase testCase = TestCase.builder()
                .id("3")
                .title("Priority Filter Test")
                .status(TestStatus.PASSED)
                .priority(priority)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Simulating a total count of 15 elements in the paginated response
        Page<TestCase> page = new PageImpl<>(Collections.singletonList(testCase), pageable, 15);
        when(repository.findByPriority(priority, pageable)).thenReturn(page);
        TestCaseDto testCaseDto = new TestCaseDto();
        when(modelMapper.map(testCase, TestCaseDto.class)).thenReturn(testCaseDto);

        // Log actual data
        log.info("Executing test case for priority filtering with page 2...");
        Page<TestCaseDto> result = service.getTestCases(null, priority, pageable);
        log.info("Test case result count: {}", result.getTotalElements());

        assertNotNull(result);
        assertEquals(page.getTotalElements(), result.getTotalElements()); // Matching expected with actual
        verify(repository, times(1)).findByPriority(priority, pageable);
    }


    // Test for getTestCases pagination beyond available data (page 3 with no data)
    @Test
    void testGetTestCases_Pagination_PageBeyondAvailable() {
        Pageable pageable = PageRequest.of(2, 10); // Page 3 (0-based index)
        Page<TestCase> emptyPage = Page.empty(pageable);
        when(repository.findAll(pageable)).thenReturn(emptyPage);

        Page<TestCaseDto> result = service.getTestCases(null, null, pageable);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll(pageable);
    }
}
