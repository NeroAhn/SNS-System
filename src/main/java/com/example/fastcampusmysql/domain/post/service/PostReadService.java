package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostInfo;
import com.example.fastcampusmysql.domain.post.dto.DailyPostRecordRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostReadService {

    private final PostRepository postRepository;

    public List<DailyPostInfo> getDailyPostCount(DailyPostRecordRequest request) {
        return postRepository.groupByCreatedDate(request);
    }

    public Page<Post> getPostsByOffset(Long memberId, Pageable pageable) {
        return postRepository.findAllByMemberIdOffset(memberId, pageable);
    }

    public CursorResponse<Post> getPostsByCursor(Long memberId, CursorRequest cursorRequest) {
        List<Post> posts;
        if (cursorRequest.hasKey()) {
            posts = postRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        } else {
            posts = postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
        }

        Long nextKey = posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);

        return new CursorResponse<>(cursorRequest.next(nextKey), posts);
    }
}
