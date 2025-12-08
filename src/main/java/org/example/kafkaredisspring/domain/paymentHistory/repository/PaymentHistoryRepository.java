package org.example.kafkaredisspring.domain.paymentHistory.repository;

import org.example.kafkaredisspring.common.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

}
