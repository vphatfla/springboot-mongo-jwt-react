package com.example.springjwtmongoreact.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Role {

    @Id
    private String id;

    private ERole name;
    public Role(){}

    public Role(ERole name)
    {
        this.name = name;
    }

    public ERole getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}
