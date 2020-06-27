package com.kakao.task.sprinkle.domain.sprinkle.api;

import com.kakao.task.sprinkle.domain.sprinkle.application.ReceiveSprinkleService;
import com.kakao.task.sprinkle.domain.sprinkle.application.SprinkleService;
import com.kakao.task.sprinkle.domain.sprinkle.dto.ReceiveDto;
import com.kakao.task.sprinkle.domain.sprinkle.dto.SprinkleDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class SprinkleApi {

    private final SprinkleService sprinkleService;
    private final ReceiveSprinkleService receiveSprinkleService;

    @PostMapping("/sprinkle")
    @ResponseStatus(HttpStatus.OK)
    public SprinkleDto.Res sprinkle(@RequestHeader("X-ROOM-ID") String roomId,
                                    @RequestHeader("X-USER-ID") String userId,
                                    SprinkleDto.Req reqDto) {

        reqDto.addHttpHeader(UUID.fromString(roomId), Long.parseLong(userId));
        return new SprinkleDto.Res(sprinkleService.createSprinkle(reqDto));
    }

    @GetMapping("/sprinkle")
    @ResponseStatus(HttpStatus.OK)
    public SprinkleDto.MyRes mySprinkle(@RequestHeader("X-ROOM-ID") String roomId,
                            @RequestHeader("X-USER-ID") String userId,
                            String token) {

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
                                  ReceiveDto.Req dto) {

        dto.addHttpHeader(UUID.fromString(roomId), Long.parseLong(userId));
        return new ReceiveDto.Res(receiveSprinkleService.receive(dto));
    }
}
