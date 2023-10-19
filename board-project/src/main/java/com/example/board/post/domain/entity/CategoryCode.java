package com.example.board.post.domain.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "category_codes")
@Entity
@Getter
public class CategoryCode {

    @Id
    @Column(name = "code")
    private Long code;

    @Column(name = "code_name", length = 45)
    private String codeName;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "create_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "create_by", length = 45)
    private String createdBy;

    @Column(name = "modified_at")
    @CreatedDate
    private Date modifiedAt;

    @Column(name = "modified_by", length = 45)
    private String modifiedBy;
}
