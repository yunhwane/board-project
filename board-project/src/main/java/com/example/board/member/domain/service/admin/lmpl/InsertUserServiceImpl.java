package com.example.board.member.domain.service.admin.lmpl;

import com.example.board.global.Util.ExcelUtil;
import com.example.board.global.exception.CommonErrorCode;
import com.example.board.global.exception.GlobalException;
import com.example.board.member.domain.entity.Member;
import com.example.board.member.domain.entity.code.AccountTypeCode;
import com.example.board.member.domain.entity.code.AffiliationCode;
import com.example.board.member.domain.entity.code.RankCode;
import com.example.board.member.domain.repository.AccountTypeCodeRepository;
import com.example.board.member.domain.repository.AffiliationCodeRepository;
import com.example.board.member.domain.repository.MemberRepository;
import com.example.board.member.domain.repository.RankCodeRepository;
import com.example.board.member.domain.service.admin.InsertUserService;
import com.example.board.member.dto.SignUpDto;
import com.example.board.member.validation.SerialValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class InsertUserServiceImpl implements InsertUserService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AccountTypeCodeRepository accountTypeCodeRepository;
    private final RankCodeRepository rankCodeRepository;
    private final AffiliationCodeRepository affiliationCodeRepository;


    private final ExcelUtil excelUtil;
    private final SerialValidator validator;

    @Override
    @Transactional
    public void saveUser(SignUpDto signUpDto) throws Exception {
        // ID 중복 check
        if(memberRepository.existsByAccountId(signUpDto.getAccountId())){
            throw new GlobalException(CommonErrorCode.ALREADY_EXIST_USERNAME);
        }
        // SERIAL 중복 check
        if(memberRepository.existsBySerial(signUpDto.getSerial())){
            throw new GlobalException(CommonErrorCode.SERIAL_DUPLICATED);
        }
        /* 기본 정보 등록 */
        Member newMember = new Member();
        newMember = signUpDto.toEntity();
        newMember.encodePassword(passwordEncoder);
        newMember.setIsEnabled("YES");
        newMember.addIsFirstUser();
        // 소속 코드 및 계정 유형 코드를 실제 코드 테이블에서 조회하여 설정
        AffiliationCode selectedAffiliation = affiliationCodeRepository.findByCode(signUpDto.getAffiliation());
        AccountTypeCode selectedAccountType = accountTypeCodeRepository.findByCode(signUpDto.getRole());
        RankCode selectedRank = rankCodeRepository.findByCode(signUpDto.getRank());
        ;
        newMember.setAffiliationCode(selectedAffiliation);
        newMember.setAccountTypeCode(selectedAccountType);
        newMember.setRankCode(selectedRank);
        // 저장하기
        memberRepository.save(newMember);
    }


    @Override
    @Transactional
    public void addExcel(MultipartFile multipartFile) throws Exception {
        // 파일이 존재하지 않는 경우
        if (multipartFile.isEmpty()) {
            throw new GlobalException(CommonErrorCode.INPUT_VALUE_INVALID);
        }

        // 확장자 유효성 검사
        String excelType = multipartFile.getContentType();
        log.info(excelType);
        if (excelType != null && excelType.equals("application/vnd.ms-excel") || excelType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            log.info("들어왔다 오바.");
            List<Member> memberExcelDataList = new ArrayList<>();
            List<Map<String, Object>> listMap = null;
            listMap = excelUtil.getListData(multipartFile, 1, 7);

            for (Map<String, Object> map : listMap) {
                Member newMember = new Member();
                // Map Excel data to your domain model
                // 아이디 중복 처리
                String accountId = map.get("0").toString();
                // ID
                if (memberRepository.existsByAccountId(accountId)) {
                    throw new GlobalException(CommonErrorCode.ALREADY_EXIST_USERNAME);
                }
                String serial = map.get("5").toString();

                if (memberRepository.existsBySerial(serial)) {
                    throw new GlobalException(CommonErrorCode.SERIAL_DUPLICATED);
                }
                newMember.setAccountId(accountId);
                newMember.setPassword(String.valueOf(map.get("1")));
                newMember.setUserName(String.valueOf(map.get("2")));
                newMember.encodePassword(passwordEncoder);
                Long affiliationCode = Long.parseLong(String.valueOf(map.get("3")));
                AffiliationCode code = affiliationCodeRepository.findByCode(affiliationCode);
                if(code!=null) {
                    newMember.setAffiliationCode(code);
                } else throw new GlobalException(CommonErrorCode.INPUT_VALUE_INVALID);
                log.info("여기도 통과임1");
                newMember.setAffiliationDetail(String.valueOf(map.get("4")));
                newMember.setSerial(serial);
                log.info("여기도 통과임2");
                newMember.setRankCode(rankInputData(String.valueOf(map.get("6"))));
                log.info("여기도 통과임3");
                newMember.setAccountTypeCode(extractAccountType(String.valueOf(map.get("7"))));
                log.info("여기도 통과임4");
                log.info(serial);

                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                Set<ConstraintViolation<Member>> violations = validator.validate(newMember);
                if (!violations.isEmpty()) {
                    throw new GlobalException(CommonErrorCode.INPUT_VALUE_INVALID);
                }


                memberExcelDataList.add(newMember);

            }
            memberRepository.saveAll(memberExcelDataList);
        } else{
            throw new GlobalException(CommonErrorCode.INPUT_VALUE_INVALID);
        }
    }
    //메서드
    public RankCode rankInputData(String rankCodeName){
        return rankCodeRepository.findByCodeName(rankCodeName.trim()).orElseThrow(
                () -> new GlobalException(CommonErrorCode.INPUT_VALUE_INVALID));
    }

    public AccountTypeCode extractAccountType(String type){
       String accountTypeCodeName = accountStringConvertor(type).trim();
       log.info("여기가 문제네...");
       log.info(accountTypeCodeName);
       return accountTypeCodeRepository.findByCodeName(accountTypeCodeName).orElseThrow(
               () -> new GlobalException(CommonErrorCode.INPUT_VALUE_INVALID)
       );
    }

    public String accountStringConvertor(String accountType){
        log.info(accountType);
        switch (accountType.trim()) {
            case "훈련생":
                log.info("여기도 들어오지 ?");
                return "TRAINEE";
            case "교관":
                return "INSTRUCTOR";
            default:
                log.info("혹시 여기도 ?");
                throw new IllegalArgumentException("Invalid account code name:" + accountType);
        }
    }
}
