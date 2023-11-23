package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usecase.CreateFollowMemberUsecase;
import com.example.fastcampusmysql.application.usecase.GetFollowMemberUsecase;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class FollowController {

    private final CreateFollowMemberUsecase createFollowMemberUsecase;
    private final GetFollowMemberUsecase getFollowMemberUsecase;

    @PostMapping("/follow/{fromId}/{toId}")
    public void create(@PathVariable Long fromId, @PathVariable Long toId) {
        createFollowMemberUsecase.execute(fromId, toId);
    }

    @GetMapping("/follow/members/{fromId}")
    public List<MemberDto> getFollows(@PathVariable Long fromId) {
        return getFollowMemberUsecase.execute(fromId);
    }
}
