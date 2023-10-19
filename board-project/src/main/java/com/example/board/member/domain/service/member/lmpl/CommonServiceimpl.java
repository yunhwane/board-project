package com.example.board.member.domain.service.member.lmpl;

import com.example.board.global.Util.SecurityUtils;
import com.example.board.global.exception.CommonErrorCode;
import com.example.board.global.exception.GlobalException;
import com.example.board.member.domain.entity.Member;
import com.example.board.member.domain.entity.code.AffiliationCode;
import com.example.board.member.domain.entity.code.RankCode;
import com.example.board.member.domain.repository.AccountTypeCodeRepository;
import com.example.board.member.domain.repository.AffiliationCodeRepository;
import com.example.board.member.domain.repository.MemberRepository;
import com.example.board.member.domain.repository.RankCodeRepository;
import com.example.board.member.domain.service.member.CommonService;
import com.example.board.member.dto.MemberInfoDto;
import com.example.board.member.dto.UserUpdateDto;
import com.example.board.member.dto.MemberUpdatePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonServiceimpl implements CommonService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountTypeCodeRepository accountTypeCodeRepository;
    private final RankCodeRepository rankCodeRepository;
    private final AffiliationCodeRepository affiliationCodeRepository;



    @Override
    public MemberInfoDto getMyInfo() throws Exception {
        Member findMember = memberRepository.findByAccountId(SecurityUtils.getLoginUsername()).orElseThrow(
                () -> new GlobalException(CommonErrorCode.NOT_FOUND_MEMBER)
        );
        return new MemberInfoDto(findMember);
    }

    @Override
    public void update(UserUpdateDto userUpdateDto) throws Exception {
        // update 시 id로 데이터 확인
        Member persistence = memberRepository.findByAccountId(SecurityUtils.getLoginUsername()).orElseThrow(
                () -> new GlobalException(CommonErrorCode.NOT_FOUND_MEMBER));
        /*
         update persistence
        */
        if(userUpdateDto.getAccount_Id() != null) persistence.setAccountId(userUpdateDto.getAccount_Id());
        if(userUpdateDto.getUserName() != null) persistence.setUserName(userUpdateDto.getUserName());
        if(userUpdateDto.getSerial()!=null) persistence.setSerial(userUpdateDto.getSerial());
        if(userUpdateDto.getAffiliationDetail()!=null) persistence.setAffiliationDetail(userUpdateDto.getAffiliationDetail());
        if(userUpdateDto.getAffiliationDetail()!=null) persistence.setAffiliationDetail(userUpdateDto.getAffiliationDetail());
        if(userUpdateDto.getUserName() != null) persistence.setUserName(userUpdateDto.getUserName());
        AffiliationCode selectedAffiliation = null;
        RankCode selectedRank = null;
        if(userUpdateDto.getAffiliation()!=null) {
            selectedAffiliation = affiliationCodeRepository.findByCode(userUpdateDto.getAffiliation());
        }

        if(userUpdateDto.getRank()!=null){
            selectedRank = rankCodeRepository.findByCode(userUpdateDto.getRank());
        }
        memberRepository.save(persistence);
    }

    @Override
    public void updatePassword(MemberUpdatePassword memberUpdatePassword) throws Exception {
        //비밀번호 변경할 로그인 아이디 찾기.
        Member member = memberRepository.findByAccountId(SecurityUtils.getLoginUsername()).orElseThrow(() -> new GlobalException(CommonErrorCode.NOT_FOUND_MEMBER));

        // 비밀번호 유효성 체크
        if(!member.matchPassword(passwordEncoder, memberUpdatePassword.getCheckPassword())){
            throw new GlobalException(CommonErrorCode.WRONG_PASSWORD);
        }

        member.updatePassword(passwordEncoder,memberUpdatePassword.getUpdatePassword());

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void updateFirstPassword(MemberUpdatePassword memberUpdatePassword) throws Exception {
        //비밀번호 변경할 로그인 아이디 찾기.
        Member member = memberRepository.findByAccountId(SecurityUtils.getLoginUsername()).orElseThrow(() -> new GlobalException(CommonErrorCode.NOT_FOUND_MEMBER));
        if(!member.matchPassword(passwordEncoder, memberUpdatePassword.getCheckPassword())){
            throw new GlobalException(CommonErrorCode.WRONG_PASSWORD);
        }
        member.updatePassword(passwordEncoder,memberUpdatePassword.getUpdatePassword());
        // 첫 로그인 데이터 변경
        member.setPasswordChangeFlag(false);
        memberRepository.save(member);
    }
}
