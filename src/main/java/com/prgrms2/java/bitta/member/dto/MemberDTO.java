package com.prgrms2.java.bitta.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "회원 DTO", description = "회원 요청 및 응답에 사용하는 DTO입니다.")
public class MemberDTO {
    @Schema(title = "회원 ID (PK)", description = "회원의 고유 ID 입니다.", example = "1", minimum = "1")
    @Min(value = 1, message = "회원 ID는 0 또는 음수가 될 수 없습니다.")
    private Long id;

    @Schema(title = "아이디", description = "로그인에 사용할 회원 아이디입니다.", example = "username")
    @NotBlank(message = "아이디는 비워둘 수 없습니다.")
    @Size(min = 3, max = 10, message = "아이디는 3 ~ 10자 이하여야 합니다.")
    private String username;

    @Schema(title = "비밀번호", description = "로그인에 사용할 회원 비밀번호입니다.", example = "password")
    @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8 ~ 20자 이하여야 합니다.")
    private String password;

    @Schema(title = "별명", description = "회원의 별명입니다.", example = "Nickname")
    @Size(max = 10, message = "별명은 최대 10자까지 입력할 수 있습니다.")
    private String nickname;

    @Schema(title = "주소", description = "회원의 주소입니다.", example = "경기도 고양시 일산동구 중앙로 1256")
    @Size(max = 100, message = "주소는 최대 100자까지 입력할 수 있습니다.")
    private String address;

    @Schema(title = "프로필 이미지 URL", description = "프로필 이미지의 URL 입니다.", example = "IMAGE_URL")
    private String profileUrl;
}
