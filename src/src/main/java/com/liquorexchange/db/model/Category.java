package com.liquorexchange.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Document
public class Category {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotBlank
    private String name;

    public Category(@NotBlank String name) {
        this.name = name;
    }
}