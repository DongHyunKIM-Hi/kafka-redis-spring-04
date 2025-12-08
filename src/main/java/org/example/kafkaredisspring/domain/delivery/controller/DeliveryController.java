package org.example.kafkaredisspring.domain.delivery.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.kafkaredisspring.domain.delivery.model.response.DeliveryResponse;
import org.example.kafkaredisspring.domain.delivery.service.DeliveryCacheService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryCacheService deliveryCacheService;

    @GetMapping
    public List<DeliveryResponse> getUserDeliveries(@RequestParam Long userId) {
        return deliveryCacheService.getUserDeliveries(userId);
    }
}
