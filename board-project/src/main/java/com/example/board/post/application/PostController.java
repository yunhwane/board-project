package com.example.board.post.application;


import com.example.board.post.domain.service.PostService;
import com.example.board.post.dto.PostPagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")

public class PostController {

    private final PostService postService;

    @GetMapping("/{categoryCode}")
    public PostPagingResponse getPagingList(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @PathVariable String categoryCode
    ){
        return postService.getPagingPostList(page,size,categoryCode);
    }
}
