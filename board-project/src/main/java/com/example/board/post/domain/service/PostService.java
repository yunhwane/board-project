package com.example.board.post.domain.service;

import com.example.board.post.domain.entity.Board;
import com.example.board.post.dto.PostPagingResponse;
import com.example.board.post.dto.PostRequest;
import com.example.board.post.dto.PostDetailResponse;
import org.springframework.data.domain.Page;

public interface PostService {
    public void noticeInputData(PostRequest postInputDto);
    public PostPagingResponse getPagingPostList(int page, int size,String categoryCodeName);
    public PostDetailResponse getNoticeDetail(Long id);
}
