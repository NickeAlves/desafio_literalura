package com.literalura.repository;

import com.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    List<Autor> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM Autor a WHERE a.name LIKE %:name%")
    Optional<Autor> findByName(@Param("name") String name);

    @Query("SELECT a FROM Autor a WHERE :year BETWEEN CAST(a.birth_year AS integer) AND CAST(a.death_year AS integer)")
    List<Autor> findAuthorsAlive(@Param("year") int year);
}
