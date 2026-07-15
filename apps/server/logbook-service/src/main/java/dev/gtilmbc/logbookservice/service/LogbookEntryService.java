package dev.gtilmbc.logbookservice.service;

import dev.gtilmbc.logbookservice.generated.model.CreateLogbookEntryRequest;
import dev.gtilmbc.logbookservice.generated.model.LogbookEntry;
import dev.gtilmbc.logbookservice.generated.model.LogbookEntryPage;
import dev.gtilmbc.logbookservice.generated.model.TransportMode;
import dev.gtilmbc.logbookservice.generated.model.UpdateLogbookEntryRequest;
import dev.gtilmbc.logbookservice.model.LogbookEntryEntity;
import dev.gtilmbc.logbookservice.repository.LogbookEntryRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogbookEntryService {

	private final LogbookEntryRepository repository;

	@Transactional(readOnly = true)
	public LogbookEntryPage findEntries(OffsetDateTime from, OffsetDateTime to, String q, TransportMode transportMode,
			int page, int size) {
		Specification<LogbookEntryEntity> specification = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (from != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("startTime"), from));
			}
			if (to != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("endTime"), to));
			}
			if (transportMode != null) {
				predicates.add(cb.equal(root.get("transportMode"), transportMode.getValue()));
			}
			if (q != null && !q.isBlank()) {
				String pattern = like(q);
				predicates.add(cb.or(cb.like(cb.lower(root.get("title")), pattern),
						cb.like(cb.lower(root.get("description")), pattern),
						cb.like(cb.lower(root.get("startCity")), pattern),
						cb.like(cb.lower(root.get("destinationCity")), pattern)));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		Page<LogbookEntryEntity> result = repository.findAll(specification, PageRequest.of(page, size));
		LogbookEntryPage response = new LogbookEntryPage();
		response.setItems(result.stream().map(this::toApiModel).toList());
		response.setPage(page);
		response.setSize(size);
		response.setTotalElements((int) result.getTotalElements());
		return response;
	}

	@Transactional(readOnly = true)
	public LogbookEntry getById(UUID id) {
		return toApiModel(getEntityById(id));
	}

	@Transactional
	public LogbookEntry create(CreateLogbookEntryRequest request) {
		validateTimeRange(request.getStartTime(), request.getEndTime());
		LogbookEntryEntity entity = new LogbookEntryEntity();
		applyRequest(entity, request);
		return toApiModel(repository.save(entity));
	}

	@Transactional
	public LogbookEntry update(UUID id, UpdateLogbookEntryRequest request) {
		validateTimeRange(request.getStartTime(), request.getEndTime());
		LogbookEntryEntity entity = getEntityById(id);
		applyRequest(entity, request);
		return toApiModel(repository.save(entity));
	}

	@Transactional
	public void delete(UUID id) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logbook entry not found");
		}
		repository.deleteById(id);
	}

	private void applyRequest(LogbookEntryEntity entity, CreateLogbookEntryRequest request) {
		entity.setTitle(request.getTitle());
		entity.setDescription(request.getDescription());
		entity.setStartTime(request.getStartTime());
		entity.setEndTime(request.getEndTime());
		entity.setStartCity(request.getStartCity());
		entity.setDestinationCity(request.getDestinationCity());
		entity.setStartStationId(request.getStartStationId());
		entity.setDestinationStationId(request.getDestinationStationId());
		entity.setTransportMode(request.getTransportMode().getValue());
	}

	private void applyRequest(LogbookEntryEntity entity, UpdateLogbookEntryRequest request) {
		entity.setTitle(request.getTitle());
		entity.setDescription(request.getDescription());
		entity.setStartTime(request.getStartTime());
		entity.setEndTime(request.getEndTime());
		entity.setStartCity(request.getStartCity());
		entity.setDestinationCity(request.getDestinationCity());
		entity.setStartStationId(request.getStartStationId());
		entity.setDestinationStationId(request.getDestinationStationId());
		entity.setTransportMode(request.getTransportMode().getValue());
	}

	private LogbookEntry toApiModel(LogbookEntryEntity entity) {
		LogbookEntry model = new LogbookEntry();
		model.setId(entity.getId());
		model.setTitle(entity.getTitle());
		model.setDescription(entity.getDescription());
		model.setStartTime(entity.getStartTime());
		model.setEndTime(entity.getEndTime());
		model.setStartCity(entity.getStartCity());
		model.setDestinationCity(entity.getDestinationCity());
		model.setStartStationId(entity.getStartStationId());
		model.setDestinationStationId(entity.getDestinationStationId());
		model.setTransportMode(TransportMode.fromValue(entity.getTransportMode()));
		model.setCreatedAt(entity.getCreatedAt());
		model.setUpdatedAt(entity.getUpdatedAt());
		return model;
	}

	private LogbookEntryEntity getEntityById(UUID id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logbook entry not found"));
	}

	private void validateTimeRange(OffsetDateTime startTime, OffsetDateTime endTime) {
		if (endTime.isBefore(startTime)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "endTime must be after or equal to startTime");
		}
	}

	private String like(String value) {
		return "%" + value.toLowerCase(Locale.ROOT).trim() + "%";
	}
}
