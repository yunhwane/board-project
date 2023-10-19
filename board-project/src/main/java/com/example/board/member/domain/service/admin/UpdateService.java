package com.example.board.member.domain.service.admin;

import com.example.board.member.dto.UserUpdateDto;

public interface UpdateService {
    void update(UserUpdateDto UserUpdateDto,String accountId) throws Exception;
    void activeAccount(String accountId) throws Exception;
}
