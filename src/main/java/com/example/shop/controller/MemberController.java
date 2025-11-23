package com.example.shop.controller;

import com.example.shop.controller.dto.ChangePasswordRequest;
import com.example.shop.controller.dto.MemberRegisterRequest;
import com.example.shop.controller.dto.MemberResponse;
import com.example.shop.controller.dto.MemberUpdateRequest;
import com.example.shop.domain.member.Member;
import com.example.shop.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@Valid @RequestBody MemberRegisterRequest request) {
        Member member = memberService.register(request.getEmail(), request.getPassword(), request.getName());
        return ResponseEntity.ok(member.getId().toString());
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user.getUsername());

    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email) {
        boolean exists = memberService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @PatchMapping("/me")
    public ResponseEntity<MemberResponse> updateMe(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody MemberUpdateRequest request) {
        Member updated = memberService.updateProfile(user.getUsername(), request.getName());
        return ResponseEntity.ok(MemberResponse.from(updated));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordRequest request) {
        memberService.changePassword(
                user.getUsername(),
                request.getCurrentPassword(),
                request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal User user) {
        memberService.deleteByEmail(user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
