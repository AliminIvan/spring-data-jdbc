package com.ivanalimin.spring_data_jdbc.service;

import com.ivanalimin.spring_data_jdbc.model.Book;
import com.ivanalimin.spring_data_jdbc.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book("Title", "Author", 2024);
        book.setId(1L);
    }

    @Test
    void testFindAll() {
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.findAll();
        assertEquals(1, result.size());
        assertEquals(book, result.get(0));
    }

    @Test
    void testFindById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }

    @Test
    void testSaveNewBook() {
        Book newBook = new Book("New Title", "New Author", 2024);
        when(bookRepository.save(newBook)).thenReturn(newBook);

        Book result = bookService.save(newBook);
        assertEquals(newBook, result);
        verify(bookRepository, times(1)).save(newBook);
        verify(bookRepository, never()).update(newBook);
    }

    @Test
    void testUpdateExistingBook() {
        when(bookRepository.update(book)).thenReturn(book);

        Book result = bookService.save(book);
        assertEquals(book, result);
        verify(bookRepository, times(1)).update(book);
        verify(bookRepository, never()).save(book);
    }

    @Test
    void testSaveNullBook() {
        assertThrows(IllegalArgumentException.class, () -> bookService.save(null));
        verify(bookRepository, never()).save(any(Book.class));
        verify(bookRepository, never()).update(any(Book.class));
    }

    @Test
    void testDeleteById() {
        when(bookRepository.delete(1L)).thenReturn(true);

        boolean result = bookService.deleteById(1L);
        assertTrue(result);
    }
}

