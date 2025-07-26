package com.biblio.biblioteca_api.book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;

    @Transactional
    public Book create(Book book){
        bookRepo.findByIsbn(book.getIsbn())
                .ifPresent(b ->{throw new IllegalArgumentException("The inserted book is already cataloged." + book.getIsbn()); });
        return  bookRepo.save(book);
    }

    @Transactional(readOnly = true)
    public Page<Book> listAll(Pageable pageable){
        return bookRepo.findAll(pageable);
    }

    @Transactional
    public Book update(UUID id, Book updatedBook){
        Book existing = bookRepo.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Book not found:" + id));
        bookRepo.findByIsbn(updatedBook.getIsbn())
                .filter(b -> !b.getId().equals(id))
                .ifPresent(b -> {
                    throw new IllegalArgumentException("ISBN already in use: " + updatedBook.getIsbn());
                });
        existing.setTitle(updatedBook.getTitle());
        existing.setIsbn(updatedBook.getIsbn());
        existing.setYearPublication(updatedBook.getYearPublication());
        existing.setCopiesAvailable(updatedBook.getCopiesAvailable());
        return bookRepo.save(existing);
    }

    @Transactional
    public void delete(UUID id){
        Book existing = bookRepo.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Book not found:"+ id));
        bookRepo.delete(existing);
    }

}
