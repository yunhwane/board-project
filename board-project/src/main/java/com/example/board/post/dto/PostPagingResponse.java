package com.example.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data

public class PostPagingResponse {
    private MetaDto meta;
    private List<PostListDto> content;

    public PostPagingResponse(long totalPage ,long totalElement , long currentPage, long pageSize, List<PostListDto> content) {
        this.meta = new MetaDto(totalPage, totalElement,currentPage,pageSize);
        this.content = content;
    }

}
