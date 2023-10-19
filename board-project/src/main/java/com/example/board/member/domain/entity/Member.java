package com.example.board.member.domain.entity;
import com.example.board.member.domain.entity.code.AccountTypeCode;
import com.example.board.member.domain.entity.code.AffiliationCode;
import com.example.board.member.domain.entity.code.RankCode;
import com.example.board.member.validation.PasswordCheck;
import com.example.board.member.validation.SerialCheck;
import com.example.board.member.validation.UserIdCheck;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@DynamicInsert
@Table(name = "users")
public class Member {
    @Id
    @Column(name = "account_id", length = 50)
    @UserIdCheck
    private String accountId;

    @Column(name = "password", length = 100)
    @PasswordCheck
    private String password;

    @Column(name = "users_name", length = 10)
    private String userName;

    @Column(name = "affiliation_detail", length = 50)
    private String affiliationDetail;

    @Column(name = "serial", length = 15)
    @SerialCheck
    private String serial;

    @Column(name = "is_enabled", length = 3, columnDefinition = "YES")
    private String isEnabled;

    @Column(name = "password_change_flag", columnDefinition = "BOOLEAN default true")
    private Boolean passwordChangeFlag;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "created_by", length = 45)
    private String createdBy;

    @Column(name = "modified_at")
    @CreatedDate
    private Date modifiedAt;

    @Column(name = "modified_by", length = 45)
    private String modifiedBy;

    @ManyToOne
    @JoinColumn(name = "affiliation_code", referencedColumnName = "code")
    private AffiliationCode affiliationCode;

    @ManyToOne
    @JoinColumn(name = "rank_code", referencedColumnName = "code")
    private RankCode rankCode;

    @ManyToOne
    @JoinColumn(name = "account_Type_codes_code", referencedColumnName = "code")
    private AccountTypeCode accountTypeCode;

    @Builder
    public Member(String accountId, String userName,String affiliationDetail, String serial, String password) {
        this.accountId = accountId;
        this.userName = userName;
        this.affiliationDetail = affiliationDetail;
        this.serial = serial;
        this.password = password;
    }
    //password 암호
    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }
    // 비밀번호가 맞는지 확인
    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword){
        return passwordEncoder.matches(checkPassword, getPassword());
    }


    //회원가입시, USER의 권한을 부여하는 메서드
    public void updateFirstLogin(){
        this.passwordChangeFlag = false;
    }
    public void addIsFirstUser(){
        this.passwordChangeFlag = true;
    }
    public void updatePassword(PasswordEncoder passwordEncoder, String password){
        this.password = passwordEncoder.encode(password);
    }
    public void Enabled(){
        this.isEnabled = "YES";
    }
    public void disEnabled(){
        this.isEnabled = "NO";
    }

    public void updateName(String name){
        this.userName = name;
    }
    public void updateBelong(String affiliationDetail){
        this.affiliationDetail = affiliationDetail;
    }
}
