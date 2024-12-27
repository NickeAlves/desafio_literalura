package com.literalura.repository;

import com.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("SELECT COUNT(b) > 0 FROM Livro b WHERE b.title LIKE %:title%")
    Boolean verifiedBDExistence(@Param("title") String title);

    @Query(value = "SELECT * FROM livros WHERE :language = ANY (livros.languages)", nativeQuery = true)
    List<Livro> findBooksByLanguage(@Param("language") String language);

    @Query("SELECT l FROM Livro l ORDER BY l.downloadCount DESC LIMIT 10")
    List<Livro> findByTop10ByOrderDownloadCountDesc();
}
