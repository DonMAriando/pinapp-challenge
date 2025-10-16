package com.pinapp.challenge.infrastructure.adapter.in.rest;

import com.pinapp.challenge.domain.model.Client;
import com.pinapp.challenge.domain.model.ClientMetrics;
import com.pinapp.challenge.domain.port.in.CreateClientUseCase;
import com.pinapp.challenge.domain.port.in.GetAllClientsUseCase;
import com.pinapp.challenge.domain.port.in.GetClientMetricsUseCase;
import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.ClientMetricsResponse;
import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.ClientResponse;
import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.CreateClientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client Management", description = "API para gestión de clientes")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final GetAllClientsUseCase getAllClientsUseCase;
    private final GetClientMetricsUseCase getClientMetricsUseCase;

    public ClientController(CreateClientUseCase createClientUseCase,
                           GetAllClientsUseCase getAllClientsUseCase,
                           GetClientMetricsUseCase getClientMetricsUseCase) {
        this.createClientUseCase = createClientUseCase;
        this.getAllClientsUseCase = getAllClientsUseCase;
        this.getClientMetricsUseCase = getClientMetricsUseCase;
    }

        @Operation(
                summary = "Create new client",
                description = "Registers a new client in the system with first name, last name, age and birth date"
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Client created successfully",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ClientResponse.class),
                                examples = @ExampleObject(
                                        value = "{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 30, \"birthDate\": \"1994-01-15\", \"estimatedDeathDate\": \"2074-01-15\"}"
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input data",
                        content = @Content
                )
        })
        @PostMapping
        public ResponseEntity<ClientResponse> createClient(
                @Parameter(description = "Client data to create", required = true)
                @Valid @RequestBody CreateClientRequest request) {
            Client client = new Client(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getAge(),
                    request.getBirthDate()
            );

            Client createdClient = createClientUseCase.createClient(client);
            ClientResponse response = toClientResponse(createdClient);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

    @Operation(
            summary = "List all clients",
            description = "Gets the complete list of registered clients with their data and estimated death date"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Client list retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClientResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - authentication required",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "basicAuth")
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        List<Client> clients = getAllClientsUseCase.getAllClients();
        List<ClientResponse> responses = clients.stream()
                .map(this::toClientResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Get client metrics",
            description = "Calculates and returns statistical metrics about clients: average age, standard deviation and total clients"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Metrics calculated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClientMetricsResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"averageAge\": 35.5, \"standardDeviation\": 12.3, \"totalClients\": 10}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - authentication required",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/metrics")
    public ResponseEntity<ClientMetricsResponse> getClientMetrics() {
        ClientMetrics metrics = getClientMetricsUseCase.getClientMetrics();
        ClientMetricsResponse response = ClientMetricsResponse.builder()
                .averageAge(metrics.getAverageAge())
                .standardDeviation(metrics.getStandardDeviation())
                .totalClients(metrics.getTotalClients())
                .build();

        return ResponseEntity.ok(response);
    }

    private ClientResponse toClientResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .age(client.getAge())
                .birthDate(client.getBirthDate())
                .estimatedDeathDate(client.calculateLifeExpectancy())
                .build();
    }
}
