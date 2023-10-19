package com.example.board.member.domain.service.admin.lmpl;

import com.example.board.global.exception.CommonErrorCode;
import com.example.board.global.exception.GlobalException;
import com.example.board.member.domain.entity.Member;
import com.example.board.member.domain.entity.code.AffiliationCode;
import com.example.board.member.domain.entity.code.RankCode;
import com.example.board.member.domain.repository.AccountTypeCodeRepository;
import com.example.board.member.domain.repository.AffiliationCodeRepository;
import com.example.board.member.domain.repository.MemberRepository;
import com.example.board.member.domain.repository.RankCodeRepository;
import com.example.board.member.domain.service.admin.UpdateService;
import com.example.board.member.dto.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UpdateServiceImpl implements UpdateService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountTypeCodeRepository accountTypeCodeRepository;
    private final RankCodeRepository rankCodeRepository;
    private final AffiliationCodeRepository affiliationCodeRepository;

    @Override
    @Transactional
    public void update(UserUpdateDto userUpdateDto,String accountId) throws Exception {
        // update 시 id로 데이터 확인
        Member persistence = memberRepository.findByAccountId(accountId).orElseThrow(() -> new GlobalException(CommonErrorCode.NOT_FOUND_MEMBER));
        /*
         update persistence
        */

        if(userUpdateDto.getAccount_Id() != null) persistence.setAccountId(userUpdateDto.getAccount_Id());
        if(userUpdateDto.getUserName() != null) persistence.setUserName(userUpdateDto.getUserName());
        if(userUpdateDto.getSerial()!=null) persistence.setSerial(userUpdateDto.getSerial());
        if(userUpdateDto.getAffiliationDetail()!=null) persistence.setAffiliationDetail(userUpdateDto.getAffiliationDetail());

        AffiliationCode selectedAffiliation = null;
        RankCode selectedRank = null;
        if(userUpdateDto.getAffiliation()!=null) {
            selectedAffiliation = affiliationCodeRepository.findByCode(userUpdateDto.getAffiliation());
        }

        if(userUpdateDto.getRank()!=null){
            selectedRank = rankCodeRepository.findByCode(userUpdateDto.getRank());
        }
        persistence.setAffiliationCode(selectedAffiliation);
        persistence.setRankCode(selectedRank);

        // update
        memberRepository.save(persistence);
    }

    @Override
    @Transactional
    public void activeAccount(String accountId) throws Exception {

        Member persistence = memberRepository.findByAccountId(accountId).orElseThrow(() -> new GlobalException(CommonErrorCode.NOT_FOUND_MEMBER));

        if(persistence.getIsEnabled().equals("YES")){
            persistence.setIsEnabled("NO");
        }else{
            persistence.setIsEnabled("YES");
        }
        memberRepository.save(persistence);
    }

}
