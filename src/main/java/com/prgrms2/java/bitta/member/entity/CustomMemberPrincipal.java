<<<<<<<< HEAD:src/main/java/com/prgrms2/java/bitta/user/entity/CustomUserPrincipal.java
package com.prgrms2.java.bitta.user.entity;
========
package com.prgrms2.java.bitta.member.entity;
>>>>>>>> refs/remotes/origin/main:src/main/java/com/prgrms2/java/bitta/member/entity/CustomMemberPrincipal.java

import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public class CustomMemberPrincipal implements Principal {
    private final String email;

    @Override
    public String getName() {
        return email;
    }
}