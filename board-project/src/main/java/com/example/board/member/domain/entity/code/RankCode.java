package com.example.board.member.domain.entity.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "rank_codes")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RankCode {

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

