package com.cleverdev.clientservice.note.service;

import com.cleverdev.clientservice.client.service.ClientService;
import com.cleverdev.clientservice.note.dto.ClientNoteDto;
import com.cleverdev.clientservice.note.dto.ClientNoteRequestDto;
import com.cleverdev.clientservice.note.service.repository.ClientNoteRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientNoteService {
    @Value("${base.path}")
    private final String basePath;
    private final ResourceLoader resourceLoader;
    private final ClientNoteRepository clientNoteRepository;
    private final ClientService clientService;
    private final ObjectMapper objectMapper;

    @PostConstruct
    private void loadClientNotes() {
        for (final String agencyName : clientService.getAgencies()) {
            clientNoteRepository.initAgencyClientNotes(agencyName, readAgencyClientNotes(agencyName));
        }
    }

    @SneakyThrows
    private List<ClientNoteDto> readAgencyClientNotes(final String agency) {
        if (!StringUtils.isAlphanumeric(agency)) {
            return Collections.emptyList();
        }
        final String path = String.format("classpath:%s/data/notes/%s/notes.json", basePath, agency);
        final Resource resource = resourceLoader.getResource(path);
        if (resource.exists()) {
            return objectMapper.readValue(resource.getFile(), new TypeReference<>() {
            });
        }
        return Collections.emptyList();
    }

    public List<ClientNoteDto> getNotes(final ClientNoteRequestDto requestDto) {
        List<ClientNoteDto> listNotes = clientNoteRepository.getNotesByClientGuidAndAgencyInDateRange(
                readAgencyClientNotes(requestDto.getAgency()),
                requestDto.getClientGuid(), requestDto.getAgency(),
                requestDto.getDateFrom(), requestDto.getDateTo()
        );

        return listNotes;
    }

}
