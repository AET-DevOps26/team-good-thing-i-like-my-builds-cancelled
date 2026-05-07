package dev.gtilmbc.routeservice.repository;

import dev.gtilmbc.routeservice.model.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {

}
