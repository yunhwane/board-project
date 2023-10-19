package com.example.board.post.application;


import com.example.board.member.dto.response.ResponseDto;
import com.example.board.post.domain.service.PostService;
import com.example.board.post.dto.PostRequest;
import com.example.board.post.dto.PostDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notice")
public class AdminPostController {

    private final PostService postService;

    // 게시물 등록
    @PostMapping("/insert")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postInputDto){
        postService.noticeInputData(postInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(201,"공지사항 등록이 완료되었습니다."));
    }



    // 상세 조회
    @GetMapping("/{id}")
    public PostDetailResponse getNoticeDetails(@PathVariable Long id){
        return postService.getNoticeDetail(id);
    }

    // 목록 조회 페이징


}
