package com.prgrms2.java.bitta.jobpost.controller;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.service.ApplyService;
import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.global.dto.PageRequestDTO;
import com.prgrms2.java.bitta.jobpost.service.JobPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.prgrms2.java.bitta.global.constants.ApiResponses.*;

@Tag(name = "일거리 API 컨트롤러", description = "일거리와 관련된 REST API를 제공하는 컨트롤러입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/job-post")
@Log4j2
public class JobPostController {
    private final JobPostService jobPostService;
    private final ApplyService applyService;

    @Operation(
            summary = "전체 일거리 조회",
            description = "전체 일거리를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "일거리를 성공적으로 조회했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_SUCCESS_READ_ALL)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "일거리가 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_FAILURE_NOT_FOUND)
                            )
                    )
            }
    )

    @GetMapping
    public ResponseEntity<Page<JobPostDTO>> getList(@Valid PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(jobPostService.getList(pageRequestDTO));
    }

    @Operation(
            summary = "일거리 등록",
            description = "일거리를 등록합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "일거리를 성공적으로 등록했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_SUCCESS_REGISTER)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "일거리 등록에 실패했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_FAILURE_NOT_REGISTERED)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<JobPostDTO> registerJobPost(@Valid @RequestBody JobPostDTO jobPostDTO) {
        return ResponseEntity.ok(jobPostService.register(jobPostDTO));
    }

    @Operation(
            summary = "단일 일거리 조회",
            description = "ID와 일치하는 일거리를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "일거리를 성공적으로 조회했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_SUCCESS_READ)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "일거리가 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_FAILURE_NOT_FOUND)
                            )
                    )
            }
    )
    @Parameter(
            name = "id",
            description = "조회할 일거리의 ID",
            required = true,
            example = "1",
            schema = @Schema(type = "integer")
    )
    @GetMapping("/{id}")
    public ResponseEntity<JobPostDTO> readJobPost(@PathVariable("id") @Valid Long id) {
        return ResponseEntity.ok(jobPostService.read(id));
    }

    @Operation(
            summary = "일거리 수정",
            description = "ID와 일치하는 일거리를 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "일거리를 성공적으로 수정했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_SUCCESS_MODIFY)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "일거리 수정에 실패했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_FAILURE_NOT_MODIFIED)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "일거리가 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_FAILURE_NOT_FOUND)
                            )
                    )
            }
    )
    @Parameter(
            name = "id",
            description = "수정할 일거리의 ID",
            required = true,
            example = "1",
            schema = @Schema(type = "integer")
    )
    @PutMapping("/{id}")
    public ResponseEntity<JobPostDTO> modifyJobPost(@RequestBody JobPostDTO jobPostDTO, @Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok(jobPostService.modify(jobPostDTO));
    }

    @Operation(
            summary = "일거리 삭제",
            description = "ID와 일치하는 일거리를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "일거리를 삭제했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_SUCCESS_DELETE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "일거리 삭제에 실패했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_FAILURE_NOT_REMOVED)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "삭제할 일거리가 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = JOB_POST_FAILURE_NOT_FOUND)
                            )
                    )
            }
    )
    @Parameter(
            name = "id",
            description = "삭제할 일거리의 ID",
            required = true,
            example = "1",
            schema = @Schema(type = "integer")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteJobPost(@Valid @PathVariable("id") Long id) {
        jobPostService.remove(id);
        return ResponseEntity.ok(Map.of("message", "삭제가 완료되었습니다"));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<JobPostDTO>> getJobPostByMember(@PathVariable Long memberId, @ModelAttribute PageRequestDTO pageRequestDTO) {
        Page<JobPostDTO> result = jobPostService.getJobPostByMember(memberId, pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<JobPostDTO>> searchJobPost(@RequestParam("keyword") String keyword, @ModelAttribute PageRequestDTO pageRequestDTO) {
        Page<JobPostDTO> result = jobPostService.searchJobPosts(keyword, pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/showApply")
    public ResponseEntity<List<ApplyDTO>> showApplyForJobPost(@PathVariable("id") Long id) {
        List<ApplyDTO> applyList = applyService.getApplyForJobPost(id);
        return ResponseEntity.ok(applyList);
    }
}
