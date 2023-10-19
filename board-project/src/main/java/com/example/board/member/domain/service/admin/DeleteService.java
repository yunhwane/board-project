package com.example.board.member.domain.service.admin;

public interface DeleteService {
    // 유저 삭제
    public void deleteUser(String accountId) throws Exception;
}
