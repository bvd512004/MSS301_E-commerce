package com.fu.productservice.config;

import com.fu.productservice.model.Book;
import com.fu.productservice.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class BookDataSeeder {

    @Bean
    CommandLineRunner seedBooks(BookRepository bookRepository) {
        return args -> {
            if (bookRepository.count() > 0) {
                return;
            }
            bookRepository.save(book("Dế Mèn Phiêu Lưu Ký", "Tô Hoài", "Thiếu nhi", "45000", 20));
            bookRepository.save(book("Số Đỏ", "Vũ Trọng Phụng", "Văn học", "68000", 15));
            bookRepository.save(book("Clean Code", "Robert C. Martin", "Công nghệ", "189000", 8));
        };
    }

    private Book book(String title, String author, String category, String price, int stock) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setPrice(new BigDecimal(price));
        book.setStock(stock);
        return book;
    }

}
