package com.prgrms2.java.bitta.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberRequestDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Join {
        private String username;
        private String password;
        private String nickname;
        private String address;

        @Builder
        public Join(String username, String password, String nickname, String address) {
            this.username = username;
            this.password = password;
            this.nickname = nickname;
            this.address = address;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class ChangePassword {
        private String beforePassword;
        private String afterPassword;

        @Builder
        public ChangePassword(String beforePassword, String afterPassword) {
            this.beforePassword = beforePassword;
            this.afterPassword = afterPassword;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Modify {
        private String nickname;
        private String address;

        @Builder
        public Modify(String nickname, String address) {
            this.nickname = nickname;
            this.address = address;
        }
    }
}