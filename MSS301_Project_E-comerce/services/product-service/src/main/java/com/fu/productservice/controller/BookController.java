package com.fu.productservice.controller;

import com.fu.productservice.dto.BookRequest;
import com.fu.productservice.dto.BookResponse;
import com.fu.productservice.dto.QuantityRequest;
import com.fu.productservice.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponse> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category
    ) {
        return bookService.search(title, author, category);
    }

    @GetMapping("/{id}")
    public BookResponse findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        BookResponse created = bookService.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return bookService.update(id, request);
    }

    @PostMapping("/{id}/reserve")
    public BookResponse reserve(@PathVariable Long id, @Valid @RequestBody QuantityRequest request) {
        return bookService.reserve(id, request.quantity());
    }

    @PostMapping("/{id}/release")
    public BookResponse release(@PathVariable Long id, @Valid @RequestBody QuantityRequest request) {
        return bookService.release(id, request.quantity());
    }

}
