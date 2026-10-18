package com.fu.productservice.repository;

import com.fu.productservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
            SELECT b FROM Book b
            WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))
              AND (:category IS NULL OR LOWER(b.category) LIKE LOWER(CONCAT('%', :category, '%')))
            ORDER BY b.title
            """)
    List<Book> search(
            @Param("title") String title,
            @Param("author") String author,
            @Param("category") String category
    );

}
