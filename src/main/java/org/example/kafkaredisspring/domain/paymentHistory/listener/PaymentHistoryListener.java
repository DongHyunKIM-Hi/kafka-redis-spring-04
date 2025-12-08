package org.example.kafkaredisspring.domain.paymentHistory.listener;

import static org.example.kafkaredisspring.common.model.kafka.topic.KafkaTopics.TOPIC_PAYMENT_COMPLETED;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafkaredisspring.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredisspring.domain.paymentHistory.service.PaymentHistoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentHistoryListener {

    private final PaymentHistoryService paymentHistoryService;

    @KafkaListener(
        topics = TOPIC_PAYMENT_COMPLETED,
        groupId = "payment-history-group",
        containerFactory = "paymentHistoryKafkaListenerContainerFactory"
    )
    public void consume(PaymentCompletedEvent event) {

        log.info("[Consumer-Record] 결제 완료 이벤트 수신 - paymentId={}, orderId={}, userId={}", event.getPaymentId(), event.getOrderId(), event.getUserId());

        paymentHistoryService.savePaymentHistory(event);
    }
}
