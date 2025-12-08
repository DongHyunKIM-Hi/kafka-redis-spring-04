package org.example.kafkaredisspring.domain.delivery.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kafkaredisspring.common.enums.DeliveryStatus;

@Getter
@AllArgsConstructor
public class DeliveryResponse {

    private Long deliveryId;
    private Long orderId;
    private Long productId;
    private DeliveryStatus status;
    private String statusUpdatedAt; // 문자열로 내려줘도 충분합니다.
}
