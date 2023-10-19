package com.example.board.post.domain.service;
import com.example.board.global.Util.SecurityUtils;
import com.example.board.global.exception.CommonErrorCode;
import com.example.board.global.exception.GlobalException;
import com.example.board.member.domain.entity.Member;
import com.example.board.member.domain.repository.MemberRepository;
import com.example.board.post.domain.entity.Board;
import com.example.board.post.domain.entity.CategoryCode;
import com.example.board.post.domain.repository.BoardRepository;
import com.example.board.post.domain.repository.CategoryRepository;
import com.example.board.post.domain.repository.PagingRepository;
import com.example.board.post.dto.PostListDto;
import com.example.board.post.dto.PostPagingResponse;
import com.example.board.post.dto.PostRequest;
import com.example.board.post.dto.PostDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final PagingRepository pagingRepository;

    // 공지사항

    @Override
    public void noticeInputData(PostRequest postRequest) {
        Member findMember = memberRepository.findByAccountId(SecurityUtils.getLoginUsername()).orElseThrow(
                () -> new GlobalException(CommonErrorCode.NOT_FOUND_MEMBER)
        );
        Board board = postRequest.toEntity();
        board.setAuthor(findMember);

        CategoryCode categoryCode = categoryRepository.findByCode(postRequest.getCategoryCode()).orElseThrow(
                () -> new GlobalException(CommonErrorCode.NOT_FOUND_CATEGORY)
        );
        board.setCategoryCode(categoryCode);
        boardRepository.save(board);
    }

    @Override
    public PostPagingResponse getPagingPostList(int page, int size, String categoryCode) {
        // 최신순 정렬
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Board> boardPage;

        if(categoryCode.equals("all")){
            boardPage = pagingRepository.findAll(pageable);
        }else{

            CategoryCode category = categoryRepository.findByCodeName(categoryCode).orElseThrow(
                    () -> new GlobalException(CommonErrorCode.NOT_FOUND_CATEGORY)
            );

            boardPage = pagingRepository.findByCategoryCode_CodeName(categoryCode,pageable);
        }
        List<PostListDto> boardList = boardPage.stream().map(this::convertToDto).collect(Collectors.toList());

        return new PostPagingResponse(boardPage.getTotalPages(),boardPage.getTotalElements(),boardPage.getNumber(),boardPage.getSize(),boardList);
    }
    public PostListDto convertToDto(Board board){
        return PostListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .author(board.getAuthor().getUserName())
                .createDate(board.getCreatedAt())
                .build();
    }

    @Override
    public PostDetailResponse getNoticeDetail(Long id) {

        Optional<Board> findBoard = boardRepository.findById(id);
        PostDetailResponse postDetailResponse = new PostDetailResponse();
        postDetailResponse.setContent(findBoard.get().getContent());
        return postDetailResponse;

    }


}
