package main.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class GlobalSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;
}
