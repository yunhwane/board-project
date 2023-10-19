package com.example.board.member.domain.repository;

import com.example.board.member.domain.entity.code.AccountTypeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeCodeRepository extends JpaRepository<AccountTypeCode,Long> {
    AccountTypeCode findByCode(Long code);
    Optional<AccountTypeCode> findByCodeName(String accountType);
}
