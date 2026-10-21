package com.evidencia1.dto;

import com.evidencia1.model.StudentModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import tools.jackson.databind.ObjectMapper;

public record StudentDto(
        @JsonIgnore Integer id,
        String name,
        String lastName,
        String phone,
        String eMail) {

    public StudentModel toModel() {
        return new StudentModel(
                this.id,
                this.name,
                this.lastName,
                this.phone,
                this.eMail);
    }

    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

}
