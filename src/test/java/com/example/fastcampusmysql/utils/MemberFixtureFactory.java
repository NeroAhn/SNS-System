package com.example.fastcampusmysql.utils;

import com.example.fastcampusmysql.domain.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class MemberFixtureFactory {

    public static Member create(Long seed) {
        EasyRandomParameters params = new EasyRandomParameters().seed(seed);
        EasyRandom easyRandom = new EasyRandom(params);
        return easyRandom.nextObject(Member.class);
    }
}
