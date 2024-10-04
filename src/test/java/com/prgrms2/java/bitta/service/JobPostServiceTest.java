package com.prgrms2.java.bitta.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.global.dto.PageRequestDTO;
import com.prgrms2.java.bitta.jobpost.repository.JobPostRepository;
import com.prgrms2.java.bitta.jobpost.service.JobPostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

class JobPostServiceTest {

    @Mock
    private JobPostRepository jobPostRepository;

    @InjectMocks
    private JobPostServiceImpl jobPostService;

    private PageRequestDTO pageRequestDTO;
    private List<JobPostDTO> jobPostDTOList;
    private JobPostDTO jobPostDTO1;
    private JobPostDTO jobPostDTO2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        jobPostDTO1 = JobPostDTO.builder()
                .id(1L)
                .title("Job Post 1")
                .description("Description 1")
                .build();

        jobPostDTO2 = JobPostDTO.builder()
                .id(2L)
                .title("Job Post 2")
                .description("Description 2")
                .build();

        jobPostDTOList = Arrays.asList(jobPostDTO1, jobPostDTO2);

        pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
    }

    @Test
    @DisplayName("페이징된 전체 목록 조회")
    void testGetListWithPaging() {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("id").descending());
        Page<JobPostDTO> jobPostDTOPage = new PageImpl<>(jobPostDTOList, pageable, jobPostDTOList.size());

        when(jobPostRepository.getList(pageable)).thenReturn(jobPostDTOPage);

        Page<JobPostDTO> resultPage = jobPostService.getList(pageRequestDTO);

        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getTotalPages()).isEqualTo(1);
        assertThat(resultPage.getContent().get(0).getTitle()).isEqualTo("Job Post 1");
        assertThat(resultPage.getContent().get(1).getTitle()).isEqualTo("Job Post 2");

        verify(jobPostRepository, times(1)).getList(pageable);
    }
}
