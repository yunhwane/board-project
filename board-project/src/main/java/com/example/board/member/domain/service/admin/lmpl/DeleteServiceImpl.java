package com.example.board.member.domain.service.admin.lmpl;

import com.example.board.global.exception.CommonErrorCode;
import com.example.board.global.exception.ErrorCode;
import com.example.board.global.exception.GlobalException;
import com.example.board.member.domain.repository.MemberRepository;
import com.example.board.member.domain.service.admin.DeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DeleteServiceImpl implements DeleteService {

    private final MemberRepository memberRepository;
    @Transactional
    @Override
    public void deleteUser(String accountId) throws Exception {
        // 유효성 check
        if(!memberRepository.existsByAccountId(accountId)){
            throw new GlobalException(CommonErrorCode.NOT_FOUND_MEMBER);
        }
        memberRepository.deleteByAccountId(accountId);
    }
}
