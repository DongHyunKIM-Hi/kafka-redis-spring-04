package org.example.kafkaredisspring.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kafkaredisspring.common.enums.DeliveryStatus;

@Entity
@Table(name = "deliveries")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long paymentId;
    private Long productId;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;    // PREPARING / SHIPPING / COMPLETED

    private LocalDateTime paidAt;          // 결제 완료 시각
    private LocalDateTime statusUpdatedAt; // 마지막 상태 변경 시각

    public void markShipping(LocalDateTime now) {       // 배송 시작
        this.status = DeliveryStatus.SHIPPING;
        this.statusUpdatedAt = now;
    }

    public void markCompleted(LocalDateTime now) {      // 배송 완료
        this.status = DeliveryStatus.COMPLETED;
        this.statusUpdatedAt = now;
    }
}