package com.example.board.post.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
페이징 처리하기 위한 메타 정보
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetaDto {
    private long totalPages;
    private long totalElements;
    private long currentPage;
    private long pageSize;
}
