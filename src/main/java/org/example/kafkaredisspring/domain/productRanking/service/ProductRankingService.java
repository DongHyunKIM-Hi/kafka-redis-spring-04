package org.example.kafkaredisspring.domain.productRanking.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafkaredisspring.domain.productRanking.model.dto.RankingDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {

    public static final String PRODUCT_RANKING_DAILY_KEY = "product:ranking:";

    private final StringRedisTemplate stringRedisTemplate;

    public void increaseProductRanking(long productId, LocalDate currentDate) {

        String key = PRODUCT_RANKING_DAILY_KEY + currentDate.toString();
        stringRedisTemplate.opsForZSet().incrementScore(key, String.valueOf(productId),1);
    }

    public List<RankingDto> findProductRankingTop3InToday() {

        LocalDate currentDate = LocalDate.now();

        String key = PRODUCT_RANKING_DAILY_KEY + currentDate.toString();

        Set<TypedTuple<String>> result = stringRedisTemplate.opsForZSet()
            .reverseRangeWithScores(key, 0, 2);

        if (result == null) {
            return Collections.emptyList();
        }

        return result.stream()
            .map(tuple -> new RankingDto(tuple.getValue(), tuple.getScore()))
            .toList();
    }

}
