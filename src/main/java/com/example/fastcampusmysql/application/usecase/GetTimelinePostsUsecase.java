package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.TimelineReadService;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetTimelinePostsUsecase {

    private final FollowReadService followReadService;
    private final PostReadService postReadService;
    private final TimelineReadService timelineReadService;

    public CursorResponse<Post> execute(Long memberId, CursorRequest cursorRequest) {
        var followMemberIds = followReadService.getFollowings(memberId).stream()
                .map(Follow::getToMemberId)
                .toList();

        return postReadService.getPostsByCursor(followMemberIds, cursorRequest);
    }

    public CursorResponse<Post> executeByTimeline(Long memberId, CursorRequest cursorRequest) {
        var cursoredTimelines = timelineReadService.getTimelines(memberId, cursorRequest);
        var postIds = cursoredTimelines.body().stream().map(Timeline::getPostId).toList();
        var posts = postReadService.getPosts(postIds);

        return new CursorResponse<>(cursoredTimelines.nextCursorRequest(), posts);
    }
}
