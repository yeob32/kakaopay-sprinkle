package com.kakao.task.sprinkle.domain.sprinkle.application;

import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.sprinkle.dto.ReceiveDto;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ReceiveSprinkleService {

    private final SprinkleRepository sprinkleRepository;
    private final UserRepository userRepository;

    @Transactional
    public Dividend receive(ReceiveDto.Req req) {

        Sprinkle sprinkle = sprinkleRepository.findByToken(req.getToken());
        sprinkle.validateExpired();

        Dividend usableDividend = sprinkle.getDividends().stream()
                .filter(Dividend::usable)
                .findFirst()
                .orElseThrow(() -> new RuntimeException());

        User user = userRepository.findById(req.getUserId()).orElse(null);
        usableDividend.allotUser(user);

        return usableDividend;
    }
}
