package com.example.fitpassserver.domain.member.repository;

import com.example.fitpassserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByPhoneNumber(String phoneNumber);

    boolean existsByLoginId(String loginId);
}
