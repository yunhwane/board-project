package com.example.board.member.dto;


import com.example.board.member.validation.PasswordCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdatePassword {
    @PasswordCheck
    String checkPassword;
    @PasswordCheck
    String updatePassword;
}
