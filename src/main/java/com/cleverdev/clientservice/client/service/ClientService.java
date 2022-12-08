package com.cleverdev.clientservice.client.service;

import com.cleverdev.clientservice.client.dto.ClientDto;
import com.cleverdev.clientservice.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public List<ClientDto> getClients() {
        List<ClientDto> clientDtos = clientRepository.getClients();
        return clientDtos;
    }

    public List<String> getAgencies() {
        return clientRepository.getAgencies();
    }

}
