package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetPostsUsecase {

    private final PostReadService postReadService;

    public Page<Post> execute(Long memberId, Pageable pageable) {
        return postReadService.getPostsByOffset(memberId, pageable);
    }

    public CursorResponse<Post> execute(Long memberId, CursorRequest cursorRequest) {
        return postReadService.getPostsByCursor(memberId, cursorRequest);
    }
}
