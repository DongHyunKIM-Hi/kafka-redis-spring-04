package org.example.kafkaredisspring.domain.delivery.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafkaredisspring.common.entity.Delivery;
import org.example.kafkaredisspring.common.enums.DeliveryStatus;
import org.example.kafkaredisspring.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredisspring.domain.delivery.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryCacheService deliveryCacheService;

    @Transactional
    public void createDeliveryFromPayment(PaymentCompletedEvent event) {

        LocalDateTime paidAt = LocalDateTime.parse(
            event.getPaidAt(),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        );

        Delivery delivery = Delivery.builder()
            .orderId(event.getOrderId())
            .paymentId(event.getPaymentId())
            .productId(event.getProductId())
            .userId(event.getUserId())
            .status(DeliveryStatus.PREPARING)
            .paidAt(paidAt)
            .statusUpdatedAt(paidAt)
            .build();

        Delivery saved = deliveryRepository.save(delivery);

        log.info("[Delivery] 배송 준비 생성 - orderId={}, status={}",
            saved.getOrderId(), saved.getStatus());

        // Redis 캐시에도 함께 반영
        deliveryCacheService.cacheDelivery(saved);
    }
}