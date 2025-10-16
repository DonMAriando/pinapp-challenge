package com.pinapp.challenge.domain.port.in;

import com.pinapp.challenge.domain.model.Client;

import java.util.List;

public interface CreateClientUseCase {
    Client createClient(Client client);
}
