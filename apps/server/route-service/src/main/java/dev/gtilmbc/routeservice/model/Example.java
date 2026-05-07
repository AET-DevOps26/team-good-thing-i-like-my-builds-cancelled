package dev.gtilmbc.routeservice.model;

import jakarta.persistence.*;

@Entity
@Table(name="example")
public class Example {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
}
