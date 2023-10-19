package com.example.board.post.dto;


import com.example.board.post.domain.entity.Board;
import com.example.board.post.domain.entity.CategoryCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    private String title;
    private String content;
    //category
    private Long categoryCode;


    public Board toEntity(){
        return Board.builder()
                .title(title)
                .content(content)
                .build();
    }
}
