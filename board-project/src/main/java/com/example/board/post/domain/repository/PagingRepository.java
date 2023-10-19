package com.example.board.post.domain.repository;

import com.example.board.post.domain.entity.Board;
import com.example.board.post.domain.entity.CategoryCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagingRepository extends PagingAndSortingRepository<Board,Long> {
    Page<Board> findByCategoryCode_CodeName(String categoryCode_codeName, Pageable pageable);
}
