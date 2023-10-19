package com.example.board.member.domain.repository;

import com.example.board.member.domain.entity.code.RankCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankCodeRepository extends JpaRepository<RankCode,Long> {
    RankCode findByCode(Long code);
    Optional<RankCode> findByCodeName(String rankCodeName);
}
