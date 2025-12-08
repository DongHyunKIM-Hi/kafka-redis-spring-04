package org.example.kafkaredisspring.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafkaredisspring.common.entity.Delivery;
import org.example.kafkaredisspring.common.enums.DeliveryStatus;
import org.example.kafkaredisspring.domain.delivery.repository.DeliveryRepository;
import org.example.kafkaredisspring.domain.delivery.service.DeliveryCacheService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryStatusScheduler {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryCacheService deliveryCacheService;

    // 데모용: 5초마다 실행 (수업에서는 3~5초로 맞춰도 좋습니다.)
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void updatePreparingToShipping() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusSeconds(10); // 10초 이상 지난 건만

        List<Delivery> preparingList =
            deliveryRepository.findByStatusAndStatusUpdatedAtBefore(
                DeliveryStatus.PREPARING,
                threshold
            );

        if (preparingList.isEmpty()) {
            return;
        }

        log.info("[Scheduler] PREPARING → SHIPPING 전환 대상: {}건", preparingList.size());

        for (Delivery delivery : preparingList) {
            delivery.markShipping(now);
            // 트랜잭션 안에서 상태만 변경하면, flush 시점에 DB에 반영됩니다.
            deliveryCacheService.cacheDelivery(delivery); // Redis 캐시 갱신
        }
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void updateShippingToCompleted() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusSeconds(10);

        List<Delivery> shippingList =
            deliveryRepository.findByStatusAndStatusUpdatedAtBefore(
                DeliveryStatus.SHIPPING,
                threshold
            );

        if (shippingList.isEmpty()) {
            return;
        }

        log.info("[Scheduler] SHIPPING → COMPLETED 전환 대상: {}건", shippingList.size());

        for (Delivery delivery : shippingList) {
            delivery.markCompleted(now);
            deliveryCacheService.cacheDelivery(delivery); // Redis 캐시 갱신
        }
    }
}