package ates.homework.auth.controller;

import ates.homework.auth.entity.BookEntity;
import ates.homework.auth.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookEntity> getBooks() {
        return bookService.getBooks();
    }

    @PostMapping
    public ResponseEntity<BookEntity> createBook(@RequestBody BookEntity book) {
        var saved = bookService.createBook(book);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
