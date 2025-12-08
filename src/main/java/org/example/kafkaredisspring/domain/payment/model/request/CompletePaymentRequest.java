package org.example.kafkaredisspring.domain.payment.model.request;

import lombok.Getter;
import org.example.kafkaredisspring.common.enums.Category;

@Getter
public class CompletePaymentRequest {

    private Long orderId;
    private Long paymentId;
    private Long productId;
    private Long userId;
    private Category category;
    private int quantity;
}