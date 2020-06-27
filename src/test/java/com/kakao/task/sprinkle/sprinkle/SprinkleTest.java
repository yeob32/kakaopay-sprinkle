package com.kakao.task.sprinkle.sprinkle;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.dividend.dao.DividendRepository;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
public class SprinkleTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    SprinkleRepository sprinkleRepository;
    @Autowired
    DividendRepository dividendRepository;

    User sprinkler;
    User receiver;
    Chat chat1;
    Sprinkle sprinkle;
    
    @BeforeEach
    public void setUp() {
        sprinkler = userRepository.save(new User("yeob32"));
        receiver = userRepository.save(new User("sykim"));
        chat1 = chatRepository.save(new Chat());
        chat1.addChatter(sprinkler, receiver);
        sprinkle = Sprinkle.builder()
                .user(sprinkler)
                .chat(chat1)
                .amount(1000)
                .divideCount(3)
                .build();
        sprinkle.createSprinkle();
    }

    @Test
    @DisplayName("배당금 확인")
    public void sumDivdideAmount() {
        long totalAmount = sprinkle.getDividends()
                .stream()
                .mapToLong(Dividend::getAmount)
                .sum();

        Assertions.assertEquals(1000, totalAmount);
    }

    @Test
    @DisplayName("뿌리기 등록")
    public void createSprinkle() {
        Sprinkle saveSprinkle = sprinkleRepository.save(sprinkle);

        Assertions.assertNotNull(saveSprinkle);
        Assertions.assertNotNull(saveSprinkle.getToken());
        Assertions.assertEquals(sprinkle.getDividends().size(), saveSprinkle.getDividends().size());
    }

    @Test
    @DisplayName("나의 뿌리기 조회")
    @Transactional
    public void mySprinkle() {
        Sprinkle saveSprinkle = sprinkleRepository.save(sprinkle);

        Sprinkle sprinkle = sprinkleRepository.findByToken(saveSprinkle.getToken());
        Assertions.assertEquals(0, sprinkle.getReceivedAmount());
        Assertions.assertEquals(3, sprinkle.getDividends().size());
        Assertions.assertEquals(3, sprinkle.getDividends().size());
        Assertions.assertEquals(1000, sprinkle.getDividends()
                .stream()
                .mapToLong(Dividend::getAmount)
                .sum());
    }


}
