package com.example.fastcampusmysql.domain.member.service;

import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

    public Member register(RegisterMemberCommand command) {
        var member = Member.builder()
                .email(command.email())
                .nickname(command.nickname())
                .birthday(command.birthday())
                .build();
        return memberRepository.save(member);
    }

    public void changeNickname(Long id, String nickname) {
        /*
            1. 회원 닉네임 변경
            2. 닉네임 변경 히스토리 저장
         */
        var member = memberRepository.findById(id).orElseThrow();
        member.changeNickname(nickname);
        var savedMember = memberRepository.save(member);
        saveNicknameHistory(savedMember);
    }

    private void saveNicknameHistory(Member member) {
        memberNicknameHistoryRepository.save(
                MemberNicknameHistory.builder()
                        .memberId(member.getId())
                        .nickname(member.getNickname())
                        .build()
        );
    }


}
