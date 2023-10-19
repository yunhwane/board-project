package com.example.board.member.domain.repository;

import com.example.board.member.domain.entity.code.AffiliationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AffiliationCodeRepository extends JpaRepository<AffiliationCode,Long> {
    AffiliationCode findByCode(Long code);
    Optional<AffiliationCode> findOptionalByCode(Long code);

}
