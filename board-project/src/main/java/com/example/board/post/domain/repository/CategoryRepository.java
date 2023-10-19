package com.example.board.post.domain.repository;

import com.example.board.post.domain.entity.CategoryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryCode,Long> {
    Optional<CategoryCode> findByCodeName(String codeName);
    Optional<CategoryCode> findByCode(Long code);
}
