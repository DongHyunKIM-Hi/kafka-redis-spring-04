package org.example.kafkaredisspring.domain.productRanking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingDto {

    private String title;
    private double score;
}
