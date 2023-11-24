package com.example.fastcampusmysql.utils;

import com.example.fastcampusmysql.domain.post.entity.Post;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.*;

public class PostFixtureFactory {

    public static EasyRandom get(Long memberId, LocalDate startDate, LocalDate endDate) {
        // id 컬럼을 자동생성 제외하기 위한 predicate
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        // memberId 컬럼을 고정값으로 생성하기 위한 predicate
        var memberIdPredicate = named("memberId")
                .and(ofType(Long.class))
                .and(inClass(Post.class));


        EasyRandomParameters params = new EasyRandomParameters()
                .excludeField(idPredicate)                      // id 는 자동생성 제외
                .dateRange(startDate, endDate)                  // startDate ~ endDate 사이의 값으로 random 생성
                .randomize(memberIdPredicate, () -> memberId);  // memberId 는 고정값으로 생성

        return new EasyRandom(params);
    }
}
