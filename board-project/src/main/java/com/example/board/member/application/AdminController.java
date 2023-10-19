package com.example.board.member.application;


import com.example.board.global.exception.ErrorResponse;
import com.example.board.member.domain.service.admin.DeleteService;
import com.example.board.member.domain.service.admin.InsertUserService;
import com.example.board.member.domain.service.admin.RetrieveService;
import com.example.board.member.domain.service.admin.UpdateService;
import com.example.board.member.dto.MemberInfoDto;
import com.example.board.member.dto.SignUpDto;
import com.example.board.member.dto.UserUpdateDto;
import com.example.board.member.dto.response.ResponseDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "관리자 컨트롤러 매핑 url")
@SwaggerDefinition(tags = {@Tag(name = "Admin API",description = "관리자 API")})
@Slf4j
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final DeleteService deleteService;
    private final RetrieveService retrieveService;
    private final InsertUserService insertUserService;
    private final UpdateService updateService;

    // 전체 회원 목록 조회
    @ApiOperation(value = "전체 회원 목록 조회", notes = "전체 회원 목록을 조회한다.")
    @GetMapping()
    public Map<String, List<MemberInfoDto>> retrieveAllUser() throws Exception{
        return retrieveService.GetAllUser();
    }

    // 특정 회원 조회
    @ApiOperation(value = "특정 회원 목록 조회",notes = "특정 회원 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 조회된 경우"),
            @ApiResponse(code = 404, message = "회원을 찾을 수 없는 경우", response = ErrorResponse.class),
    })
    @GetMapping("{account_id}")
    public MemberInfoDto retrieveOneUser(@PathVariable String account_id) throws Exception{
        return retrieveService.getOneUser(account_id);
    }


    // 회원 등록 1명
    @PostMapping("/insert")
    @ApiOperation(value = "회원 등록 1명",notes = "회원 1명을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created Account"),
            @ApiResponse(code = 409, message = "아이디가 이미 등록되어 있는 경우", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "군번이 이미 등록되어 있는 경우", response = ErrorResponse.class),
    })
    public ResponseEntity<ResponseDto> InsertUser(@RequestBody @Valid SignUpDto dto) throws Exception{
        insertUserService.saveUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(201,"성공적으로 등록되었습니다."));
    }
    // 회원 정보 수정
    @ApiOperation(value = "회원 수정",notes = "특정 유저 회원 정보를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Update Account"),
            @ApiResponse(code = 404, message = "회원을 찾을 수 없는 경우", response = ErrorResponse.class)
    })
    @PatchMapping ("/update/{accountId}")
    public ResponseEntity<ResponseDto> UpdateUser(@RequestBody @Valid UserUpdateDto dto, @PathVariable String accountId) throws Exception{
        updateService.update(dto,accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(201,"계정 정보 변경이 완료되었습니다."));
    }
    // 회원 삭제
    @ApiOperation(value = "특정 회원 삭제", notes = "회원을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Delete Account"),
            @ApiResponse(code = 404, message = "회원을 찾을 수 없는 경우", response = ErrorResponse.class)
    })
    @DeleteMapping("/delete/{account_id}")
    public ResponseEntity<ResponseDto> DeleteUser(@PathVariable String account_id) throws Exception{
        deleteService.deleteUser(account_id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(200,"성공적으로 계정이 삭제되었습니다."));
    }

    @ApiOperation(value = "엑셀 다운로드", notes = "엑셀로 회원을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "회원 일괄 등록이 완료되었습니다."),
            @ApiResponse(code = 404, message = "회원을 찾을 수 없는 경우", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "액셀 파일에 아무것도 없는 경우"),
            @ApiResponse(code = 404, message = "액셀 파일 확장자가 아닌 경우", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "아이디가 이미 등록되어 있는 경우", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "군번이 이미 등록되어 있는 경우", response = ErrorResponse.class)
    })
    @PostMapping("/insert/all")
    public ResponseEntity<ResponseDto> addExcel(@Valid @RequestParam(value = "file") MultipartFile multipartFile) throws Exception{
        // 파일이 존재하지 않는 경우
        if (multipartFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseDto(400,"파일이 존재하지 않습니다."));
        }
            // 엑셀 파일 처리 로직 구현
            // 예: 엑셀 파일을 읽어서 데이터베이스에 등록하는 작업 수행
            insertUserService.addExcel(multipartFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(201,"회원 일괄 등록이 완료되었습니다."));
    }



     /*
       @Mapping("/admin/re")
    public ResponseEntity<?> insertAdmin(@RequestBody SignUpDto signUpDto) throws Exception{
        insertUserService.saveAdmin(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Account");
    }
      */


    @ApiOperation(value = "비활성화", notes = "아이디를 비활성화 한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Update Account disable"),
            @ApiResponse(code = 409, message = "비활성화 된 경우", response = ErrorResponse.class)
    })

    @PatchMapping("/disable/{account_id}")
    public ResponseEntity<ResponseDto> updateIsDisabled(@PathVariable String account_id) throws Exception{
        updateService.activeAccount(account_id);
          return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(201,"비활성화/활성화 처리 완료되었습니다."));
    }
}
