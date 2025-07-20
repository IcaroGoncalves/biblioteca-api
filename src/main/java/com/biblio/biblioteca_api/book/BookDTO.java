package com.biblio.biblioteca_api.book;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private UUID id;
    private String title;
    private String isbn;
    private Integer yearPublication;
    private Integer copiesAvailable;

    public static BookDTO fromEntity(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .yearPublication(book.getYearPublication())
                .copiesAvailable(book.getCopiesAvailable())
                .build();
    }
}
