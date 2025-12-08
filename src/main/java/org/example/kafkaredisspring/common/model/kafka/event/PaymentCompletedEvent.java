package org.example.kafkaredisspring.common.model.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kafkaredisspring.common.enums.Category;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {

    private Long paymentId;    // 결제 ID (PG사나 내부 결제 번호)
    private Long orderId;      // 주문 번호
    private Long productId;    // 상품 ID
    private Long userId;       // 사용자 ID
    private Category category;   // 카테고리 ID
    private int quantity;      // 결제된 상품 수량
    private String paidAt;     // 결제 완료 시각 (예: "2025-11-28T10:15:30")
}