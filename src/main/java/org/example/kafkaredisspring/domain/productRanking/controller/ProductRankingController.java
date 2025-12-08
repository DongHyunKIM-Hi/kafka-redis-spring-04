package org.example.kafkaredisspring.domain.productRanking.controller;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.kafkaredisspring.domain.productRanking.model.dto.RankingDto;
import org.example.kafkaredisspring.domain.productRanking.service.ProductRankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking/product")
public class ProductRankingController {

    private final ProductRankingService productRankingService;

    @GetMapping("/today")
    public ResponseEntity<List<RankingDto>> findProductRankingTop3InToday() {
        return ResponseEntity.ok(productRankingService.findProductRankingTop3InToday());
    }
}
