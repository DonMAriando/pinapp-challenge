package com.pinapp.challenge.infrastructure.adapter.out.persistence;

import com.pinapp.challenge.domain.model.Client;
import com.pinapp.challenge.domain.port.out.ClientRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ClientRepositoryAdapter implements ClientRepositoryPort {

    private final ClientJpaRepository clientJpaRepository;

    public ClientRepositoryAdapter(ClientJpaRepository clientJpaRepository) {
        this.clientJpaRepository = clientJpaRepository;
    }

    @Override
    public Client save(Client client) {
        ClientEntity entity = toEntity(client);
        ClientEntity savedEntity = clientJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Client> findAll() {
        return clientJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        clientJpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return clientJpaRepository.count();
    }

    private ClientEntity toEntity(Client client) {
        return ClientEntity.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .age(client.getAge())
                .birthDate(client.getBirthDate())
                .build();
    }

    private Client toDomain(ClientEntity entity) {
        return new Client(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAge(),
                entity.getBirthDate()
        );
    }
}
