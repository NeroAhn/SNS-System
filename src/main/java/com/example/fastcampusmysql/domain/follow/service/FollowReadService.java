package com.example.fastcampusmysql.domain.follow.service;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowReadService {

    private final FollowRepository followRepository;

    public List<Follow> getFollowings(Long memberId) {
        return followRepository.findByFromMemberId(memberId);
    }

    public List<Follow> getFollowers(Long memberId) {
        return followRepository.findByToMemberId(memberId);
    }
}
