package com.biblio.biblioteca_api.book;

import com.biblio.biblioteca_api.category.Category;
import lombok.*;

import java.util.List;
import java.util.Objects;
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
    private UUID categoryId;
    private List<String> categories;
    private List<String> subjects;


    public static BookDTO fromEntity(Book book) {
        List<String> catNames = (book.getCategories() == null)? List.of():
                book.getCategories().stream()
                        .map(Category::getName)
                        .filter(Objects::nonNull)
                        .toList();

        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .yearPublication(book.getYearPublication())
                .copiesAvailable(book.getCopiesAvailable())
                .categories(catNames)
                .build();
    }

    public Book toEntity(){
        return Book.builder()
                .title(title)
                .isbn(isbn)
                .yearPublication(yearPublication)
                .copiesAvailable(copiesAvailable)
                .build();

    }
}
