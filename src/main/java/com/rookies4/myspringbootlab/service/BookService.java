package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.entity.BookDetail;
import com.rookies4.myspringbootlab.entity.Publisher;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookDetailRepository;
import com.rookies4.myspringbootlab.repository.BookRepository;
import com.rookies4.myspringbootlab.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;
    private final PublisherRepository publisherRepository;

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntityWithPublisher)
                .toList();
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithAllDetails(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntityWithPublisher(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. isbn=" + isbn, HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntityWithPublisher(book);
    }

    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(BookDTO.Response::fromEntityWithPublisher)
                .toList();
    }

    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(BookDTO.Response::fromEntityWithPublisher)
                .toList();
    }

    //출판사별 도서
    public List<BookDTO.Response> getBooksByPublisherId(Long publisherId) {
        return bookRepository.findByPublisherId(publisherId).stream()
                .map(BookDTO.Response::fromEntityWithPublisher)
                .toList();
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.CreateRequest req) {
        if (bookRepository.existsByIsbn(req.getIsbn())) {
            //중복은 409
            throw new BusinessException("이미 사용중인 ISBN입니다: " + req.getIsbn(), HttpStatus.CONFLICT);
        }

        Publisher publisher = publisherRepository.findById(req.getPublisherId())
                .orElseThrow(() -> new BusinessException("출판사를 찾을 수 없습니다. id=" + req.getPublisherId(), HttpStatus.NOT_FOUND));

        Book toSave = req.toEntity();
        toSave.setPublisher(publisher);

        Book saved = bookRepository.save(toSave);
        return BookDTO.Response.fromEntityWithPublisher(saved);
    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.UpdateRequest req) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("수정할 도서를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND));

        if (!book.getIsbn().equals(req.getIsbn()) && bookRepository.existsByIsbn(req.getIsbn())) {
            throw new BusinessException("이미 사용중인 ISBN입니다: " + req.getIsbn(), HttpStatus.CONFLICT);
        }

        Publisher publisher = publisherRepository.findById(req.getPublisherId())
                .orElseThrow(() -> new BusinessException("출판사를 찾을 수 없습니다. id=" + req.getPublisherId(), HttpStatus.NOT_FOUND));

        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setIsbn(req.getIsbn());
        book.setPrice(req.getPrice());
        book.setPublishDate(req.getPublishDate());
        book.setPublisher(publisher);

        if (req.getDetailRequest() != null) {
            BookDetail current = book.getBookDetail();
            if (current == null) { current = new BookDetail(); book.setBookDetail(current); }
            var d = req.getDetailRequest();
            current.setDescription(d.getDescription());
            current.setLanguage(d.getLanguage());
            current.setPageCount(d.getPageCount());
            current.setPublisher(d.getPublisher());
            current.setCoverImageUrl(d.getCoverImageUrl());
            current.setEdition(d.getEdition());
        } else if (book.getBookDetail() != null) {
            book.setBookDetail(null);
        }

        Book saved = bookRepository.save(book);
        return BookDTO.Response.fromEntityWithPublisher(saved);
    }

    @Transactional
    public BookDTO.Response patchBook(Long id, BookDTO.PatchRequest req) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("수정할 도서를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND));

        if (req.getTitle() != null) book.setTitle(req.getTitle());
        if (req.getAuthor() != null) book.setAuthor(req.getAuthor());
        if (req.getPrice() != null) book.setPrice(req.getPrice());
        if (req.getPublishDate() != null) book.setPublishDate(req.getPublishDate());

        if (req.getIsbn() != null) {
            if (!book.getIsbn().equals(req.getIsbn()) && bookRepository.existsByIsbn(req.getIsbn())) {
                throw new BusinessException("이미 사용중인 ISBN입니다: " + req.getIsbn(), HttpStatus.CONFLICT);
            }
            book.setIsbn(req.getIsbn());
        }

        if (req.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(req.getPublisherId())
                    .orElseThrow(() -> new BusinessException("출판사를 찾을 수 없습니다. id=" + req.getPublisherId(), HttpStatus.NOT_FOUND));
            book.setPublisher(publisher);
        }

        if (req.getDetailRequest() != null) {
            BookDetail detail = book.getBookDetail();
            if (detail == null) { detail = new BookDetail(); book.setBookDetail(detail); }
            var d = req.getDetailRequest();
            if (d.getDescription() != null) detail.setDescription(d.getDescription());
            if (d.getLanguage() != null) detail.setLanguage(d.getLanguage());
            if (d.getPageCount() != null) detail.setPageCount(d.getPageCount());
            if (d.getPublisher() != null) detail.setPublisher(d.getPublisher());
            if (d.getCoverImageUrl() != null) detail.setCoverImageUrl(d.getCoverImageUrl());
            if (d.getEdition() != null) detail.setEdition(d.getEdition());
        }

        Book saved = bookRepository.save(book);
        return BookDTO.Response.fromEntityWithPublisher(saved);
    }

    @Transactional
    public BookDTO.Response patchBookDetail(Long id, BookDTO.BookDetailPatchRequest req) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("대상 도서를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND));

        BookDetail detail = book.getBookDetail();
        if (detail == null) { detail = new BookDetail(); book.setBookDetail(detail); }

        if (req.getDescription() != null) detail.setDescription(req.getDescription());
        if (req.getLanguage() != null) detail.setLanguage(req.getLanguage());
        if (req.getPageCount() != null) detail.setPageCount(req.getPageCount());
        if (req.getPublisher() != null) detail.setPublisher(req.getPublisher());
        if (req.getCoverImageUrl() != null) detail.setCoverImageUrl(req.getCoverImageUrl());
        if (req.getEdition() != null) detail.setEdition(req.getEdition());

        Book saved = bookRepository.save(book);
        return BookDTO.Response.fromEntityWithPublisher(saved);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("삭제할 도서를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }
}
