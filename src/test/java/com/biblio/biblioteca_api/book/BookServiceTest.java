package com.biblio.biblioteca_api.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepo;

    @InjectMocks
    private BookService service;

    private Book sampleBook(UUID id, String isbn) {
        return Book.builder()
                .id(id)
                .title("Title")
                .isbn(isbn)
                .yearPublication(2020)
                .copiesAvailable(5)
                .build();
    }

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

    @Test
    @DisplayName("create() salva novo livro quando ISBN é único")
    void createSucceedsWhenIsbnUnique() {
        Book toCreate = sampleBook(null, "UNIQUE-ISBN");
        when(bookRepo.findByIsbn("UNIQUE-ISBN")).thenReturn(Optional.empty());
        when(bookRepo.save(toCreate)).thenReturn(toCreate);

        Book result = service.create(toCreate);

        assertSame(toCreate, result);
        verify(bookRepo).findByIsbn("UNIQUE-ISBN");
        verify(bookRepo).save(toCreate);
    }

    @Test
    @DisplayName("create() lança erro quando ISBN já cadastrado")
    void createThrowsOnDuplicateIsbn() {
        Book existing = sampleBook(UUID.randomUUID(), "DUP-ISBN");
        when(bookRepo.findByIsbn("DUP-ISBN")).thenReturn(Optional.of(existing));
        Book toCreate = sampleBook(null, "DUP-ISBN");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.create(toCreate));

        assertTrue(ex.getMessage().contains("already cataloged"));
        verify(bookRepo).findByIsbn("DUP-ISBN");
        verify(bookRepo, never()).save(any());
    }

    @Test
    @DisplayName("update() altera livro existente com sucesso")
    void updateSucceedsWhenValid() {
        UUID id = UUID.randomUUID();
        Book existing = sampleBook(id, "OLD-ISBN");
        Book updated = sampleBook(null, "NEW-ISBN");

        when(bookRepo.findById(id)).thenReturn(Optional.of(existing));
        when(bookRepo.findByIsbn("NEW-ISBN")).thenReturn(Optional.empty());
        when(bookRepo.save(existing)).thenReturn(existing);

        Book result = service.update(id, updated);

        assertEquals("NEW-ISBN", existing.getIsbn());
        assertSame(existing, result);
        verify(bookRepo).findById(id);
        verify(bookRepo).findByIsbn("NEW-ISBN");
        verify(bookRepo).save(existing);
    }

    @Test
    @DisplayName("update() lança erro quando livro não existe")
    void updateThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(bookRepo.findById(id)).thenReturn(Optional.empty());
        Book updated = sampleBook(null, "ANY");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.update(id, updated));

        assertTrue(ex.getMessage().contains("Book not found"));
        verify(bookRepo).findById(id);
        verify(bookRepo, never()).save(any());
    }

    @Test
    @DisplayName("update() lança erro quando ISBN entra em conflito com outro registro")
    void updateThrowsOnIsbnConflict() {
        UUID id = UUID.randomUUID();
        Book existing = sampleBook(id, "OLD-ISBN");
        Book conflict = sampleBook(UUID.randomUUID(), "CONFLICT-ISBN");
        Book updated = sampleBook(null, "CONFLICT-ISBN");

        when(bookRepo.findById(id)).thenReturn(Optional.of(existing));
        when(bookRepo.findByIsbn("CONFLICT-ISBN")).thenReturn(Optional.of(conflict));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.update(id, updated));

        assertTrue(ex.getMessage().contains("already in use"));
        verify(bookRepo).findById(id);
        verify(bookRepo).findByIsbn("CONFLICT-ISBN");
        verify(bookRepo, never()).save(any());
    }

    @Test
    @DisplayName("delete() remove livro existente com sucesso")
    void deleteSucceedsWhenFound() {
        UUID id = UUID.randomUUID();
        Book existing = sampleBook(id, "ISBN");
        when(bookRepo.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(bookRepo).delete(existing);

        assertDoesNotThrow(() -> service.delete(id));
        verify(bookRepo).findById(id);
        verify(bookRepo).delete(existing);
    }

    @Test
    @DisplayName("delete() lança erro quando livro não existe")
    void deleteThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(bookRepo.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.delete(id));

        assertTrue(ex.getMessage().contains("Book not found"));
        verify(bookRepo).findById(id);
        verify(bookRepo, never()).delete(any());
    }
}
