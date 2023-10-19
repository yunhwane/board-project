package com.example.board.member.domain.service.admin;

import com.example.board.member.domain.entity.Member;
import com.example.board.member.dto.MemberInfoDto;

import java.util.List;
import java.util.Map;

public interface RetrieveService {

    public Map<String, List<MemberInfoDto>> GetAllUser() throws Exception;
    public MemberInfoDto getOneUser(String accountId) throws Exception;
}
