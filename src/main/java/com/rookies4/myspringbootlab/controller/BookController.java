package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    // POST /api/books : 새 도서 등록
    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // GET /api/books : 전체 조회
    @GetMapping
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    // ID로 조회 ResponseEntity<Book>
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).<Book>build()); // ★ 타입 명시
    }

    // GET /api/books/isbn/{isbn} : ISBN 조회 (BusinessException + Advice 404 처리)
    @GetMapping("/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found (isbn=" + isbn + ")", HttpStatus.NOT_FOUND));
    }

    // ResponseEntity<Book>
    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book req) {
        return bookRepository.findById(id)
                .map(b -> {
                    b.setTitle(req.getTitle());
                    b.setAuthor(req.getAuthor());
                    b.setIsbn(req.getIsbn());
                    b.setPrice(req.getPrice());
                    b.setPublishDate(req.getPublishDate());
                    return ResponseEntity.ok(bookRepository.save(b));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).<Book>build()); // ★ 타입 명시
    }

    // 삭제 (ResponseEntity<Void>)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
