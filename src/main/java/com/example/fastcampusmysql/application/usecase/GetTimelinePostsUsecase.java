package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetTimelinePostsUsecase {

    private final FollowReadService followReadService;
    private final PostReadService postReadService;

    public CursorResponse<Post> execute(Long memberId, CursorRequest cursorRequest) {
        var followMemberIds = followReadService.getFollows(memberId).stream()
                .map(Follow::getToMemberId)
                .toList();

        return postReadService.getPostsByCursor(followMemberIds, cursorRequest);
    }
}
