package org.example.kafkaredisspring.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kafkaredisspring.common.enums.Category;
import org.example.kafkaredisspring.common.model.kafka.event.PaymentCompletedEvent;

@Entity
@Table(name = "payment_histories")
@Getter
@NoArgsConstructor
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long paymentId;
    private Long orderId;
    private Long productId;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private Category category;
    private int quantity;

    private LocalDateTime paidAt;

    private PaymentHistory(Long paymentId, Long orderId, Long productId, Long userId, Category category, int quantity, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.productId = productId;
        this.userId = userId;
        this.category = category;
        this.quantity = quantity;
        this.paidAt = paidAt;
    }

    public static PaymentHistory from(PaymentCompletedEvent event) {
        return new PaymentHistory(
            event.getPaymentId(),
            event.getOrderId(),
            event.getProductId(),
            event.getUserId(),
            event.getCategory(),
            event.getQuantity(),
            LocalDateTime.parse(event.getPaidAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}
