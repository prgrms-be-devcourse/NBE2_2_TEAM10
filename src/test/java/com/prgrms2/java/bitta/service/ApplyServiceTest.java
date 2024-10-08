package com.prgrms2.java.bitta.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.apply.repository.ApplyRepository;
import com.prgrms2.java.bitta.apply.service.ApplyServiceImpl;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.entity.Location;
import com.prgrms2.java.bitta.jobpost.entity.PayStatus;
import com.prgrms2.java.bitta.jobpost.util.JobPostProvider;
import com.prgrms2.java.bitta.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Map;

class ApplyServiceTest {

    @Mock
    private ApplyRepository applyRepository;

    @Mock
    private MemberProvider memberProvider;

    @Mock
    private JobPostProvider jobPostProvider;  // Mock된 JobPostProvider 추가

    @InjectMocks
    private ApplyServiceImpl applyService;

    private ApplyDTO applyDTO;
    private Apply apply;
    private Member member;
    private JobPost jobPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        member = Member.builder()
                .id(1L)
                .username("testUser")
                .password("testPass")
                .nickname("testNickname")
                .address("testAddress")
                .build();

        jobPost = JobPost.builder()
                .id(100L)
                .member(member)
                .title("Test Job Title")
                .description("This is a test job description.")
                .location(Location.SEOUL)
                .payStatus(PayStatus.PAID)
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 10))
                .build();

        applyDTO = ApplyDTO.builder()
                .id(1L)
                .memberId(member.getId())
                .jobPostId(jobPost.getId())
                .appliedAt(LocalDateTime.now())
                .build();

        apply = Apply.builder()
                .id(1L)
                .member(member)
                .jobPost(jobPost)
                .appliedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("등록 테스트")
    void registerApplyTest() {
        when(jobPostProvider.getById(anyLong())).thenReturn(jobPost);
        when(applyRepository.save(any(Apply.class))).thenReturn(apply);

        Map<String, Object> response = applyService.register(applyDTO);

        assertThat(response.get("message")).isEqualTo(apply.getMember().getNickname() + "님 지원 완료");
        assertThat(((ApplyDTO) response.get("data")).getId()).isEqualTo(apply.getId());
    }

    @Test
    @DisplayName("단일 조회 테스트")
    void readApplyTest() {
        when(applyRepository.getApplyDTO(anyLong())).thenReturn(Optional.of(applyDTO));

        ApplyDTO foundDTO = applyService.read(1L);

        assertThat(foundDTO.getId()).isEqualTo(applyDTO.getId());
    }

    @Test
    @DisplayName("회원별 조회 테스트")
    void readByIdAndMemberTest() {
        when(applyRepository.findByIdAndMember(anyLong(), any(Member.class))).thenReturn(Optional.of(apply));

        ApplyDTO foundDTO = applyService.readByIdAndMember(1L, member);

        assertThat(foundDTO.getId()).isEqualTo(apply.getId());
    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteApplyTest() {
        when(applyRepository.deleteByIdAndReturnCount(anyLong())).thenReturn(1);

        applyService.delete(1L);

        verify(applyRepository, times(1)).deleteByIdAndReturnCount(1L);
    }
}

