package com.example.board.member.application;


import com.example.board.member.domain.service.member.CommonService;
import com.example.board.member.dto.MemberInfoDto;
import com.example.board.member.dto.MemberUpdatePassword;
import com.example.board.member.dto.UserUpdateDto;
import com.example.board.member.dto.response.ResponseDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/*
sample controller
 */
@Api(value = "MemberController")
@SwaggerDefinition(tags = {@Tag(name = "MemberController",description = "MEMBER 공통 컨트롤러")})
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class MemberController {

    private final CommonService commonService;
    @ApiOperation(value = "내 정보 가져오기", notes = "로그인 된 내 정보를 가져온다.")
    @GetMapping("/me")
    public MemberInfoDto  getMyInfo() throws Exception{
          return commonService.getMyInfo();
    }

    @ApiOperation(value = "내 정보 수정하기", notes = "내 정보를 수정한다.")
    @PatchMapping("/update")
    public ResponseEntity<ResponseDto> updateMyInfo(@RequestBody @Valid UserUpdateDto userUpdateDto) throws Exception{
        commonService.update(userUpdateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(201,"내 정보 수정이 완료되었습니다."));
    }

    @ApiOperation(value = "내 정보 비밀번호 변경하기",notes = "내 비밀번호를 변경한다.")
    @PatchMapping("/update/password")
    public ResponseEntity<ResponseDto> updateMyPassword(@RequestBody @Valid MemberUpdatePassword memberUpdatePassword) throws Exception{
        commonService.updatePassword(memberUpdatePassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(201,"비밀번호 변경이 완료되었습니다."));
    }

    @ApiOperation(value = "첫 로그인 패스워드 변경",notes = "첫 로그인 시 패스워드 변경한다.")
    @PatchMapping("/update/first")
    public ResponseEntity<?> updateFirstPassword(@ApiParam(value = "update 비밀번호") @RequestBody @Valid MemberUpdatePassword memberUpdatePassword) throws Exception{
        commonService.updateFirstPassword(memberUpdatePassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(201,"비밀번호 변경이 완료되었습니다."));
    }

}