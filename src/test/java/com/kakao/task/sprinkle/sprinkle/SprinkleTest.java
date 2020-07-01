package com.kakao.task.sprinkle.sprinkle;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.chatUser.ChatUser;
import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.dividend.dao.DividendRepository;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
public class SprinkleTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private SprinkleRepository sprinkleRepository;
    @Autowired
    private DividendRepository dividendRepository;

    private static final int totalAmount = 10000;
    private static final int divideCount = 3;

    @Test
    @DisplayName("배당금 확인")
    public void sumDivdideAmount() {
        long totalAmount = getSprinkle().getDividends()
                .stream()
                .mapToLong(Dividend::getAmount)
                .sum();

        Assertions.assertEquals(totalAmount, totalAmount);
    }

    @Test
    @DisplayName("뿌리기 등록")
    public void createSprinkle() {
        Sprinkle sprinkle = getSprinkle();
        Sprinkle saveSprinkle = sprinkleRepository.save(sprinkle);

        Assertions.assertNotNull(saveSprinkle);
        Assertions.assertNotNull(saveSprinkle.getToken());
        Assertions.assertEquals(sprinkle.getDividends().size(), saveSprinkle.getDividends().size());
        Assertions.assertEquals(sprinkle.getDividends().size(), dividendRepository.findAll().size());
    }

    @Test
    @DisplayName("나의 뿌리기 조회")
    @Transactional
    public void mySprinkle() {
        Sprinkle saveSprinkle = sprinkleRepository.save(getSprinkle());

        Sprinkle sprinkle = sprinkleRepository.findByToken(saveSprinkle.getToken());
        Assertions.assertEquals(0, sprinkle.getReceivedAmount());
        Assertions.assertEquals(divideCount, sprinkle.getDividends().size());
        Assertions.assertEquals(totalAmount, sprinkle.getDividends()
                .stream()
                .mapToLong(Dividend::getAmount)
                .sum());
    }

    private Sprinkle getSprinkle() {
        User sprinkler = userRepository.save(new User("yeob32"));
        User receiver = userRepository.save(new User("yeob33"));
        List<ChatUser> chatUsers = ChatUser.createChatUsers(Arrays.asList(sprinkler, receiver));

        Chat chat = Chat.createChat(chatUsers);
        chatRepository.save(chat);

        return Sprinkle.builder()
                .user(sprinkler)
                .chat(chat)
                .amount(totalAmount)
                .divideCount(divideCount)
                .build();
    }
}
