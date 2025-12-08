package org.example.kafkaredisspring.domain.productRanking.listener;

import static org.example.kafkaredisspring.common.model.kafka.topic.KafkaTopics.TOPIC_PAYMENT_COMPLETED;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafkaredisspring.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredisspring.domain.productRanking.service.ProductRankingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductRankingListener {

    private final ProductRankingService productRankingService;

    @KafkaListener(
        topics = TOPIC_PAYMENT_COMPLETED,
        groupId = "product-ranking-group",
        containerFactory = "productRankingKafkaListenerContainerFactory"
    )
    public void consume(PaymentCompletedEvent event) {

        LocalDateTime paidAt = LocalDateTime.parse(event.getPaidAt());

        LocalDate currentDate = paidAt.toLocalDate();

        productRankingService.increaseProductRanking(event.getProductId(),currentDate);
    }
}
