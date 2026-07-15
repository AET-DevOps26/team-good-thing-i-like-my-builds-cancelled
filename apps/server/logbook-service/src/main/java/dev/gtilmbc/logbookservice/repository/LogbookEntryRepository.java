package dev.gtilmbc.logbookservice.repository;

import dev.gtilmbc.logbookservice.model.LogbookEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogbookEntryRepository
		extends
			JpaRepository<LogbookEntryEntity, UUID>,
			JpaSpecificationExecutor<LogbookEntryEntity> {
}
