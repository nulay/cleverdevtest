package com.cleverdev.clientservice.api;

import com.cleverdev.clientservice.client.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Service
@RequiredArgsConstructor
public class ClientApi {
    private final TestRestTemplate testRestTemplate;

    public List<ClientDto> getClientsAndCheckStatus() {
        final ResponseEntity<List<ClientDto>> response = testRestTemplate.exchange("/clients",
                HttpMethod.POST,
                new HttpEntity<Void>(null, null),
                new ParameterizedTypeReference<>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }
}
