package com.kakao.task.sprinkle.receive;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.chatUser.ChatUser;
import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.dividend.dao.DividendRepository;
import com.kakao.task.sprinkle.domain.sprinkle.exception.DuplicateReceiveException;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.sprinkle.exception.VerifyReceiverException;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

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

    private static final int totalAmount = 1000;
    private static final int divideCount = 3;

    User sprinkler;
    User receiver;
    Chat chat;
    Sprinkle sprinkle;

    @BeforeEach
    public void setUp() {
        setUpData();
    }

    @Test
    @DisplayName("배당금 받기")
    public void receive() {
        sprinkle.receiveValidator(receiver);
        Dividend usableDividend = sprinkle.getDividends().stream().filter(Dividend::usable).findFirst().get();
        usableDividend.allotMoney(receiver);

        Assertions.assertTrue(usableDividend.getAmount() > 0);
    }

    @Test
    @DisplayName("배당금 받은 후 받기 완료 금액 확인")
    public void checkReceivedAmount() {
        sprinkle.receiveValidator(receiver);
        Dividend usableDividend = sprinkle.getDividends().stream().filter(Dividend::usable).findFirst().get();
        usableDividend.allotMoney(receiver);

        sprinkle.totalReceivedAmount(usableDividend);

        Assertions.assertTrue(sprinkle.getAmount() > 0);
    }

    @Test
    @DisplayName("내 배당금 내가 받기")
    public void receiveMyMoney() {
        Assertions.assertThrows(VerifyReceiverException.class, () -> sprinkle.receiveValidator(sprinkler));
    }

    @Test
    @DisplayName("배당금 두번 받기")
    public void duplicateRecevice() {
        sprinkle.receiveValidator(receiver);

        Dividend usableDividend = sprinkle.getDividends().stream().filter(Dividend::usable).findFirst().get();
        usableDividend.allotMoney(receiver);

        Assertions.assertThrows(DuplicateReceiveException.class, () -> sprinkle.receiveValidator(receiver));
    }

    private void setUpData() {
        sprinkler = userRepository.save(new User("yeob32"));
        receiver = userRepository.save(new User("sykim"));
        List<ChatUser> chatUsers = ChatUser.createChatUsers(Arrays.asList(sprinkler, receiver));

        chat = Chat.createChat(chatUsers);
        chatRepository.save(chat);

        sprinkle = Sprinkle.builder()
                .user(sprinkler)
                .chat(chat)
                .amount(totalAmount)
                .divideCount(divideCount)
                .build();
        sprinkleRepository.save(sprinkle);
    }
}
