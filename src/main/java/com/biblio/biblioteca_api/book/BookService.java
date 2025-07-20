package com.biblio.biblioteca_api.book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;

    @Transactional(readOnly = true)
    public Page<Book> listAll(Pageable pageable){
        return bookRepo.findAll(pageable);
    }
}
