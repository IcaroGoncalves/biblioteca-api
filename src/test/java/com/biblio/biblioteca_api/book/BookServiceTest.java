package com.biblio.biblioteca_api.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepo;

    @InjectMocks
    private BookService service;

    @Test
    @DisplayName("listAll(Pageable) deve retornar página vazia quando não há livros")
    void listAllReturnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(bookRepo.findAll(pageable)).thenReturn(emptyPage);

        Page<Book> result = service.listAll(pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty(), "Deve retornar lista vazia");
        assertEquals(0, result.getTotalElements(), "Total de elementos deve ser zero");
        verify(bookRepo, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("listAll(Pageable) deve retornar página com livros quando existirem registros")
    void listAllReturnsPageWithBooks() {
        Pageable pageable = PageRequest.of(1, 3);
        Book b1 = Book.builder()
                .id(java.util.UUID.randomUUID())
                .title("Livro A")
                .isbn("ISBN-A")
                .yearPublication(2001)
                .copiesAvailable(2)
                .build();
        Book b2 = Book.builder()
                .id(UUID.randomUUID())
                .title("Livro B")
                .isbn("ISBN-B")
                .yearPublication(2002)
                .copiesAvailable(5)
                .build();

        List<Book> list = List.of(b1, b2);
        Page<Book> page = new PageImpl<>(list, pageable, 10);
        when(bookRepo.findAll(pageable)).thenReturn(page);

        Page<Book> result = service.listAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size(), "Deve retornar dois livros na página");
        assertEquals(10, result.getTotalElements(), "Total de elementos deve vir de PageImpl");
        assertEquals(list, result.getContent(), "Conteúdo da página deve ser igual ao mock");
        verify(bookRepo, times(1)).findAll(pageable);
    }
}
