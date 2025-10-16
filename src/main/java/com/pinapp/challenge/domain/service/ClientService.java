package com.pinapp.challenge.domain.service;

import com.pinapp.challenge.domain.model.Client;
import com.pinapp.challenge.domain.model.ClientMetrics;
import com.pinapp.challenge.domain.port.in.CreateClientUseCase;
import com.pinapp.challenge.domain.port.in.GetAllClientsUseCase;
import com.pinapp.challenge.domain.port.in.GetClientMetricsUseCase;
import com.pinapp.challenge.domain.port.out.ClientRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService implements CreateClientUseCase, GetAllClientsUseCase, GetClientMetricsUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    public ClientService(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    @Override
    public Client createClient(Client client) {
        // Validate business rules
        if (client.getFirstName() == null || client.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (client.getLastName() == null || client.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (client.getAge() == null || client.getAge() <= 0) {
            throw new IllegalArgumentException("Age must be greater than 0");
        }
        if (client.getBirthDate() == null) {
            throw new IllegalArgumentException("Birth date is required");
        }

        return clientRepositoryPort.save(client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepositoryPort.findAll();
    }

    @Override
    public ClientMetrics getClientMetrics() {
        List<Client> clients = clientRepositoryPort.findAll();
        
        if (clients.isEmpty()) {
            return new ClientMetrics(0.0, 0.0, 0L);
        }

        // Calculate average age
        double averageAge = clients.stream()
                .mapToInt(Client::getAge)
                .average()
                .orElse(0.0);

        // Calculate standard deviation
        double standardDeviation = calculateStandardDeviation(clients, averageAge);

        return new ClientMetrics(averageAge, standardDeviation, (long) clients.size());
    }

    private double calculateStandardDeviation(List<Client> clients, double mean) {
        if (clients.size() <= 1) {
            return 0.0;
        }

        double sumSquaredDifferences = clients.stream()
                .mapToDouble(client -> Math.pow(client.getAge() - mean, 2))
                .sum();

        return Math.sqrt(sumSquaredDifferences / (clients.size() - 1));
    }
}
