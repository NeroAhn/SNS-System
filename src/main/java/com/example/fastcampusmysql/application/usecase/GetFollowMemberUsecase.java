package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GetFollowMemberUsecase {

    private final FollowReadService followReadService;
    private final MemberReadService memberReadService;


    public List<MemberDto> execute(Long memberId) {
        var memberIds = followReadService.getFollowings(memberId)
                .stream()
                .map(follow -> follow.getToMemberId())
                .toList();

        return memberReadService.getMembers(memberIds);
    }
}
