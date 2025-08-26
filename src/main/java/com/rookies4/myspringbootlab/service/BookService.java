package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.entity.BookDetail;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookDetailRepository;
import com.rookies4.myspringbootlab.repository.BookRepository;
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

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. isbn=" + isbn, HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.CreateRequest req) {
        //ISBN 중복 체크
        if (bookRepository.existsByIsbn(req.getIsbn())) {
            throw new BusinessException("이미 사용중인 ISBN입니다: " + req.getIsbn(), HttpStatus.BAD_REQUEST);
        }

        Book saved = bookRepository.save(req.toEntity());
        return BookDTO.Response.fromEntity(saved);
    }

    //PUT 전체 수정
    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.UpdateRequest req) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("수정할 도서를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND));

        //ISBN 변경 시 중복 체크
        if (!book.getIsbn().equals(req.getIsbn()) && bookRepository.existsByIsbn(req.getIsbn())) {
            throw new BusinessException("이미 사용중인 ISBN입니다: " + req.getIsbn(), HttpStatus.BAD_REQUEST);
        }

        //전체 덮어쓰기
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setIsbn(req.getIsbn());
        book.setPrice(req.getPrice());
        book.setPublishDate(req.getPublishDate());

        //상세 전체 교체
        if (req.getDetailRequest() != null) {
            BookDetail current = book.getBookDetail();
            if (current == null) {
                //기존이 없으면 새로 만들어 연결
                current = new BookDetail();
                book.setBookDetail(current); //양방향 세팅
            }
            var d = req.getDetailRequest();
            current.setDescription(d.getDescription());
            current.setLanguage(d.getLanguage());
            current.setPageCount(d.getPageCount());
            current.setPublisher(d.getPublisher());
            current.setCoverImageUrl(d.getCoverImageUrl());
            current.setEdition(d.getEdition());
        } else {
            //요청에 detail이 없으면 상세 제거
            if (book.getBookDetail() != null) {
                book.setBookDetail(null);
            }
        }

        //영속상태이므로 save 없어도 flush 시점에 반영되지만, 일관성을 위해 save 호출
        Book saved = bookRepository.save(book);
        return BookDTO.Response.fromEntity(saved);
    }

    //PATCH 부분 수정
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
                throw new BusinessException("이미 사용중인 ISBN입니다: " + req.getIsbn(), HttpStatus.BAD_REQUEST);
            }
            book.setIsbn(req.getIsbn());
        }

        //Detail 일부 수정
        if (req.getDetailRequest() != null) {
            BookDetail detail = book.getBookDetail();
            if (detail == null) {
                detail = new BookDetail();
                book.setBookDetail(detail);
            }
            var d = req.getDetailRequest();
            if (d.getDescription() != null) detail.setDescription(d.getDescription());
            if (d.getLanguage() != null) detail.setLanguage(d.getLanguage());
            if (d.getPageCount() != null) detail.setPageCount(d.getPageCount());
            if (d.getPublisher() != null) detail.setPublisher(d.getPublisher());
            if (d.getCoverImageUrl() != null) detail.setCoverImageUrl(d.getCoverImageUrl());
            if (d.getEdition() != null) detail.setEdition(d.getEdition());
        }

        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }

    //BookDetail만 PATCH
    @Transactional
    public BookDTO.Response patchBookDetail(Long id, BookDTO.BookDetailPatchRequest req) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("대상 도서를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND));

        BookDetail detail = book.getBookDetail();
        if (detail == null) {
            detail = new BookDetail();
            book.setBookDetail(detail);
        }

        if (req.getDescription() != null) detail.setDescription(req.getDescription());
        if (req.getLanguage() != null) detail.setLanguage(req.getLanguage());
        if (req.getPageCount() != null) detail.setPageCount(req.getPageCount());
        if (req.getPublisher() != null) detail.setPublisher(req.getPublisher());
        if (req.getCoverImageUrl() != null) detail.setCoverImageUrl(req.getCoverImageUrl());
        if (req.getEdition() != null) detail.setEdition(req.getEdition());

        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("삭제할 도서를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }
}
