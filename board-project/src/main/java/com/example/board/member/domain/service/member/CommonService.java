package com.example.board.member.domain.service.member;

import com.example.board.member.dto.MemberInfoDto;
import com.example.board.member.dto.UserUpdateDto;
import com.example.board.member.dto.MemberUpdatePassword;


public interface CommonService {

    //내정보 가져오기
    public MemberInfoDto getMyInfo() throws Exception;

    // 내정보 수정하기
    public void update(UserUpdateDto userUpdateDto) throws Exception;

    // 비밀번호 변경하기
    public void updatePassword(MemberUpdatePassword memberUpdatePassword) throws Exception;
    public void updateFirstPassword(MemberUpdatePassword memberUpdatePassword) throws Exception;
}
