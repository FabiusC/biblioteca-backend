package com.library.controllers;

import com.library.domain.entities.Book;
import com.library.domain.entities.Copy;
import com.library.domain.services.BookService;
import com.library.dto.requests.BookRequest;
import com.library.dto.responses.BookResponse;
import com.library.dto.responses.CopyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        Book createdBook = bookService.create(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(createdBook));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        return ResponseEntity.ok(bookService.findAll().stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(bookService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(toResponse(bookService.update(id, toEntity(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{isbn}/available-copies")
    public ResponseEntity<List<CopyResponse>> availableCopies(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.findAvailableCopiesByIsbn(isbn).stream().map(this::toResponse).toList());
    }

    private Book toEntity(BookRequest request) {
        return Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .edition(request.getEdition())
                .publicationDate(request.getPublicationDate())
                .author(request.getAuthor())
                .build();
    }

    private BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .edition(book.getEdition())
                .publicationDate(book.getPublicationDate())
                .author(book.getAuthor())
                .build();
    }

    private CopyResponse toResponse(Copy copy) {
        return CopyResponse.builder()
                .id(copy.getId())
                .bookId(copy.getBook().getId())
                .isbn(copy.getBook().getIsbn())
                .status(copy.getStatus())
                .build();
    }
}