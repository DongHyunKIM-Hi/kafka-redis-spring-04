package org.example.kafkaredisspring.domain.delivery.listener;

import static org.example.kafkaredisspring.common.model.kafka.topic.KafkaTopics.TOPIC_PAYMENT_COMPLETED;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafkaredisspring.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredisspring.domain.delivery.service.DeliveryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryListener {

    private final DeliveryService deliveryService;

    @KafkaListener(
        topics = TOPIC_PAYMENT_COMPLETED,
        groupId = "delivery-group",
        containerFactory = "deliveryKafkaListenerContainerFactory"
    )
    public void consume(PaymentCompletedEvent event) {
        log.info("[Delivery-Consumer] 결제 완료 이벤트 수신 - orderId={}, paymentId={}", event.getOrderId(), event.getPaymentId());

  /*      // 테스트용 : 특정 productId에 대해서는 일부러 예외 발생
        if (event.getProductId() == 999L) {
            log.info("[Delivery-Consumer] 테스트용 예외 발생 - productId=999");
            throw new RuntimeException("테스트용 배송 에러");
        }*/

        deliveryService.createDeliveryFromPayment(event);
    }
}