package com.kakao.task.sprinkle.domain.sprinkle.application;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.sprinkle.dto.ReceiveDto;
import com.kakao.task.sprinkle.domain.sprinkle.exception.CloseSprinkleException;
import com.kakao.task.sprinkle.domain.sprinkle.exception.DataNotFoundException;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import com.kakao.task.sprinkle.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ReceiveService {

    private final SprinkleRepository sprinkleRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    public Dividend receive(ReceiveDto.Req req) {
        Chat chat = chatRepository.findById(req.getRoomId())
                .orElseThrow(() -> new DataNotFoundException(req.getRoomId(), ErrorCode.CHAT_NOT_FOUND));
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new DataNotFoundException(req.getUserId(), ErrorCode.USER_NOT_FOUND));

        Sprinkle sprinkle = sprinkleRepository.findByToken(req.getToken());
        if(sprinkle == null) {
            throw new DataNotFoundException(req.getToken(), ErrorCode.SPRINKLE_NOT_FOUND);
        }

        sprinkle.checkInvalidChat(chat);
        sprinkle.receiveValidator(user);

        Dividend usableDividend = sprinkle.getDividends().stream()
                .filter(Dividend::usable)
                .findFirst()
                .orElseThrow(() -> new CloseSprinkleException(ErrorCode.SPRINKLE_CLOSE));
        usableDividend.allotMoney(user);
        sprinkle.totalReceivedAmount(usableDividend);

        return usableDividend;
    }
}
