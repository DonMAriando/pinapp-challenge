package com.pinapp.challenge.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Required data to create a new client")
public class CreateClientRequest {
    
    @Schema(description = "Client's first name", example = "John", required = true)
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @Schema(description = "Client's last name", example = "Doe", required = true)
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @Schema(description = "Client's age in years", example = "30", minimum = "1", required = true)
    @NotNull(message = "Age is required")
    @Positive(message = "Age must be greater than 0")
    private Integer age;
    
    @Schema(description = "Client's birth date", example = "1994-01-15", required = true)
    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;
}
