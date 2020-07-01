package com.kakao.task.sprinkle.receive;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.chatUser.ChatUser;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import com.kakao.task.sprinkle.global.exception.ErrorCode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ReceiveApiTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    SprinkleRepository sprinkleRepository;

    User sprinkler;
    User receiver;
    Chat chat;
    Sprinkle sprinkle;

    private static final long totalAmount = 1000;
    private static final int divideCount = 2;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        chatRepository.deleteAll();
        sprinkleRepository.deleteAll();

        setUpData();
    }

    @Test
    @DisplayName("받기")
    public void receive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/receive")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", receiver.getId())
                .param("token", sprinkle.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNumber())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("받은 거 또 받기")
    public void receiveWithDuplicationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/receive")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", receiver.getId())
                .param("token", sprinkle.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNumber())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post("/receive")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", receiver.getId())
                .param("token", sprinkle.getToken()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.RECEIVE_DUPLICATION.getMessage()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("내가 뿌린거 내가 받기")
    public void receiveWithVerifyReceiverException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/receive")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", sprinkler.getId())
                .param("token", sprinkle.getToken()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.VERIFY_RECEIVER.getMessage()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("받고 나서 해당 뿌리기 조회")
    public void receiveAndMySprinkle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/receive")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", receiver.getId())
                .param("token", sprinkle.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNumber())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/sprinkle")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", sprinkler.getId())
                .param("token", sprinkle.getToken()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNumber())
                .andDo(MockMvcResultHandlers.print());
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
