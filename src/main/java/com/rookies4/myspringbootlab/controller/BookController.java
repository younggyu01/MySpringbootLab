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
    public ResponseEntity<BookDTO.Response> create(@RequestBody @Valid BookDTO.CreateRequest req) {
        return ResponseEntity.ok(bookService.createBook(req));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAll() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }

    //저자 검색
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO.Response>> getByAuthor(@RequestParam("author") String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    //제목 검색
    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO.Response>> getByTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> update(@PathVariable Long id,
                                                   @RequestBody @Valid BookDTO.UpdateRequest req) {
        return ResponseEntity.ok(bookService.updateBook(id, req));
    }

    //PATCH - Book
    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO.Response> patch(@PathVariable Long id,
                                                  @RequestBody @Valid BookDTO.PatchRequest req) {
        return ResponseEntity.ok(bookService.patchBook(id, req));
    }

    //PATCH - BookDetail
    @PatchMapping("/{id}/detail")
    public ResponseEntity<BookDTO.Response> patchDetail(@PathVariable Long id,
                                                        @RequestBody @Valid BookDTO.BookDetailPatchRequest req) {
        return ResponseEntity.ok(bookService.patchBookDetail(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
