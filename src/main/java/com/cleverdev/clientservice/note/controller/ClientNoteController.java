package com.cleverdev.clientservice.note.controller;

import com.cleverdev.clientservice.note.dto.ClientNoteDto;
import com.cleverdev.clientservice.note.dto.ClientNoteRequestDto;
import com.cleverdev.clientservice.note.service.ClientNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class ClientNoteController {
    private final ClientNoteService clientNoteService;

    @PostMapping
    public List<ClientNoteDto> getNotes(@RequestBody @Validated @NotNull final ClientNoteRequestDto clientNoteRequestDto) {
        return clientNoteService.getNotes(clientNoteRequestDto);
    }
}
