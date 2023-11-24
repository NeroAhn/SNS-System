package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.repository.TimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TimelineWriteService {

    private final TimelineRepository timelineRepository;

    // 팔로우 하고 있는 회원들에게 Post 정보 전달
    public void deliveryToTimeline(Long postId, List<Long> toMemberIds) {
        var timelines = toMemberIds.stream()
                .map(toMemberId -> Timeline.builder()
                        .postId(postId)
                        .memberId(toMemberId)
                        .build())
                .toList();

        timelineRepository.bulkInsert(timelines);
    }
}
