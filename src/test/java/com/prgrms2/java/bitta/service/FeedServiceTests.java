package com.prgrms2.java.bitta.service;
/*
import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.exception.FeedException;
import com.prgrms2.java.bitta.feed.exception.FeedTaskException;
import com.prgrms2.java.bitta.feed.repository.FeedRepository;
import com.prgrms2.java.bitta.feed.service.FeedServiceImpl;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.photo.entity.Photo;
import com.prgrms2.java.bitta.photo.service.PhotoService;
import com.prgrms2.java.bitta.video.service.VideoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class FeedServiceTests {
    @InjectMocks
    private FeedServiceImpl feedServiceImpl;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private MemberProvider memberProvider;

    @Mock
    private PhotoService photoService;

    @Mock
    private VideoService videoService;

    private Member member;

    private Feed feed;

    private FeedDTO feedDTO;

    private Photo photo;

    private List<Feed> feeds;

    private List<MultipartFile> files;

    @BeforeEach
    void initialize() {
        member = Member.builder()
                .id(1L)
                .username("Username")
                .password("Password")
                .address("Address")
//                .profileImg("ImageUrl")
                .build();

        feed = Feed.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .member(member)
                .build();

        feedDTO = FeedDTO.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .memberId(1L)
                .build();

        photo = Photo.builder()
                .photoId(1L)
                .photoUrl("uploads/photos/fake")
                .fileSize(100000L)
                .feed(feed)
                .build();

        feeds = new ArrayList<>();
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Feed _feed = Feed.builder()
                    .id((long) i)
                    .title("Title" + i)
                    .content("Content" + i)
                    .member(member)
                    .build();

            feeds.add(_feed);
        });

        files = new ArrayList<>();
        MultipartFile file = new MockMultipartFile("image", "image.png"
                , MediaType.IMAGE_PNG_VALUE, "image".getBytes());
        files.add(file);
    }

    void assertObject(FeedDTO feedDTO) {
        assertThat(feedDTO.getId()).isEqualTo(1L);
        assertThat(feedDTO.getTitle()).isEqualTo("Title");
        assertThat(feedDTO.getContent()).isEqualTo("Content");
        assertThat(feedDTO.getMemberId()).isEqualTo(1L);
    }

    void assertObject(List<FeedDTO> feedDTOs) {
        for (int i = 1; i < feedDTOs.size() + 1; i++) {
            FeedDTO _feedDTO = feedDTOs.get(i - 1);
            assertThat(_feedDTO.getId()).isEqualTo(i);
            assertThat(_feedDTO.getTitle()).isEqualTo("Title" + i);
            assertThat(_feedDTO.getContent()).isEqualTo("Content" + i);
            assertThat(_feedDTO.getMemberId()).isEqualTo(1L);
        }
    }

    @Test
    @DisplayName("피드 단일 조회 (성공)")
    void read_ValidId_ReturnFeedDTO() {
        when(feedRepository.findById(anyLong()))
                .thenReturn(Optional.of(feed));

        FeedDTO feedDTO = feedServiceImpl.read(1L);

        assertObject(feedDTO);
    }

    @Test
    @DisplayName("피드 단일 조회 (실패) :: 일치하는 피드 없음")
    void read_InvalidId_ThrowException() {
        feedDTO.setId(null);

        when(feedRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedServiceImpl.read(1L))
                .isInstanceOf(FeedTaskException.class)
                .hasMessage("피드가 존재하지 않습니다.");
    }
    
    @Test
    @DisplayName("피드 전체 조회 (성공)")
    void readAll_FeedExists_ReturnDtoList() {
        when(feedRepository.findAll())
                .thenReturn(feeds);

        List<FeedDTO> feedDTOs = feedServiceImpl.readAll();

        assertObject(feedDTOs);
    }

    @Test
    @DisplayName("피드 전체 조회 (실패) :: 피드 없음")
    void readAll_FeedNotExists_ThrowException() {
        when(feedRepository.findAll())
                .thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> feedServiceImpl.readAll())
                .isInstanceOf(FeedTaskException.class)
                .hasMessage("피드가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("피드 삽입 (성공)")
    void insert_ProcessWithoutException_InsertSuccess() throws Exception {
        feedDTO.setId(null);

        when(photoService.upload(any(MultipartFile.class)))
                .thenReturn(photo);

        when(feedRepository.save(any(Feed.class)))
                .thenReturn(feed);

        when(memberProvider.getById(anyLong())).thenReturn(member);

        feedServiceImpl.insert(feedDTO, files);
    }

    @Test
    @DisplayName("피드 삽입 (실패) :: 업데이트를 시도함")
    void insert_DtoContainsId_ThrowException() {
        assertThatThrownBy(() -> feedServiceImpl.insert(feedDTO, files))
                .isInstanceOf(FeedTaskException.class)
                .hasMessage("잘못된 요청입니다.");
    }

    @Test
    @DisplayName("피드 삽입 (실패) :: 파일 저장 실패")
    void insert_SaveWrongFile_ThrowException() throws IOException {
        feedDTO.setId(null);

        when(photoService.upload(any(MultipartFile.class)))
                .thenThrow(new IOException());

        assertThatThrownBy(() -> feedServiceImpl.insert(feedDTO, files))
                .isInstanceOf(FeedTaskException.class)
                .hasMessage("서버 내부에 오류가 발생했습니다.");
    }

    @Test
    @DisplayName("피드 수정 (성공)")
    void update_ProcessWithoutException_UpdateSuccess() throws IOException {
        when(feedRepository.findById(anyLong()))
                .thenReturn(Optional.of(feed));

        doNothing().when(photoService).delete(anyString());

        when(photoService.upload(any(MultipartFile.class)))
                .thenReturn(photo);

        when(feedRepository.save(any(Feed.class)))
                .thenReturn(feed);

        feedServiceImpl.update(feedDTO, files, new ArrayList<String>(){{
            add("uploads/photos/fake1");
            add("uploads/photos/fake2");
        }});
    }

    @Test
    @DisplayName("피드 수정 (실패) :: 피드 검색 실패")
    void update_FeedNotExists_ThrowException() {
        when(feedRepository.findById(anyLong()))
                .thenThrow(FeedException.CANNOT_FOUND.get());

        assertThatThrownBy(() -> feedServiceImpl.update(feedDTO, files, new ArrayList<String>()))
                .isInstanceOf(FeedTaskException.class)
                .hasMessage("피드가 존재하지 않습니다.");
    }
    
    @Test
    @DisplayName("피드 수정 (실패) :: 파일 삭제 실패")
    void update_FileDeleteFailed_ThrowException() {
        when(feedRepository.findById(anyLong()))
                .thenReturn(Optional.of(feed));

        doThrow(NoSuchElementException.class)
                .when(photoService).delete(anyString());

        assertThatThrownBy(() -> feedServiceImpl.update(feedDTO, files, new ArrayList<String>(){{
            add("uploads/photos/fake1");
            add("uploads/photos/fake2");}}))
                .isInstanceOf(FeedTaskException.class)
                .hasMessage("서버 내부에 오류가 발생했습니다.");
    }

    @Test
    @DisplayName("피드 수정 (실패) :: 파일 저장 실패")
    void update_FileSaveFailed_ThrowException() throws IOException {
        when(feedRepository.findById(anyLong()))
                .thenReturn(Optional.of(feed));

        doNothing().when(photoService).delete(anyString());

        when(photoService.upload(any(MultipartFile.class)))
                .thenThrow(FeedException.INTERNAL_ERROR.get());

        assertThatThrownBy(() -> feedServiceImpl.update(feedDTO, files, new ArrayList<String>(){{
            add("uploads/photos/fake1");
            add("uploads/photos/fake2");}}))
                .isInstanceOf(FeedTaskException.class)
                .hasMessage("서버 내부에 오류가 발생했습니다.");
    }

    @Test
    @DisplayName("피드 삭제 (성공)")
    void delete_ProcessWithoutException_DeleteSuccess() {
        doNothing().when(photoService).delete(anyLong());
        doNothing().when(videoService).delete(anyLong());

        when(feedRepository.deleteByIdAndReturnCount(anyLong()))
                .thenReturn(1L);

        feedServiceImpl.delete(1L);

        verify(photoService, times(1)).delete(anyLong());
        verify(videoService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("피드 삭제 (실패) :: 피드 검색 실패")
    void delete_FeedNotExists_ThrowException() {
        doNothing().when(photoService).delete(anyLong());
        doNothing().when(videoService).delete(anyLong());

        when(feedRepository.deleteByIdAndReturnCount(anyLong()))
                .thenReturn(0L);

        assertThatThrownBy(() -> feedServiceImpl.delete(1L))
                .isInstanceOf(FeedTaskException.class)
                .hasMessage("삭제할 피드가 존재하지 않습니다");
    }
}
*/