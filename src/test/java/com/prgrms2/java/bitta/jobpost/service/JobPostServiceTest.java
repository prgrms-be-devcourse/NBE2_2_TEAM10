package com.prgrms2.java.bitta.jobpost.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.jobpost.dto.PageRequestDTO;
import com.prgrms2.java.bitta.jobpost.repository.JobPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

        // 테스트용 JobPostDTO 객체 생성
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

        // PageRequestDTO 설정 (페이지 1, 사이즈 10)
        pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
    }

    @Test
    void testGetListWithPaging() {
        // 페이징 처리된 JobPostDTO 리스트를 반환하는 Page 객체 생성
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("id").descending());
        Page<JobPostDTO> jobPostDTOPage = new PageImpl<>(jobPostDTOList, pageable, jobPostDTOList.size());

        // Repository에서 getList 메서드를 호출할 때 페이징된 데이터를 반환하도록 설정
        when(jobPostRepository.getList(pageable)).thenReturn(jobPostDTOPage);

        // Service에서 페이징된 결과를 가져옴
        Page<JobPostDTO> resultPage = jobPostService.getList(pageRequestDTO);

        // 검증: 반환된 페이지가 정상적으로 페이징되어 있는지 확인
        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getTotalPages()).isEqualTo(1);
        assertThat(resultPage.getContent().get(0).getTitle()).isEqualTo("Job Post 1");
        assertThat(resultPage.getContent().get(1).getTitle()).isEqualTo("Job Post 2");

        // Repository 메서드 호출이 예상대로 이루어졌는지 검증
        verify(jobPostRepository, times(1)).getList(pageable);
    }
}
