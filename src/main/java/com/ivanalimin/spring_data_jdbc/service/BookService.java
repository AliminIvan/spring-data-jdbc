package com.ivanalimin.spring_data_jdbc.service;

import com.ivanalimin.spring_data_jdbc.model.Book;
import com.ivanalimin.spring_data_jdbc.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (book.getId() == null) {
            return bookRepository.save(book);
        }
        return bookRepository.update(book);
    }

    public boolean deleteById(Long id) {
        return bookRepository.delete(id);
    }
}
