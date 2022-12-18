package com.example.dating_backend.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum Role implements GrantedAuthority {

    ADMIN("ADMIN", "관리자"),
    USER("USER", "사용자");

    private final String key;
    private final String title;

    @Override
    public String getAuthority() {
        return key;
    }
}