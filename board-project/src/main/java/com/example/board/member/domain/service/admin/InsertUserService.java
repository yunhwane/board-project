package com.example.board.member.domain.service.admin;

import com.example.board.member.dto.SignUpDto;
import org.springframework.web.multipart.MultipartFile;

public interface InsertUserService {
    public void saveUser(SignUpDto SignUpDto) throws Exception;
    public void addExcel(MultipartFile multipartFile) throws Exception;

}
