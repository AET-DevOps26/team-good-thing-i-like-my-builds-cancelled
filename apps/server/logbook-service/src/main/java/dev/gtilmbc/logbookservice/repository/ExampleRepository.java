package dev.gtilmbc.logbookservice.repository;

import dev.gtilmbc.logbookservice.model.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {

}
