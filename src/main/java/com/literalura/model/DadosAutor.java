package com.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAutor(@JsonAlias("name") String name,
                         @JsonAlias("birth_year") String birth_year,
                         @JsonAlias("death_year") String death_year) {
}
