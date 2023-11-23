package com.example.fastcampusmysql.domain.member.entity;

import com.example.fastcampusmysql.utils.MemberFixtureFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("닉네임 변경 테스트")
    @Test
    void changeNicknameTest() {
        Member member = MemberFixtureFactory.create(1L);
        String expected = "test";
        member.changeNickname(expected);
        Assertions.assertEquals(expected, member.getNickname());
    }

    @DisplayName("닉네임 변경 테스트 - 10글자를 초과할 경우 IllegalArgumentException throw")
    @Test
    void changeNicknameMaxLengthTest() {
        Member member = MemberFixtureFactory.create(1L);
        String expected = "12345678901";
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> member.changeNickname(expected)
        );
    }
}