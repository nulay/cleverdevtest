package com.cleverdev.clientservice.client.controller;

import com.cleverdev.clientservice.client.dto.ClientDto;
import com.cleverdev.clientservice.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public List<ClientDto> getClients() {
        return clientService.getClients();
    }
}
