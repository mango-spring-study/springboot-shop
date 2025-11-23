package com.example.shop.controller.dto;

import com.example.shop.domain.member.Role;

public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private Role role;

    public MemberResponse(Long id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public static MemberResponse from(com.example.shop.domain.member.Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getRole());
    }

    // getter only 정도면 충분
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
