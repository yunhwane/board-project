package com.example.board.member.dto;


import com.example.board.member.domain.entity.Member;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoDto {
    private String account_id;
    private String role;
    private String userName;
    private String affiliation;
    private String affiliation_detail;
    private String rank;
    private String serial;
    private String isEnabled;


    @Builder
    public MemberInfoDto(Member member) {
        this.account_id = member.getAccountId();
        this.role = member.getAccountTypeCode().getCodeName();
        this.userName = member.getUserName();
        this.affiliation = member.getAffiliationCode().getCodeName();
        this.affiliation_detail = member.getAffiliationDetail();
        this.rank = member.getRankCode().getCodeName();
        this.serial = member.getSerial();
        this.isEnabled = member.getIsEnabled();
    }

}
