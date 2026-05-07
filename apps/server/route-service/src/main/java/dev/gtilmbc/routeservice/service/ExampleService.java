package dev.gtilmbc.routeservice.service;

import dev.gtilmbc.routeservice.model.Example;
import dev.gtilmbc.routeservice.repository.ExampleRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@NoArgsConstructor
public class ExampleService {

    @Autowired
    private ExampleRepository exampleRepository;

    @GetMapping
    public List<Example> getAll() {
        return exampleRepository.findAll();
    }

    public Example get(Long id) {
        return exampleRepository.findById(id).orElse(null);
    }

    public Example save(Example example) {
        return exampleRepository.save(example);
    }
}
