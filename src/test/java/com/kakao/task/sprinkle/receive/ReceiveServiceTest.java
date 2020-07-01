package com.kakao.task.sprinkle.receive;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.chatUser.ChatUser;
import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.dividend.dao.DividendRepository;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.application.ReceiveService;
import com.kakao.task.sprinkle.domain.sprinkle.application.SprinkleService;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.sprinkle.dto.ReceiveDto;
import com.kakao.task.sprinkle.domain.sprinkle.dto.SprinkleDto;
import com.kakao.task.sprinkle.domain.sprinkle.exception.CloseSprinkleException;
import com.kakao.task.sprinkle.domain.sprinkle.exception.InvalidSprinkleException;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class ReceiveServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    SprinkleRepository sprinkleRepository;
    @Autowired
    DividendRepository dividendRepository;
    @Autowired
    SprinkleService sprinkleService;
    @Autowired
    ReceiveService receiveService;

    Sprinkle sprinkle;
    User sprinkler;
    User receiver;
    Chat chat;

    private static final long totalAmount = 1000;
    private static final int divideCount = 2;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        chatRepository.deleteAll();
        sprinkleRepository.deleteAll();
        dividendRepository.deleteAll();

        setUpData();
    }

    @Test
    @DisplayName("다른 방 뿌리기 받기")
    public void anotherSprinkle() {
        receiveService.receive(receiveReq());

        Chat anotherChatRoom = chatRepository.save(new Chat());

        Assertions.assertThrows(InvalidSprinkleException.class,
                () -> receiveService.receive(ReceiveDto.Req.builder()
                        .token(sprinkle.getToken())
                        .roomId(anotherChatRoom.getId())
                        .userId(receiver.getId())
                        .build())
        );
    }

    @Test
    @DisplayName("배당금 합산")
    public void bigSprinkle() {
        List<User> users = userRepository.findAll();
        long total = users.stream()
                .filter(user -> !user.equals(sprinkler) && !user.equals(receiver))
                .map(user -> receiveService.receive(receiveReq(user)))
                .mapToLong(Dividend::getAmount)
                .sum();

        Assertions.assertEquals(total, totalAmount);
    }

    @Test
    @DisplayName("모든 배당금 분배 후 받기")
    public void afterDividedMoney() {
        List<User> users = userRepository.findAll();

        Assertions.assertThrows(CloseSprinkleException.class, () -> {
            users.stream()
                    .filter(user -> !user.equals(sprinkler))
                    .forEach(user -> receiveService.receive(receiveReq(user)));
        });
    }

    private ReceiveDto.Req receiveReq() {
        return ReceiveDto.Req.builder().token(sprinkle.getToken()).roomId(chat.getId()).userId(receiver.getId()).build();
    }

    private ReceiveDto.Req receiveReq(User user) {
        return ReceiveDto.Req.builder().token(sprinkle.getToken()).roomId(chat.getId()).userId(user.getId()).build();
    }

    private void setUpData() {
        sprinkler = userRepository.save(new User("yeob32"));
        receiver = userRepository.save(new User("yeob33"));
        userRepository.save(new User("yeob34"));
        userRepository.save(new User("yeob35"));
        List<ChatUser> chatUsers = ChatUser.createChatUsers(userRepository.findAll());

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
