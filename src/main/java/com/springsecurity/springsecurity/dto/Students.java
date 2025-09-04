package com.springsecurity.springsecurity.dto;

import lombok.Data;

@Data
public class Students {

    private int id;
    private String name;
    private String marks;

    public Students(int id, String name, String marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

}
