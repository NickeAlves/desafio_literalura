package com.literalura.dto;

import com.literalura.model.Autor;

import java.util.List;

public record LivroDTO(Long id,
                       String title,
                       List<Autor> authors,
                       List<String> languages,
                       Integer downloadCount,
                       String cover) {
}
