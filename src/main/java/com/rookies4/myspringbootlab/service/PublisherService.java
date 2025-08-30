package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.controller.dto.PublisherDTO;
import com.rookies4.myspringbootlab.entity.Publisher;
import com.rookies4.myspringbootlab.exception.BusinessException;
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
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    public List<PublisherDTO.SimpleResponse> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(p -> PublisherDTO.SimpleResponse.fromEntityWithCount(
                        p, bookRepository.countByPublisherId(p.getId())))
                .toList();
    }

    public PublisherDTO.Response getPublisherById(Long id) {
        Publisher p = publisherRepository.findById(id)
                .orElseThrow(() -> new BusinessException("출판사를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND));
        long count = bookRepository.countByPublisherId(p.getId());
        List<BookDTO.SimpleResponse> books = bookRepository.findByPublisherId(p.getId()).stream()
                .map(BookDTO.SimpleResponse::fromEntity)
                .toList();
        return PublisherDTO.Response.fromEntity(p, count, books);
    }

    public PublisherDTO.Response getPublisherByName(String name) {
        Publisher p = publisherRepository.findByName(name)
                .orElseThrow(() -> new BusinessException("출판사를 찾을 수 없습니다. name=" + name, HttpStatus.NOT_FOUND));
        long count = bookRepository.countByPublisherId(p.getId());
        List<BookDTO.SimpleResponse> books = bookRepository.findByPublisherId(p.getId()).stream()
                .map(BookDTO.SimpleResponse::fromEntity)
                .toList();
        return PublisherDTO.Response.fromEntity(p, count, books);
    }

    @Transactional
    public PublisherDTO.Response createPublisher(PublisherDTO.Request req) {
        if (publisherRepository.existsByName(req.getName())) {
            throw new BusinessException("이미 존재하는 출판사 이름입니다: " + req.getName(), HttpStatus.CONFLICT);
        }
        Publisher p = Publisher.builder()
                .name(req.getName())
                .establishedDate(req.getEstablishedDate())
                .address(req.getAddress())
                .build();
        Publisher saved = publisherRepository.save(p);
        return PublisherDTO.Response.fromEntity(saved, 0L, List.of());
    }

    @Transactional
    public PublisherDTO.Response updatePublisher(Long id, PublisherDTO.Request req) {
        Publisher p = publisherRepository.findById(id)
                .orElseThrow(() -> new BusinessException("출판사를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND));
        if (!p.getName().equals(req.getName()) && publisherRepository.existsByName(req.getName())) {
            throw new BusinessException("이미 존재하는 출판사 이름입니다: " + req.getName(), HttpStatus.CONFLICT);
        }
        p.setName(req.getName());
        p.setEstablishedDate(req.getEstablishedDate());
        p.setAddress(req.getAddress());
        long count = bookRepository.countByPublisherId(p.getId());
        List<BookDTO.SimpleResponse> books = bookRepository.findByPublisherId(p.getId()).stream()
                .map(BookDTO.SimpleResponse::fromEntity)
                .toList();
        return PublisherDTO.Response.fromEntity(p, count, books);
    }

    @Transactional
    public void deletePublisher(Long id) {
        long count = bookRepository.countByPublisherId(id);
        if (count > 0) {
            //삭제 거부는 409
            throw new BusinessException("해당 출판사에 등록된 도서가 있어 삭제할 수 없습니다.", HttpStatus.CONFLICT);
        }
        Publisher p = publisherRepository.findById(id)
                .orElseThrow(() -> new BusinessException("출판사를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND));
        publisherRepository.delete(p);
    }
}
