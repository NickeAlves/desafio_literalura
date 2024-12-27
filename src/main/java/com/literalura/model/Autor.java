package com.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String birth_year;
    private String death_year;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Livro> livros = new ArrayList<>();

    public Autor() {
    }

    public Autor(DadosAutor dadosAutor) {
        this.name = dadosAutor.name();
        this.birth_year = dadosAutor.birth_year();
        this.death_year = dadosAutor.death_year();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getDeath_year() {
        return death_year;
    }

    public void setDeath_year(String death_year) {
        this.death_year = death_year;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = new ArrayList<>();
        livros.forEach(l -> {
            l.setAutor(this);
            this.livros.add(l);
        });
    }

    @Override
    public String toString() {
        List<String> livros = this.getLivros()
                .stream()
                .map(Livro::getTitle).toList();
        return "\n***   Autor   ***" +
                "\nNome: " + name +
                "\nBirth year: " + birth_year +
                "\nDeath year: " + death_year +
                "\nLivros: " + livros +
                "\n****************************\n";
    }
}
