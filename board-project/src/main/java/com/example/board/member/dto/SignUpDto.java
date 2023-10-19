package com.example.board.member.dto;

import com.example.board.member.domain.entity.Member;
import com.example.board.member.validation.PasswordCheck;
import com.example.board.member.validation.SerialCheck;
import com.example.board.member.validation.UserIdCheck;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto {

    @UserIdCheck
    private String accountId;
    @NotBlank
    private String userName;
    @NotBlank
    private String affiliationDetail;
    @SerialCheck
    private String serial;
    @PasswordCheck
    private String password;

    private Long affiliation;
    private Long role;
    private Long rank;

    //권한 변경 용
    public Member toEntity() {
        return Member.builder()
                .accountId(accountId)
                .userName(userName)
                .affiliationDetail(affiliationDetail)
                .serial(serial)
                .password(password)
                .build();
    }
}
