package com.example.shop.service;

import com.example.shop.domain.member.Member;
import com.example.shop.domain.member.Role;
import com.example.shop.repository.member.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member register(String email, String rawPassword, String name) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(rawPassword));
        member.setName(name);
        member.setRole(Role.USER);

        return memberRepository.save(member);
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    public Member getByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    public Member updateProfile(String email, String newName) {
        Member member = getByEmail(email);
        member.setName(newName);
        return member;
    }

    public void changePassword(String email, String currentPassword, String newPassword) {
        Member member = getByEmail(email);

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        member.setPassword(passwordEncoder.encode(newPassword));
    }

    public void deleteByEmail(String email) {
        Member member = getByEmail(email);
        memberRepository.delete(member);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
