package dev.gtilmbc.logbookservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "logbook_entries")
@Getter
@Setter
public class LogbookEntryEntity {

	@Id
	private UUID id;

	@Column(nullable = false, length = 120)
	private String title;

	@Column(length = 4000)
	private String description;

	@Column(nullable = false)
	private OffsetDateTime startTime;

	@Column(nullable = false)
	private OffsetDateTime endTime;

	@Column(nullable = false)
	private String startCity;

	private String startStationId;

	@Column(nullable = false)
	private String destinationCity;

	private String destinationStationId;

	@Column(nullable = false, length = 20)
	private String transportMode;

	@Column(nullable = false)
	private OffsetDateTime createdAt;

	@Column(nullable = false)
	private OffsetDateTime updatedAt;

	@PrePersist
	public void onCreate() {
		OffsetDateTime now = OffsetDateTime.now();
		if (id == null) {
			id = UUID.randomUUID();
		}
		if (createdAt == null) {
			createdAt = now;
		}
		updatedAt = now;
	}

	@PreUpdate
	public void onUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}
