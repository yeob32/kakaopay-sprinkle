package com.kakao.task.sprinkle.sprinkle;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.dividend.DividendRepository;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ReceiveSprinkleTest {

    @Autowired
    SprinkleRepository sprinkleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    DividendRepository dividendRepository;

    User sprinkler;
    Chat chat1;
    Sprinkle sprinkle;

    @BeforeEach
    public void setUp() {
        sprinkler = userRepository.save(new User("yeob32"));
        chat1 = chatRepository.save(new Chat());

        sprinkle = Sprinkle.builder()
                .user(sprinkler)
                .chat(chat1)
                .divideCount(7)
                .amount(1000)
                .build();
        sprinkleRepository.save(sprinkle);
    }

    @Test
    public void receive() {

    }
}
