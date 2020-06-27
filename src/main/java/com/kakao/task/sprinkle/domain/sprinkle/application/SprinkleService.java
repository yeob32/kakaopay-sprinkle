package com.kakao.task.sprinkle.domain.sprinkle.application;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.sprinkle.dto.SprinkleDto;
import com.kakao.task.sprinkle.domain.sprinkle.exception.ChatNotFoundException;
import com.kakao.task.sprinkle.domain.sprinkle.exception.NotFoundSprinkleException;
import com.kakao.task.sprinkle.domain.sprinkle.exception.UserNotFoundException;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class SprinkleService {

    private final SprinkleRepository sprinkleRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public Sprinkle createSprinkle(SprinkleDto.Req requestDto) {
        Sprinkle sprinkle = getSprinkle(requestDto);
        sprinkle.createSprinkle();

        return sprinkleRepository.save(sprinkle);
    }

    @Transactional(readOnly = true)
    public SprinkleDto.MyRes mySprinkle(SprinkleDto.MyReq myReqDto) {
        Sprinkle sprinkle = sprinkleRepository.findByToken(myReqDto.getToken());
        if(sprinkle == null)
            throw new NotFoundSprinkleException();
        sprinkle.validateExpiredByRetreive();

        List<Dividend> dividends = sprinkle.getDividends();

        return SprinkleDto.MyRes
                .builder()
                .sprinkle(sprinkle)
                .dividends(dividends)
                .build();
    }

    @Transactional(readOnly = true)
    public Sprinkle getSprinkle(SprinkleDto.Req requestDto) {
        User sprinkler = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new UserNotFoundException(""));
        Chat chat = chatRepository.findById(requestDto.getRoomId()).orElseThrow(() -> new ChatNotFoundException(requestDto.getRoomId().toString()));
        chat.checkContainsUser(sprinkler);

        return Sprinkle.builder()
                .user(sprinkler)
                .chat(chat)
                .amount(requestDto.getAmount())
                .divideCount(requestDto.getDevideCount())
                .build();
    }
}
