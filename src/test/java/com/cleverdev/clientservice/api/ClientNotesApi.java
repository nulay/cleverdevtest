package com.cleverdev.clientservice.api;

import com.cleverdev.clientservice.core.exceptionhandling.ExceptionData;
import com.cleverdev.clientservice.note.dto.ClientNoteDto;
import com.cleverdev.clientservice.note.dto.ClientNoteRequestDto;
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
public class ClientNotesApi {
    private final TestRestTemplate testRestTemplate;

    public List<ClientNoteDto> getClientsNotesAndCheckStatus(ClientNoteRequestDto request) {
        final ResponseEntity<List<ClientNoteDto>> response = testRestTemplate.exchange("/notes",
                HttpMethod.POST,
                new HttpEntity<>(request, null),
                new ParameterizedTypeReference<>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    public ExceptionData getClientsNotesAndCheckError(ClientNoteRequestDto request) {
        final ResponseEntity<ExceptionData> response = testRestTemplate.postForEntity("/notes",
                new HttpEntity<>(request, null),
                ExceptionData.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.getBody();
    }
}
