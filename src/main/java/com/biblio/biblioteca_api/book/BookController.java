package com.biblio.biblioteca_api.book;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService book;

    @GetMapping
    public ResponseEntity<Page<BookDTO>> list(Pageable pageable){
        Page<Book> page = book.listAll(pageable);
        Page<BookDTO> dtoPage = page.map(BookDTO::fromEntity);
        return ResponseEntity.ok(dtoPage);
    }
    @PostMapping
    public ResponseEntity<BookDTO>create(@RequestBody @Valid BookDTO bookDTO){
        Book bookEntity = bookDTO.toEntity();
        Book save = book.create(bookEntity);
        BookDTO response = BookDTO.fromEntity(save);
        return ResponseEntity
                .created(URI.create("/api/books/" + save.getId()))
                .body(response);
    }

}
