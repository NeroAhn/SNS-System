package com.example.fastcampusmysql.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Member {

    private final Long id;
    private final String email;
    private String nickname;
    private final LocalDate birthday;
    private final LocalDateTime createdAt;

    private static final Long NAME_MAX_LENGTH = 10L;

    @Builder
    public Member(Long id, String email, String nickname, LocalDate birthday, LocalDateTime createdAt) {
        this.id = id;
        this.email = Objects.requireNonNull(email);
        validateNickname(nickname);
        this.nickname = Objects.requireNonNull(nickname);
        this.birthday = Objects.requireNonNull(birthday);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public void changeNickname(String nickname) {
        Objects.requireNonNull(nickname);
        validateNickname(nickname);
        this.nickname = nickname;
    }

    private void validateNickname(String nickname) {
        Assert.isTrue(nickname.length() <= NAME_MAX_LENGTH, "최대 길이를 초과 하였습니다.");
    }
}
