package com.example.fastcampusmysql.domain.follow.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Follow {

    private final Long id;
    private final Long fromMemberId;
    private final Long toMemberId;
    private final LocalDateTime createdAt;

    @Builder
    public Follow(Long id, Long fromMemberId, Long toMemberId, LocalDateTime createdAt) {
        this.id = id;
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
        this.createdAt = createdAt;
    }
}
