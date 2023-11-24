package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.post.dto.DailyPostInfo;
import com.example.fastcampusmysql.domain.post.dto.DailyPostRecordRequest;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetDailyPostCountUsecase {

    private final PostReadService postReadService;

    public List<DailyPostInfo> execute(Long memberId, LocalDate startDate, LocalDate endDate) {
        DailyPostRecordRequest request = new DailyPostRecordRequest(memberId, startDate, endDate);
        return postReadService.getDailyPostCount(request);
    }
}
