package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreatePostUsecase {

    private final MemberReadService memberReadService;
    private final PostWriteService postWriteService;

    public Long execute(PostCommand command) {
        memberReadService.getMember(command.memberId());
        return postWriteService.create(command);
    }

}
