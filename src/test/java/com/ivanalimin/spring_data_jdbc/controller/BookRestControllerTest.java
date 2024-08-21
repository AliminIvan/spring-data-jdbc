package com.ivanalimin.spring_data_jdbc.controller;

import com.ivanalimin.spring_data_jdbc.model.Book;
import com.ivanalimin.spring_data_jdbc.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class BookRestControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookRestController bookController;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book("Title", "Author", 2024);
        book.setId(1L);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Collections.singletonList(book);
        when(bookService.findAll()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals(book, response.getBody().get(0));
    }

    @Test
    void testGetBookById() {
        when(bookService.findById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookController.getBookById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void testCreateBook() {
        Book newBook = new Book("New Title", "New Author", 2024);
        when(bookService.save(newBook)).thenReturn(newBook);

        ResponseEntity<Book> response = bookController.createBook(newBook);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newBook, response.getBody());
        verify(bookService, times(1)).save(newBook);
    }

    @Test
    void testUpdateBook_Found() {
        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        when(bookService.save(book)).thenReturn(book);

        ResponseEntity<Book> response = bookController.updateBook(1L, book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
        verify(bookService, times(1)).findById(1L);
        verify(bookService, times(1)).save(book);
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.updateBook(1L, book);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService, times(1)).findById(1L);
        verify(bookService, never()).save(any(Book.class));
    }

    @Test
    void testDeleteBook() {
        when(bookService.deleteById(1L)).thenReturn(true);

        ResponseEntity<Void> response = bookController.deleteBook(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}

