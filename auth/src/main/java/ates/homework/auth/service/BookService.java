package ates.homework.auth.service;

import ates.homework.auth.entity.BookEntity;
import ates.homework.auth.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> getBooks() {
        return bookRepository.findAll();
    }

    public BookEntity createBook(BookEntity book) {
        return bookRepository.save(book);
    }
}
