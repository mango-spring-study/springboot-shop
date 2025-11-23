package com.example.shop.security;

import com.example.shop.domain.member.Member;
import com.example.shop.repository.member.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // ROLE_ 접두어를 Spring이 붙이는 패턴 고려
        String role = "ROLE_" + member.getRole().name();

        return new User(member.getEmail(), member.getPassword(),
                List.of(new SimpleGrantedAuthority(role)));
    }
}
