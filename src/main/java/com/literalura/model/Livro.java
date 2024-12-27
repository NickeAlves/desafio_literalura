package com.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne(fetch = FetchType.EAGER)
    private Autor autor;
    private List<String> languages = new ArrayList<>();
    private int downloadCount;

    public Livro() {
    }

    public Livro(DadosLivro dadosLivro) {
        this.title = dadosLivro.title();
        this.languages = dadosLivro.languages();
        this.downloadCount = dadosLivro.downloadCount();
        this.autor = new Autor(dadosLivro.autor().get(0));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "\n***   Livro   ***" +
                "\nTitulo: " + title +
                "\nAutor: " + autor.getName() +
                "\nIdioma: " + languages +
                "\nDownloads: " + downloadCount +
                "\n*****************\n";

    }
}
