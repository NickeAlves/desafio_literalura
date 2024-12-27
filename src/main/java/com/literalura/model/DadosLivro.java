package com.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(@JsonAlias("title") String title,
                         @JsonAlias("authors") List<DadosAutor> autor,
                         @JsonAlias("languages") List<String> languages,
                         @JsonAlias("download_count") int downloadCount) {

}
