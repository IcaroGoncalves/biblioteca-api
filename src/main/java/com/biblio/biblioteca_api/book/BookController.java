package com.biblio.biblioteca_api.book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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
}
