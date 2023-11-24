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
        List<Post> posts = findAllBy(memberId, cursorRequest);

        Long nextKey = getNextKey(posts);

        return new CursorResponse<>(cursorRequest.next(nextKey), posts);
    }

    public CursorResponse<Post> getPostsByCursor(List<Long> memberIds, CursorRequest cursorRequest) {
        List<Post> posts = findAllBy(memberIds, cursorRequest);

        Long nextKey = getNextKey(posts);

        return new CursorResponse<>(cursorRequest.next(nextKey), posts);
    }

    private static Long getNextKey(List<Post> posts) {
        Long nextKey = posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
        return nextKey;
    }

    private List<Post> findAllBy(Long memberId, CursorRequest cursorRequest) {
        if (cursorRequest.hasKey()) {
            return postRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        } else {
            return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
        }
    }

    private List<Post> findAllBy(List<Long> memberIds, CursorRequest cursorRequest) {
        if (cursorRequest.hasKey()) {
            return postRepository.findAllByLessThanIdAndMemberIdsAndOrderByIdDesc(cursorRequest.key(), memberIds, cursorRequest.size());
        } else {
            return postRepository.findAllByMemberIdsAndOrderByIdDesc(memberIds, cursorRequest.size());
        }
    }
}
