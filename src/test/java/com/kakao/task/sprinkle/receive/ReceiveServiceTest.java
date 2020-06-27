package com.kakao.task.sprinkle.receive;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.dividend.dao.DividendRepository;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.application.ReceiveService;
import com.kakao.task.sprinkle.domain.sprinkle.application.SprinkleService;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.sprinkle.dto.ReceiveDto;
import com.kakao.task.sprinkle.domain.sprinkle.dto.SprinkleDto;
import com.kakao.task.sprinkle.domain.sprinkle.exception.CloseSprinkleException;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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


    Sprinkle bigSprinkle;

    User sprinkler;
    User receiver;
    Chat chat;

    final long totalAmount = 112852880123213213L;
    final int divideCount = 241;

    @AfterEach
    public void end() {
//        userRepository.deleteAll();
//        chatRepository.deleteAll();
//        sprinkleRepository.deleteAll();
//        dividendRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        sprinkler = userRepository.save(new User("yeob32"));
        receiver = userRepository.save(new User("sykim"));

        userRepository.saveAll(IntStream.range(0, divideCount)
                .mapToObj(i -> new User(String.valueOf(i)))
                .collect(Collectors.toList())
        );

        chat = new Chat();
        chat.addChatter(userRepository.findAll());
        chat = chatRepository.save(chat);

        SprinkleDto.Req req = SprinkleDto.Req.builder().roomId(chat.getId()).userId(sprinkler.getId()).amount(totalAmount).divideCount(divideCount).build();
        bigSprinkle = sprinkleService.createSprinkle(req);
    }

    @Test
    @DisplayName("다른 방 뿌리기 받기")
    public void anotherSprinkle() {
        receiveService.receive(ReceiveDto.Req.builder().token(bigSprinkle.getToken()).roomId(chat.getId()).userId(receiver.getId()).build());

        Chat anotherChatRoom = chatRepository.save(new Chat());
        receiveService.receive(ReceiveDto.Req.builder().token(bigSprinkle.getToken()).roomId(anotherChatRoom.getId()).userId(receiver.getId()).build());
    }

    @Test
    @DisplayName("배당금 합산")
    public void bigSprinkle() {
        List<User> users = userRepository.findAll();
        Predicate<User> predicate = user -> !user.getId().equals(sprinkler.getId()) && !user.getId().equals(receiver.getId());
        long total = users.stream()
                .filter(predicate)
                .map(user -> receiveService.receive(ReceiveDto.Req.builder().token(bigSprinkle.getToken()).roomId(chat.getId()).userId(user.getId()).build()))
                .mapToLong(Dividend::getAmount)
                .sum();

        Assertions.assertEquals(total, totalAmount);
    }

    @Test
    @DisplayName("모든 배당금 분배 후 받기")
    public void afterDividedMoney() {
        List<User> users = userRepository.findAll();
        Predicate<User> predicate = user -> !user.getId().equals(sprinkler.getId());
        Assertions.assertThrows(CloseSprinkleException.class, () -> {
            users.stream()
                    .filter(predicate)
                    .forEach(user -> receiveService.receive(ReceiveDto.Req.builder().token(bigSprinkle.getToken()).roomId(chat.getId()).userId(user.getId()).build()));
        });
    }
}
