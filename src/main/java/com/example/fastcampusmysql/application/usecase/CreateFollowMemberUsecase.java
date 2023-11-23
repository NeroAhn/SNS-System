package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.service.FollowWriteService;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 각 도메인 별 흐름을 제어하기 위한 Usecase
 * 최소한의 비즈니스 로직을 갖는다
 */
@RequiredArgsConstructor
@Service
public class CreateFollowMemberUsecase {

    private final MemberReadService memberReadService;
    private final FollowWriteService followWriteService;

    public void execute(Long fromMemberId, Long toMemberId) {
        var fromMember = memberReadService.getMember(fromMemberId);
        var toMember = memberReadService.getMember(toMemberId);
        followWriteService.create(fromMember, toMember);
    }
}
