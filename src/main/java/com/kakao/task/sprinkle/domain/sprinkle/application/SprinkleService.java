package com.kakao.task.sprinkle.domain.sprinkle.application;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.sprinkle.dto.SprinkleDto;
import com.kakao.task.sprinkle.domain.sprinkle.exception.DataNotFoundException;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import com.kakao.task.sprinkle.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SprinkleService {

    private final SprinkleRepository sprinkleRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public Sprinkle createSprinkle(SprinkleDto.Req requestDto) {
        Sprinkle sprinkle = getSprinkle(requestDto);

        return sprinkleRepository.save(sprinkle);
    }

    @Transactional(readOnly = true)
    public Sprinkle getSprinkle(SprinkleDto.Req requestDto) {
        User sprinkler = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new DataNotFoundException(requestDto.getUserId(), ErrorCode.USER_NOT_FOUND));
        Chat chat = chatRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new DataNotFoundException(requestDto.getRoomId(), ErrorCode.CHAT_NOT_FOUND));

        return Sprinkle.builder()
                .user(sprinkler)
                .chat(chat)
                .amount(requestDto.getAmount())
                .divideCount(requestDto.getDivideCount())
                .build();
    }

    @Transactional(readOnly = true)
    public SprinkleDto.MyRes mySprinkle(SprinkleDto.MyReq myReqDto) {
        Sprinkle sprinkle = sprinkleRepository.findByToken(myReqDto.getToken());
        if (sprinkle == null) {
            throw new DataNotFoundException(myReqDto.getToken(), ErrorCode.SPRINKLE_NOT_FOUND);
        }

        sprinkle.validateExpiredByRetreive();
        List<Dividend> dividends = sprinkle.getDividends();

        return SprinkleDto.MyRes
                .builder()
                .sprinkle(sprinkle)
                .dividends(dividends)
                .build();
    }
}
