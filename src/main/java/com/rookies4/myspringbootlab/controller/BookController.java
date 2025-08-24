package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO.BookResponse> create(@RequestBody @Valid BookDTO.BookCreateRequest req) {
        return ResponseEntity.ok(bookService.createBook(req));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO.BookResponse>> getAll() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.BookResponse> getByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }

    // 저자 조회
    @GetMapping("/author")
    public ResponseEntity<List<BookDTO.BookResponse>> getByAuthor(@RequestParam String name) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> update(@PathVariable Long id,
                                                       @RequestBody @Valid BookDTO.BookUpdateRequest req) {
        return ResponseEntity.ok(bookService.updateBook(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}