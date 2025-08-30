package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.controller.dto.PublisherDTO;
import com.rookies4.myspringbootlab.service.BookService;
import com.rookies4.myspringbootlab.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<PublisherDTO.SimpleResponse>> getAll() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PublisherDTO.Response> getByName(@PathVariable String name) {
        return ResponseEntity.ok(publisherService.getPublisherByName(name));
    }

    //출판사별 도서 목록
    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO.Response>> getBooks(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBooksByPublisherId(id));
    }

    @PostMapping
    public ResponseEntity<PublisherDTO.Response> create(@RequestBody PublisherDTO.Request req) {
        return ResponseEntity.ok(publisherService.createPublisher(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> update(@PathVariable Long id,
                                                        @RequestBody PublisherDTO.Request req) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}
