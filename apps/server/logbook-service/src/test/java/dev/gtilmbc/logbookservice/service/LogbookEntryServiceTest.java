package dev.gtilmbc.logbookservice.service;

import dev.gtilmbc.logbookservice.generated.model.CreateLogbookEntryRequest;
import dev.gtilmbc.logbookservice.generated.model.LogbookEntryPage;
import dev.gtilmbc.logbookservice.generated.model.TransportMode;
import dev.gtilmbc.logbookservice.model.LogbookEntryEntity;
import dev.gtilmbc.logbookservice.repository.LogbookEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogbookEntryServiceTest {

	@Mock
	private LogbookEntryRepository repository;

	@InjectMocks
	private LogbookEntryService service;

	@Test
	void createShouldRejectInvalidTimeRange() {
		OffsetDateTime start = OffsetDateTime.parse("2026-07-01T11:00:00Z");
		OffsetDateTime end = OffsetDateTime.parse("2026-07-01T10:00:00Z");

		CreateLogbookEntryRequest request = new CreateLogbookEntryRequest("Trip", start, end, "Munich", "Berlin",
				TransportMode.TRAIN);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.create(request));

		assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		verify(repository, never()).save(any(LogbookEntryEntity.class));
	}

	@Test
	void deleteShouldThrowNotFoundWhenEntryDoesNotExist() {
		UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
		when(repository.existsById(id)).thenReturn(false);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.delete(id));

		assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		verify(repository, never()).deleteById(any(UUID.class));
	}

	@Test
	void findEntriesShouldMapEntityPageToApiPage() {
		LogbookEntryEntity entity = new LogbookEntryEntity();
		entity.setId(UUID.fromString("22222222-2222-2222-2222-222222222222"));
		entity.setTitle("Morning Commute");
		entity.setDescription("Fast train");
		entity.setStartCity("Munich");
		entity.setDestinationCity("Berlin");
		entity.setStartStationId("8000261");
		entity.setDestinationStationId("8011160");
		entity.setTransportMode("TRAIN");
		entity.setStartTime(OffsetDateTime.parse("2026-07-01T09:00:00Z"));
		entity.setEndTime(OffsetDateTime.parse("2026-07-01T13:00:00Z"));
		entity.setCreatedAt(OffsetDateTime.parse("2026-07-01T08:00:00Z"));
		entity.setUpdatedAt(OffsetDateTime.parse("2026-07-01T08:30:00Z"));

		PageRequest pageable = PageRequest.of(0, 8);
		when(repository.findAll(org.mockito.ArgumentMatchers.<Specification<LogbookEntryEntity>>any(), eq(pageable)))
				.thenReturn(new PageImpl<>(List.of(entity), pageable, 1));

		LogbookEntryPage page = service.findEntries(null, null, "  munich ", TransportMode.TRAIN, 0, 8);

		assertThat(page.getPage()).isEqualTo(0);
		assertThat(page.getSize()).isEqualTo(8);
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getItems()).hasSize(1);
		assertThat(page.getItems().getFirst().getTitle()).isEqualTo("Morning Commute");
		assertThat(page.getItems().getFirst().getTransportMode()).isEqualTo(TransportMode.TRAIN);
	}
}
