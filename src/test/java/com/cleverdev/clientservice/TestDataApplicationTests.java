package com.cleverdev.clientservice;

import com.cleverdev.clientservice.api.ClientApi;
import com.cleverdev.clientservice.api.ClientNotesApi;
import com.cleverdev.clientservice.client.dto.ClientDto;
import com.cleverdev.clientservice.core.exceptionhandling.ExceptionData;
import com.cleverdev.clientservice.note.dto.ClientNoteDto;
import com.cleverdev.clientservice.note.dto.ClientNoteRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestDataApplicationTests {
    @Autowired
    private ClientApi clientApi;
    @Autowired
    private ClientNotesApi clientNotesApi;

    @Test
    void testGetClients() {
        final List<ClientDto> clients = clientApi.getClientsAndCheckStatus();
        assertThat(clients).hasSize(4);
    }

    @Test
    void testGetClientNotes() {
        testWrongAgencyCheck();
        testClientNotes();
    }

    private void testWrongAgencyCheck() {
        final LocalDate now = LocalDate.now();
        final ClientNoteRequestDto request = ClientNoteRequestDto.builder()
                .clientGuid("guid")
                .agency("unknownAgency")
                .dateFrom(now)
                .dateTo(now)
                .build();
        final ExceptionData error = clientNotesApi.getClientsNotesAndCheckError(request);
        assertThat(error.getMessage()).isEqualTo("Agency \"unknownAgency\" is not found");
    }

    private void testClientNotes() {
        final String agency = "agency1";
        final String guid = "17DD0E94-6A57-49C8-E933-CC480D1AC3FB";
        final String anotherAgency = "agency2";

        testClientNotes(agency, guid,
                LocalDate.of(1990, Month.JANUARY, 1),
                LocalDate.of(2030, Month.JANUARY, 1),
                4);

        testClientNotes(agency, guid,
                LocalDate.of(2020, Month.JANUARY, 1),
                LocalDate.of(2021, Month.JANUARY, 1),
                3);

        testClientNotes(agency, guid,
                LocalDate.of(2019, Month.DECEMBER, 31),
                LocalDate.of(2021, Month.JANUARY, 1),
                4);

        testClientNotes(anotherAgency, guid,
                LocalDate.of(1990, Month.JANUARY, 1),
                LocalDate.of(2030, Month.JANUARY, 1),
                0);
    }

    private void testClientNotes(String agency, String guid, LocalDate from, LocalDate to, int expectedSize) {
        final ClientNoteRequestDto requestAll = createRequest(agency, guid, from, to);
        final List<ClientNoteDto> notes = clientNotesApi.getClientsNotesAndCheckStatus(requestAll);
        assertThat(notes).hasSize(expectedSize);
    }

    private ClientNoteRequestDto createRequest(String agency, String guid, LocalDate from, LocalDate to) {
        return ClientNoteRequestDto.builder()
                .agency(agency)
                .clientGuid(guid)
                .dateFrom(from)
                .dateTo(to)
                .build();
    }

}
