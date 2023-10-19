package com.example.board.post.domain.entity;


import com.example.board.member.domain.entity.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "boards")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String content;

    @Column(name = "view", columnDefinition = "int default 0")
    private int view;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "account_id")
    private Member author;

    @ManyToOne
    @JoinColumn(name = "category_code", referencedColumnName = "code")
    private CategoryCode categoryCode;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "modified_at")
    private Date modifiedAt;

    @Builder
    public Board(String title,String content){
        this.title = title;
        this.content = content;
    }

}
