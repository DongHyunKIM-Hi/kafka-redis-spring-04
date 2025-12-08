package org.example.kafkaredisspring.domain.delivery.service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafkaredisspring.common.entity.Delivery;
import org.example.kafkaredisspring.common.enums.DeliveryStatus;
import org.example.kafkaredisspring.domain.delivery.model.response.DeliveryResponse;
import org.example.kafkaredisspring.domain.delivery.repository.DeliveryRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCacheService {

    private final StringRedisTemplate stringRedisTemplate;
    private final DeliveryRepository deliveryRepository;

    public void cacheDelivery(Delivery delivery) {

        String key = "delivery_status:" + delivery.getId();

        // 배송 상태 캐시에 저장
        stringRedisTemplate.opsForHash().putAll(key, Map.of(
            "status", delivery.getStatus().name(),
            "orderId", delivery.getOrderId().toString(),
            "productId", delivery.getProductId().toString(),
            "statusUpdatedAt", delivery.getStatusUpdatedAt().toString()
        ));


        // 사용자별 ZSET에 추가 (최근 상태 기준으로 정렬)
        String userKey = "user_deliveries:" + delivery.getUserId();
        double score = delivery.getStatusUpdatedAt().toEpochSecond(ZoneOffset.UTC);

        stringRedisTemplate.opsForZSet()
            .add(userKey, delivery.getId().toString(), score);

        log.info("[Delivery-Redis] 캐시 업데이트 - key={}, status={}", key, delivery.getStatus());
    }

    public List<DeliveryResponse> getUserDeliveries(Long userId) {

        String userKey = "user_deliveries:" + userId;

        // 1. Redis ZSET에서 최근 배송 20개 ID 조회
        Set<String> deliveryIds =
            stringRedisTemplate.opsForZSet()
                .reverseRange(userKey, 0, 19);

        if (deliveryIds == null || deliveryIds.isEmpty()) {
            // Redis에 캐시가 없다면 DB에서 fallback (옵션)
            return deliveryRepository.findByUserIdOrderByStatusUpdatedAtDesc(userId)
                .stream()
                .map(d -> new DeliveryResponse(
                    d.getId(),
                    d.getOrderId(),
                    d.getProductId(),
                    d.getStatus(),
                    d.getStatusUpdatedAt().toString()
                ))
                .collect(Collectors.toList());
        }

        List<DeliveryResponse> responses = new ArrayList<>();

        for (String deliveryId : deliveryIds) {
            String key = "delivery_status:" + deliveryId;

            Map<Object, Object> map =
                stringRedisTemplate.opsForHash().entries(key);

            if (map.isEmpty()) {
                continue;
            }

            DeliveryResponse response = new DeliveryResponse(
                Long.valueOf(deliveryId),
                Long.valueOf(map.get("orderId").toString()),
                Long.valueOf(map.get("productId").toString()),
                DeliveryStatus.valueOf(map.get("status").toString()),
                map.get("statusUpdatedAt").toString()
            );

            responses.add(response);
        }

        return responses;
    }

}