package com.baibei.authservice.entity;

import lombok.Data;

@Data
public class Role {

    private Long id;
    private String name;

    public Role(String role) {
        this.name = role;
    }

}
