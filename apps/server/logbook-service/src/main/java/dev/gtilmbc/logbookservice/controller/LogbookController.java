package dev.gtilmbc.logbookservice.controller;

import dev.gtilmbc.logbookservice.generated.api.LogbookApi;
import dev.gtilmbc.logbookservice.generated.model.CreateLogbookEntryRequest;
import dev.gtilmbc.logbookservice.generated.model.LogbookEntry;
import dev.gtilmbc.logbookservice.generated.model.LogbookEntryPage;
import dev.gtilmbc.logbookservice.generated.model.TransportMode;
import dev.gtilmbc.logbookservice.generated.model.UpdateLogbookEntryRequest;
import dev.gtilmbc.logbookservice.service.LogbookEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.OffsetDateTime;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LogbookController implements LogbookApi {

    private final LogbookEntryService service;

    @Override
    public ResponseEntity<LogbookEntry> createLogbookEntry(CreateLogbookEntryRequest createLogbookEntryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(createLogbookEntryRequest));
    }

    @Override
    public ResponseEntity<Void> deleteLogbookEntry(UUID entryId) {
        service.delete(entryId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<LogbookEntryPage> getLogbookEntries(
        OffsetDateTime from,
        OffsetDateTime to,
        String q,
        TransportMode transportMode,
        Integer page,
        Integer size
    ) {
        return ResponseEntity.ok(service.findEntries(from, to, q, transportMode, page, size));
    }

    @Override
    public ResponseEntity<LogbookEntry> getLogbookEntryById(UUID entryId) {
        return ResponseEntity.ok(service.getById(entryId));
    }

    @Override
    public ResponseEntity<LogbookEntry> updateLogbookEntry(UUID entryId, UpdateLogbookEntryRequest updateLogbookEntryRequest) {
        return ResponseEntity.ok(service.update(entryId, updateLogbookEntryRequest));
    }
}
