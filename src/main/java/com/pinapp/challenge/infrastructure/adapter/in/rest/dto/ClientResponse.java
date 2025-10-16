package com.pinapp.challenge.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Complete client information")
public class ClientResponse {
    
    @Schema(description = "Client's unique identifier", example = "1")
    private Long id;
    
    @Schema(description = "Client's first name", example = "John")
    private String firstName;
    
    @Schema(description = "Client's last name", example = "Doe")
    private String lastName;
    
    @Schema(description = "Client's age in years", example = "30")
    private Integer age;
    
    @Schema(description = "Client's birth date", example = "1994-01-15")
    private LocalDate birthDate;
    
    @Schema(description = "Estimated death date (80 years from birth)", example = "2074-01-15")
    private LocalDate estimatedDeathDate;
}
