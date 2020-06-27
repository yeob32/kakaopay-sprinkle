package com.kakao.task.sprinkle.domain.dividend.dao;

import com.kakao.task.sprinkle.domain.dividend.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long> {
    List<Dividend> findBySprinkleId(long sprinkleId);
}
