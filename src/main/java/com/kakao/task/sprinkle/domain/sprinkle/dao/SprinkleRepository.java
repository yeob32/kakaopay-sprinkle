package com.kakao.task.sprinkle.domain.sprinkle.dao;

import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprinkleRepository extends JpaRepository<Sprinkle, Long> {

    Sprinkle findByToken(String token);
}
