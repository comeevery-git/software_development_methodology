package app.member.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.member.application.dto.CreateMemberDto;
import app.member.application.dto.UpdateMemberDto;
import app.member.application.vo.CreateMemberVo;
import app.member.application.vo.MemberVo;
import app.member.application.vo.UpdateMemberVo;
import app.common.domain.model.common.ResponseCode;
import app.member.domain.model.entity.Member;
import app.member.domain.repository.MemberRepository;
import app.common.infrastructure.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public CreateMemberVo createMember(CreateMemberDto dto) {
        validateNewMember(dto);

        Member member = Member.builder()
                        .name(dto.getName())
                        .email(dto.getEmail())
                        .role(dto.getRole())
                        .build();

        Member savedMember = memberRepository.save(member);

        log.info("New member created with ID: {}", savedMember.getId());

        return CreateMemberVo.toVo(savedMember);
    }

    @Transactional(readOnly = true)
    public MemberVo getMember(Long memberId) {
        try {
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ResponseCode.NOT_EXIST));
            log.info("### 회원 조회 결과: {}", member);
            return MemberVo.toVo(member);
        } catch (CustomException e) {
            throw new CustomException(e.getResponseCode());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public UpdateMemberVo updateMember(UpdateMemberDto dto) {
        try {
            Member member = memberRepository.findById(dto.getMemberId())
                    .orElseThrow(() -> new CustomException(ResponseCode.NOT_EXIST));

            log.debug("### 회원 조회 결과: {}", member);

            updateMemberFields(member, dto);

            Member result = memberRepository.save(member);
            log.debug("### 회원 수정 결과: {}", result);

            return UpdateMemberVo.toVo(result);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ResponseCode.CONFLICT_DATA);
        } catch (CustomException e) {
            throw new CustomException(e.getResponseCode());
        }
    }

    private void updateMemberFields(Member member, UpdateMemberDto dto) {
        if (!dto.getName().isBlank()) {
            member.setName(dto.getName());
        }
        if (!dto.getEmail().isBlank()) {
            member.setEmail(dto.getEmail());
        }
        if (!dto.getRole().equals(member.getRole())) {
            member.setRole(dto.getRole());
        }
        member.setUpdatedMemberId(dto.getMemberId());
    }


}
