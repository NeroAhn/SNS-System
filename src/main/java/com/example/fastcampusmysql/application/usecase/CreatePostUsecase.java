package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreatePostUsecase {

    private final FollowReadService followReadService;
    private final PostWriteService postWriteService;
    private final TimelineWriteService timelineWriteService;

    public Long execute(PostCommand command) {
        // 포스트 저장
        Long postId = postWriteService.create(command);

        // 타임라인 저장 (Fan Out On Write)
        List<Long> toMemberIds = followReadService.getFollowers(command.memberId()).stream()
                .map(Follow::getFromMemberId)
                .toList();
        timelineWriteService.deliveryToTimeline(postId, toMemberIds);

        return postId;
    }

}
