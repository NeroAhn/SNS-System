package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usecase.CreatePostUsecase;
import com.example.fastcampusmysql.application.usecase.GetDailyPostCountUsecase;
import com.example.fastcampusmysql.domain.post.dto.DailyPostInfo;
import com.example.fastcampusmysql.domain.post.dto.DailyPostRecordRequest;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final CreatePostUsecase createPostUsecase;
    private final GetDailyPostCountUsecase getDailyPostCountUsecase;

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
}
