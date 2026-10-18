package com.fu.productservice.service;

import com.fu.productservice.dto.BookRequest;
import com.fu.productservice.dto.BookResponse;
import com.fu.productservice.exception.BadRequestException;
import com.fu.productservice.exception.NotFoundException;
import com.fu.productservice.model.Book;
import com.fu.productservice.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> search(String title, String author, String category) {
        return bookRepository.search(blankToNull(title), blankToNull(author), blankToNull(category))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public BookResponse create(BookRequest request) {
        Book book = new Book();
        apply(book, request);
        return toResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = getOrThrow(id);
        apply(book, request);
        return toResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse reserve(Long id, int quantity) {
        Book book = getOrThrow(id);
        if (book.getStock() < quantity) {
            throw new BadRequestException("Không đủ tồn kho cho sách \"" + book.getTitle() + "\". Còn " + book.getStock());
        }
        book.setStock(book.getStock() - quantity);
        return toResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse release(Long id, int quantity) {
        Book book = getOrThrow(id);
        book.setStock(book.getStock() + quantity);
        return toResponse(bookRepository.save(book));
    }

    private Book getOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sách " + id));
    }

    private void apply(Book book, BookRequest request) {
        book.setTitle(request.title().trim());
        book.setAuthor(request.author().trim());
        book.setCategory(request.category().trim());
        book.setPrice(request.price());
        book.setStock(request.stock());
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getPrice(),
                book.getStock()
        );
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

}
