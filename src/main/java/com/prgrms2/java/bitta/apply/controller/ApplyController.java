package com.prgrms2.java.bitta.apply.controller;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.service.ApplyService;
import com.prgrms2.java.bitta.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.prgrms2.java.bitta.global.constants.ApiResponses.*;

@Tag(name = "지원서 API 컨트롤러", description = "지원서와 관련된 REST API를 제공하는 컨트롤러입니다.")
@RestController
@RequestMapping("api/v1/apply")
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService applyService;

    @Operation(
            summary = "전체 지원서 조회",
            description = "회원의 전체 지원서를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "지원서를 성공적으로 조회했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = APPLY_SUCCESS_READ_ALL)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<ApplyDTO>> findAll(Member member) {
        return ResponseEntity.ok(applyService.readAll(member));
    }

    @Operation(
            summary = "지원서 등록",
            description = "지원서를 등록합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "지원서를 성공적으로 등록했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = APPLY_SUCCESS_REGISTER)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "지원서 등록에 실패했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = APPLY_FAILURE_NOT_REGISTERED)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ApplyDTO> registerApply(@RequestBody ApplyDTO applyDTO) {
        return ResponseEntity.ok(applyService.register(applyDTO));
    }

    @Operation(
            summary = "단일 지원서 조회",
            description = "단일 지원서를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "지원서를 조회했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = APPLY_SUCCESS_READ)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "지원서가 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = APPLY_FAILURE_NOT_FOUND)
                            )
                    )
            }
    )
    @Parameter(
            name = "id",
            description = "조회할 지원서의 ID",
            required = true,
            example = "1",
            schema = @Schema(type = "integer")
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApplyDTO> readApply(@PathVariable("id") Long id) {
        return ResponseEntity.ok(applyService.read(id));
    }

    @Operation(
            summary = "지원서 삭제",
            description = "지원서를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "지원서가 삭제되었습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = APPLY_SUCCESS_DELETE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "지원서 삭제에 실패했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = APPLY_FAILURE_NOT_REMOVED)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "삭제할 지원서가 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = APPLY_FAILURE_NOT_FOUND)
                            )
                    )
            }
    )
    @Parameter(
            name = "id",
            description = "삭제할 지원서의 ID",
            required = true,
            example = "1",
            schema = @Schema(type = "integer")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteApply(@PathVariable("id") Long id) {
        applyService.delete(id);
        return ResponseEntity.ok(Map.of("message", "delete complete"));
    }
}
