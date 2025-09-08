package com.biblio.biblioteca_api.book;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    private BookSpecs(){}
    public static Specification<Book>titleContains(String t){
        return(root, query, cb) ->  {
            if (t == null || t.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("title")),"%" + t.toLowerCase() + "%" );
        };
    }
    public static Specification<Book> isbnEquals(String v) {
        return (root, query, cb) -> {
            if (v == null || v.isBlank()) return cb.conjunction();
            return cb.equal(root.get("isbn"), v);
        };
    }

    public static Specification<Book> hasCategory(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return cb.conjunction();
            // evita duplicatas quando há N categorias
            query.distinct(true);
            var join = root.join("categories", JoinType.INNER);
            return cb.equal(cb.lower(join.get("name")), name.toLowerCase());
        };
    }

    public static Specification<Book> yearEquals(Integer y) {
        return (root, query, cb) -> (y == null) ? cb.conjunction() : cb.equal(root.get("yearPublication"), y);
    }

    public static Specification<Book> copiesAtLeast(Integer min) {
        return (root, query, cb) -> (min == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("copiesAvailable"), min);
    }
}


