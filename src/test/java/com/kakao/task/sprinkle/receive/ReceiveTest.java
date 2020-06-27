package com.kakao.task.sprinkle.receive;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.dividend.dao.DividendRepository;
import com.kakao.task.sprinkle.domain.sprinkle.exception.DuplicateReceiveException;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.sprinkle.exception.VerifyReceiver;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ReceiveTest {

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
        sprinkle = Sprinkle.builder()
                .user(sprinkler)
                .chat(chat1)
                .amount(1000)
                .divideCount(3)
                .build();
        sprinkle.createSprinkle();
    }

    @Test
    @DisplayName("배당금 받기")
    public void receive() {
        Sprinkle saveSprinkle = sprinkleRepository.save(sprinkle);
        Sprinkle findSprinkle = sprinkleRepository.findByToken(sprinkle.getToken());

        saveSprinkle.receiveValidator(receiver);
        Dividend usableDividend = findSprinkle.getDividends().stream().filter(Dividend::usable).findFirst().get();

        User user = userRepository.findById(receiver.getId()).orElse(null);
        usableDividend.allotMoney(user);

        Assertions.assertTrue(usableDividend.getAmount() > 0);
    }

    @Test
    @DisplayName("내 배당금 내가 받기")
    public void receiveMyMoney() {
        Sprinkle saveSprinkle = sprinkleRepository.save(sprinkle);
        Sprinkle findSprinkle = sprinkleRepository.findByToken(sprinkle.getToken());

        saveSprinkle.receiveValidator(sprinkler);
        Assertions.assertThrows(VerifyReceiver.class, () -> findSprinkle.receiveValidator(sprinkler));
    }

    @Test
    @DisplayName("배당금 두번 받기")
    public void duplicateRecevice() {
        Sprinkle saveSprinkle = sprinkleRepository.save(sprinkle);
        Sprinkle findSprinkle = sprinkleRepository.findByToken(saveSprinkle.getToken());

        User findReceiver = userRepository.findById(receiver.getId()).orElse(null);
        findSprinkle.receiveValidator(findReceiver);

        Dividend usableDividend = findSprinkle.getDividends().stream().filter(Dividend::usable).findFirst().get();
        usableDividend.allotMoney(findReceiver);
        Assertions.assertThrows(DuplicateReceiveException.class, () -> findSprinkle.receiveValidator(findReceiver));
    }
}
