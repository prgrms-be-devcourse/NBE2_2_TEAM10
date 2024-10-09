package com.prgrms2.java.bitta.feed.controller;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.service.FeedService;
import com.prgrms2.java.bitta.global.exception.AuthenticationException;
import com.prgrms2.java.bitta.global.util.AuthenticationProvider;
import com.prgrms2.java.bitta.media.dto.MediaDto;
import com.prgrms2.java.bitta.member.entity.Role;
import com.prgrms2.java.bitta.member.service.MemberProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.prgrms2.java.bitta.global.constants.ApiResponses.*;

@Tag(name = "피드 API 컨트롤러", description = "피드와 관련된 REST API를 제공하는 컨틀롤러입니다.")
@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
@Validated
public class FeedController {
    private final FeedService feedService;

    @Operation(
            summary = "피드 목록 조회",
            description = "페이지와 사이즈를 조건으로 피드 목록을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "피드를 성공적으로 조회했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_SUCCESS_READ_ALL)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "피드가 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_NOT_FOUND)
                            )
                    )
            }
    )
    @Parameters({
            @Parameter(
                    name = "page",
                    description = "피드 페이지 번호",
                    required = true,
                    example = "0",
                    schema = @Schema(type = "integer", defaultValue = "0", minimum = "0")
            ),
            @Parameter(
                    name = "size",
                    description = "피드 페이지 크기",
                    required = true,
                    example = "10",
                    schema = @Schema(type = "integer", defaultValue = "10", minimum = "1")
            )
    })
    @GetMapping
    public ResponseEntity<?> getFeeds(@RequestParam(required = false, defaultValue = "0", value = "page") int page
            , @RequestParam(required = false, defaultValue = "10", value = "size") int size
            , @RequestParam(required = false, value = "username") String username
            , @RequestParam(required = false, value = "title") String title) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                Map.of("message", "피드를 성공적으로 조회했습니다."
                        , "result", feedService.readAll(pageable, username, title)));
    }

    @Operation(
            summary = "단일 피드 조회",
            description = "하나의 피드를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "피드를 성공적으로 조회했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_SUCCESS_READ)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "0 이하의 ID를 입력했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_WRONG_PATH_VARIABLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "피드가 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_NOT_FOUND)
                            )
                    )
            }
    )
    @Parameter(
            name = "id",
            description = "조회할 피드의 ID",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", minimum = "1")
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedById(@PathVariable("id") @Min(1) Long id) {
        return ResponseEntity.ok(
                Map.of("message", "피드를 성공적으로 조회했습니다.", "result", feedService.read(id))
        );
    }

    @Operation(
            summary = "피드 등록",
            description = "피드를 등록합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "피드가 등록되었습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_SUCCESS_CREATE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "ID 필드가 비어있지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_BAD_REQUEST)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "파라미터 검증에 실패했습니다. (ex) 비어있는 제목 필드",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_WRONG_REQUEST_BODY)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "파일을 등록하는데 실패했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_INTERNAL_ERROR)
                            )
                    )
            }
    )

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createFeed(@RequestPart(value = "feed") @Valid FeedDTO feedDto
            , @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        feedService.insert(feedDto, files);

        return ResponseEntity.ok().body(Map.of("message", "피드가 등록되었습니다."));
    }

    @Operation(
            summary = "피드 수정",
            description = "피드를 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "피드가 수정되었습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_SUCCESS_MODIFY)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "0 이하의 ID를 입력했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_WRONG_PATH_VARIABLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "파라미터 검증에 실패했습니다. (ex) 비어있는 제목 필드",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_WRONG_REQUEST_BODY)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "수정할 피드가 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_NOT_FOUND)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "파일을 수정하는데 실패했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_INTERNAL_ERROR)
                            )
                    )
            }
    )
    @Parameter(
            name = "id",
            description = "수정할 피드의 ID",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", minimum = "1")
    )
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> modifyFeed(@PathVariable("id") @Min(1) Long id, @RequestPart("feed") @Valid FeedDTO feedDTO
            , @RequestPart("filesToUpload") List<MultipartFile> filesToUpload, @RequestPart("filesToDelete") List<MediaDto> filesToDelete) {
        if (!checkPermission(id)) {
            throw AuthenticationException.CANNOT_ACCESS.get();
        }

        feedDTO.setId(id);

        feedService.update(feedDTO, filesToUpload, filesToDelete);

        return ResponseEntity.ok().body(Map.of("message", "피드가 수정되었습니다."));
    }

    @Operation(
            summary = "피드 삭제",
            description = "피드를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "피드가 삭제되었습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_SUCCESS_DELETE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "0 이하의 ID를 입력했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_WRONG_PATH_VARIABLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "삭제할 피드가 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = FEED_FAILURE_NOT_FOUND)
                            )
                    )
            }
    )
    @Parameter(
            name = "id",
            description = "삭제할 피드의 ID",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", minimum = "1")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeed(@PathVariable("id") @Min(1) Long id) {
        if (!checkPermission(id)) {
            throw AuthenticationException.CANNOT_ACCESS.get();
        }

        feedService.delete(id);

        return ResponseEntity.ok().body(Map.of("message", "피드가 삭제되었습니다."));
    }

    private boolean checkPermission(Long id) {
        if (AuthenticationProvider.getRoles() == Role.ROLE_ADMIN) {
            return true;
        }

        return feedService.checkAuthority(id, AuthenticationProvider.getUsername());
    }
}

