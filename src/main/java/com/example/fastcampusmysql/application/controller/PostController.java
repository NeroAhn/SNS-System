package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usecase.CreatePostUsecase;
import com.example.fastcampusmysql.application.usecase.GetDailyPostCountUsecase;
import com.example.fastcampusmysql.application.usecase.GetPostsUsecase;
import com.example.fastcampusmysql.application.usecase.GetTimelinePostsUsecase;
import com.example.fastcampusmysql.domain.post.dto.DailyPostInfo;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final CreatePostUsecase createPostUsecase;
    private final GetDailyPostCountUsecase getDailyPostCountUsecase;
    private final GetPostsUsecase getPostsUsecase;
    private final GetTimelinePostsUsecase getTimelinePostsUsecase;

    @PostMapping("/posts")
    public Long create(@RequestBody PostCommand command) {
        return createPostUsecase.execute(command);
    }

    @GetMapping("/posts/daily-count")
    public List<DailyPostInfo> getDailyPostInfos(
            @RequestParam Long memberId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {

        return getDailyPostCountUsecase.execute(memberId, startDate, endDate);
    }

    @GetMapping("/posts/members/{memberId}/by-offset")
    public Page<Post> getPostsByOffset(
            @PathVariable Long memberId,
            Pageable pageable
    ) {
        return getPostsUsecase.execute(memberId, pageable);
    }

    @GetMapping("/posts/members/{memberId}/by-cursor")
    public CursorResponse<Post> getPostsByCursor(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ) {
        return getPostsUsecase.execute(memberId, cursorRequest);
    }

    @GetMapping("/posts/members/{memberId}/timelines")
    public CursorResponse<Post> getTimelineByCursor (
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ) {
        return getTimelinePostsUsecase.execute(memberId, cursorRequest);
    }
}
