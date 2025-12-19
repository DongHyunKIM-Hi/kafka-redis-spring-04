package org.example.kafkaredisspring.domain.paymentHistory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafkaredisspring.common.entity.PaymentHistory;
import org.example.kafkaredisspring.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredisspring.domain.paymentHistory.repository.PaymentHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentHistoryService {

    private final PaymentHistoryRepository historyRepository;

    @Transactional
    public void savePaymentHistory(PaymentCompletedEvent event) {

        // 이미 처리된 paymentId라면 재처리하지 않습니다.
        if (historyRepository.existsByPaymentId(event.getPaymentId())) {
            log.info("[DB] 이미 처리된 결제입니다. (paymentId={}) - 재처리 스킵", event.getPaymentId());
            return;
        }

        PaymentHistory paymentHistory = PaymentHistory.from(event);

        historyRepository.save(paymentHistory);

        log.info("[DB] 결제 기록 저장 완료 - paymentId={}, orderId={}, productId={}", event.getPaymentId(), event.getOrderId(), event.getProductId());
    }

}
