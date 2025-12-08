package org.example.kafkaredisspring.domain.payment.producer;

import static org.example.kafkaredisspring.common.model.kafka.topic.KafkaTopics.TOPIC_PAYMENT_COMPLETED;

import lombok.RequiredArgsConstructor;
import org.example.kafkaredisspring.common.model.kafka.event.PaymentCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedEventKafkaTemplate;

    public void send(PaymentCompletedEvent event) {
        paymentCompletedEventKafkaTemplate.send(TOPIC_PAYMENT_COMPLETED, event);
    }

}
