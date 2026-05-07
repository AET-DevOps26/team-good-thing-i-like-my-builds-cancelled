package dev.gtilmbc.routeservice.controller;

import dev.gtilmbc.routeservice.model.Example;
import dev.gtilmbc.routeservice.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/example/")
public class ExampleController {

    @Autowired
    private ExampleService exampleService;

    @GetMapping
    public List<Example> getExamples() {
        return exampleService.getAll();
    }

    @GetMapping("{exampleId}")
    public Example getExample(@PathVariable Long exampleId) {
        return exampleService.get(exampleId);
    }

    @PostMapping
    public Example save(Example example) {
        return exampleService.save(example);
    }
}
