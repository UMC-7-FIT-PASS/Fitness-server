package com.example.fitpassserver.domain.coin.repository;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    List<Coin> findAllByMemberIsAndCountGreaterThanOrderByExpiredDateAsc(Member member, Long count);
}