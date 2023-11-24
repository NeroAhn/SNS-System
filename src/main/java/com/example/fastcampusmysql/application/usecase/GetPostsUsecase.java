package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetPostsUsecase {

    private final PostReadService postReadService;

    public Page<Post> execute(Long memberId, Pageable pageable) {
        return postReadService.getPosts(memberId, pageable);
    }
}
