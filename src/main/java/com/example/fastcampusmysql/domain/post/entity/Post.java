package com.example.fastcampusmysql.domain.post.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Post {

    private final Long id;
    private final Long memberId;
    private final String contents;
    private Long likeCount;
    private final LocalDate createdDate;
    private final LocalDateTime createdAt;
    private Long version;

    @Builder
    public Post(Long id, Long memberId, String contents, Long likeCount, LocalDate createdDate, LocalDateTime createdAt, Long version) {
        this.id = id;
        this.memberId = Objects.requireNonNull(memberId);
        this.contents = Objects.requireNonNull(contents);
        this.likeCount = likeCount == null ? 0L : likeCount;
        this.createdDate = createdDate == null ? LocalDate.now() : createdDate;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.version = version == null ? 0L : version;
    }

    public void incrementLikeCount() {
        likeCount += 1;
    }
}
