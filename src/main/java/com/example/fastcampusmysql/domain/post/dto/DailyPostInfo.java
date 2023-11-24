package com.example.fastcampusmysql.domain.post.dto;

import java.time.LocalDate;

public record DailyPostInfo(
        Long memberId,
        LocalDate date,
        Long postCount
) {
}
