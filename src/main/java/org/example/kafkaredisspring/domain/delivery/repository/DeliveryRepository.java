package org.example.kafkaredisspring.domain.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.example.kafkaredisspring.common.entity.Delivery;
import org.example.kafkaredisspring.common.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // 스케줄러에서 상태 변경 대상 조회할 때 사용
    List<Delivery> findByStatusAndStatusUpdatedAtBefore(
        DeliveryStatus status,
        LocalDateTime dateTime
    );

    // 사용자별 배송 목록 조회
    List<Delivery> findByUserIdOrderByStatusUpdatedAtDesc(Long userId);
}
