package com.pinapp.challenge.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Statistical metrics of clients")
public class ClientMetricsResponse {
    
    @Schema(description = "Average age of all clients", example = "35.5")
    private Double averageAge;
    
    @Schema(description = "Standard deviation of client ages", example = "12.3")
    private Double standardDeviation;
    
    @Schema(description = "Total number of clients registered in the system", example = "10")
    private Long totalClients;
}
