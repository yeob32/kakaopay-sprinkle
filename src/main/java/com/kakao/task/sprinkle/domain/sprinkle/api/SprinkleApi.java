package com.kakao.task.sprinkle.domain.sprinkle.api;

import com.kakao.task.sprinkle.domain.sprinkle.application.ReceiveService;
import com.kakao.task.sprinkle.domain.sprinkle.application.SprinkleService;
import com.kakao.task.sprinkle.domain.sprinkle.dto.ReceiveDto;
import com.kakao.task.sprinkle.domain.sprinkle.dto.SprinkleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SprinkleApi {

    private final SprinkleService sprinkleService;
    private final ReceiveService receiveService;

    @PostMapping("/sprinkle")
    @ResponseStatus(HttpStatus.OK)
    public SprinkleDto.Res sprinkle(@RequestHeader("X-ROOM-ID") String roomId,
                                    @RequestHeader("X-USER-ID") String userId,
                                    @RequestParam long amount, @RequestParam int divideCount) {

        SprinkleDto.Req req = SprinkleDto.Req.builder()
                .roomId(UUID.fromString(roomId))
                .userId(Long.parseLong(userId))
                .amount(amount)
                .divideCount(divideCount)
                .build();

        return new SprinkleDto.Res(sprinkleService.createSprinkle(req));
    }

    @GetMapping("/sprinkle")
    @ResponseStatus(HttpStatus.OK)
    public SprinkleDto.MyRes mySprinkle(@RequestHeader("X-ROOM-ID") String roomId,
                            @RequestHeader("X-USER-ID") String userId,
                            @RequestParam String token) {

        SprinkleDto.MyReq myReqDto = SprinkleDto.MyReq.builder()
                .roomId(UUID.fromString(roomId))
                .userId(Long.parseLong(userId))
                .token(token)
                .build();

        return sprinkleService.mySprinkle(myReqDto);
    }

    @PostMapping("/receive")
    @ResponseStatus(HttpStatus.OK)
    public ReceiveDto.Res receive(@RequestHeader("X-ROOM-ID") String roomId,
                                  @RequestHeader("X-USER-ID") String userId,
                                  @RequestParam String token) {

        ReceiveDto.Req req = ReceiveDto.Req.builder()
                .roomId(UUID.fromString(roomId))
                .userId(Long.parseLong(userId))
                .token(token)
                .build();

        return new ReceiveDto.Res(receiveService.receive(req));
    }
}
