package com.example.board.member.domain.service.admin.lmpl;


import com.example.board.global.exception.CommonErrorCode;
import com.example.board.global.exception.GlobalException;
import com.example.board.member.domain.entity.Member;
import com.example.board.member.domain.repository.MemberRepository;
import com.example.board.member.domain.service.admin.RetrieveService;
import com.example.board.member.dto.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrieveServiceImpl implements RetrieveService {

    private final MemberRepository memberRepository;
    //회원 목록 전체 조회
    @Override
    public Map<String, List<MemberInfoDto>> GetAllUser() throws Exception {

        List<Member> users = memberRepository.findAllByAccountTypeCode_Code(10);
        List<Member> instructors = memberRepository.findAllByAccountTypeCode_Code(20);


        List<MemberInfoDto> userDto = users.stream().map(MemberInfoDto::new).collect(Collectors.toList());
        List<MemberInfoDto> instructorDto = instructors.stream().map(MemberInfoDto::new).collect(Collectors.toList());

        Map<String, List<MemberInfoDto>> resultMap = new HashMap<>();

        resultMap.put("users", userDto);
        resultMap.put("instructors", instructorDto);

        return resultMap;
    }

    @Override
    public MemberInfoDto getOneUser(String accountId) throws Exception {
         Member member = memberRepository.findByAccountId(accountId).orElseThrow(() -> new GlobalException(CommonErrorCode.NOT_FOUND_MEMBER));
         return MemberInfoDto.builder()
                 .account_id(member.getAccountId())
                 .userName(member.getUserName())
                 .serial(member.getSerial())
                 .affiliation(member.getAffiliationCode().getCodeName())
                 .affiliation_detail(member.getAffiliationDetail())
                 .rank(member.getRankCode().getCodeName())
                 .role(member.getAccountTypeCode().getCodeName())
                 .isEnabled(member.getIsEnabled())
                 .build();
    }
}
