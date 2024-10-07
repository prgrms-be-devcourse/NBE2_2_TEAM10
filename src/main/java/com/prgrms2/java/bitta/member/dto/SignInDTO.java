package com.prgrms2.java.bitta.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(title = "로그인 DTO", description = "로그인 요청에 사용하는 DTO입니다.")
public class SignInDTO {

    @Schema(title = "회원 아이디", description = "로그인에 사용할 아이디입니다.", example = "username")
    private String username;

    @Schema(title = "회원 비밀번호", description = "로그인에 사용할 비밀번호입니다.", example = "password")
    private String password;
}
