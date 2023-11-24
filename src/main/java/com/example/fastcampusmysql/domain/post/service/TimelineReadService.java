package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.repository.TimelineRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TimelineReadService {

    private final TimelineRepository timelineRepository;

    public CursorResponse<Timeline> getTimelines(Long memberId, CursorRequest cursorRequest) {
        var timelines = findAllBy(memberId, cursorRequest);
        var nextKey = getNextKey(timelines);

        return new CursorResponse<>(cursorRequest.next(nextKey), timelines);
    }

    private List<Timeline> findAllBy(Long memberId, CursorRequest cursorRequest) {
        if (cursorRequest.hasKey()) {
            return timelineRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        } else {
            return timelineRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
        }
    }

    private static Long getNextKey(List<Timeline> timelines) {
        Long nextKey = timelines.stream()
                .mapToLong(Timeline::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
        return nextKey;
    }
}
