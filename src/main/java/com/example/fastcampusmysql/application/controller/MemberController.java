package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import com.example.fastcampusmysql.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberWriteService memberWriteService;
    private final MemberReadService memberReadService;

    @PostMapping("/members")
    public MemberDto register(@RequestBody RegisterMemberCommand command) {
        var member = memberWriteService.register(command);
        return memberReadService.toDto(member);
    }

    @PutMapping("/members/{id}/name")
    public void modifyNickname(
            @PathVariable Long id,
            @RequestBody String nickname
    ) {
        memberWriteService.changeNickname(id, nickname);
    }

    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }

    @GetMapping("/members/{id}/histories")
    public List<MemberNicknameHistoryDto> getNicknameHistories(@PathVariable Long id) {
        return memberReadService.getMemberNicknameHistories(id);
    }
}
