package com.example.board.member.dto;


import com.example.board.member.validation.PasswordCheck;
import com.example.board.member.validation.SerialCheck;
import com.example.board.member.validation.UserIdCheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
public class UserUpdateDto {

    @UserIdCheck
    private String account_Id;
    private String userName;
    private String affiliationDetail;
    private Long affiliation;
    private Long rank;
    @SerialCheck
    private String serial;
   /*
    @Builder
    public UserUpdateDto(String accountId,String username, String name, String belong, String rank, String serial) {
        this.accountId = accountId;
        this.username = username;
        this.name = name;
        this.belong = belong;
        this.rank = rank;
        this.serial = serial;
    }
    */
}
