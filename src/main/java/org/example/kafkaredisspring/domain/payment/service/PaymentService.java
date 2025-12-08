package org.example.kafkaredisspring.domain.payment.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.example.kafkaredisspring.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredisspring.domain.payment.model.request.CompletePaymentRequest;
import org.example.kafkaredisspring.domain.payment.producer.PaymentProducer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentProducer producer;

    public void paymentComplete(CompletePaymentRequest request) {

        // 결제 PG 사 통신 로직
        // 실제 결제 시도 로직 들어가는 가리


        // 결제가 최종적으로 성공했으면 해당 메시지 발행
        // 파싱 에러를 막기 위한 포멧팅
        String paidAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        PaymentCompletedEvent event = PaymentCompletedEvent.builder()
            .paymentId(request.getPaymentId())
            .orderId(request.getOrderId())
            .productId(request.getProductId())
            .userId(request.getUserId())
            .category(request.getCategory())
            .quantity(request.getQuantity())
            .paidAt(paidAt)
            .build();


        producer.send(event);
    }
}
