package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.service.PostLikeWriteService;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreatePostLikeUsecase {

    private final MemberReadService memberReadService;
    private final PostReadService postReadService;
    private final PostLikeWriteService postLikeWriteService;

    public Long execute(Long postId, Long memberId) {
        // 포스트 정보 조회
        var post = postReadService.getPost(postId);
        // 회원 정보 조회
        var member = memberReadService.getMember(memberId);

        return postLikeWriteService.create(post, member);
    }

}
