package com.example.board.member.domain.repository;

import com.example.board.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    // 군번 체크
    Boolean existsByAccountId(String username);
    Boolean existsBySerial(String serial);
   // List<Member> findAllByRoleIn(List<Role> role);
    // 중복 체크
    Optional<Member> findByAccountId(String accountId);
    List<Member> findAllByAccountTypeCode_Code(long accountTypeCode);



    void deleteByAccountId(String accountId);

   // @Query("SELECT m FROM Member m WHERE m.role IN :roles")
   // List<Member> findMembersByRoles(@Param("role") List<Role> role);
}
