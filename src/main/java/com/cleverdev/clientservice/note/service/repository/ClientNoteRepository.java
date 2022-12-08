package com.cleverdev.clientservice.note.service.repository;

import com.cleverdev.clientservice.note.dto.ClientNoteDto;
import com.cleverdev.clientservice.note.utils.DateCompareUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientNoteRepository {
    public static final String AGENCY_NOT_FOUND_MESSAGE_TEMPLATE = "Agency \"%s\" is not found";

    private final Map<String, List<ClientNoteDto>> notesByAgency = new ConcurrentHashMap<>();

    public List<ClientNoteDto> getNotesByClientGuidAndAgencyInDateRange(final String clientGuid, final String agency,
                                                                        final LocalDate from, final LocalDate to) {
        if (!notesByAgency.containsKey(agency)) {
            throw new IllegalArgumentException(String.format(AGENCY_NOT_FOUND_MESSAGE_TEMPLATE, agency));
        }
        final List<ClientNoteDto> clientNotes = notesByAgency.get(agency);
        return getNotesByPeriodAndClientGuid(clientNotes, clientGuid, from, to);
    }

    public List<ClientNoteDto> getNotesByClientGuidAndAgencyInDateRange(List<ClientNoteDto> clientNotes, final String clientGuid,
                                                                        final String agency,
                                                                        final LocalDate from, final LocalDate to) {
        if (!notesByAgency.containsKey(agency)) {
            throw new IllegalArgumentException(String.format(AGENCY_NOT_FOUND_MESSAGE_TEMPLATE, agency));
        }
        return getNotesByPeriodAndClientGuid(clientNotes, clientGuid, from, to);
    }

    public void initAgencyClientNotes(final String agency, final List<ClientNoteDto> clientNotes) {
        notesByAgency.put(agency, List.copyOf(clientNotes));
    }

    private List<ClientNoteDto> getNotesByPeriodAndClientGuid(final List<ClientNoteDto> notes, final String clientGuid,
                                                              final LocalDate from, final LocalDate to) {
        List<ClientNoteDto> filteredNotes = notes.stream()
                .filter(note -> Objects.equals(clientGuid, note.getClientGuid())
                        && DateCompareUtils.isDateInPeriod(note.getCreatedDateTime().toLocalDate(),
                        from, to))
                .collect(Collectors.toList());
        return filteredNotes;
    }

}
