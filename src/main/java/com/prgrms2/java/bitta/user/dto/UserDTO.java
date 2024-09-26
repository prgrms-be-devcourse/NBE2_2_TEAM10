package com.prgrms2.java.bitta.user.dto;

import com.prgrms2.java.bitta.xenum.Role;
import com.prgrms2.java.bitta.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private String location;
    private Role role;
    private String profilePicture;
    private LocalDateTime createdAt;

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.location = user.getLocation();
        this.role = user.getRole();
        this.profilePicture = user.getProfilePicture();
        this.createdAt = user.getCreatedAt();
    }

    //JWT
    public Map<String, Object> getPayload() {
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("userId", userId);
        payloadMap.put("username", username);
        payloadMap.put("email", email);
        payloadMap.put("role", role);
        return payloadMap;
    }
}
