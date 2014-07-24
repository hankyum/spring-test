package com.hank.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hank.domain.Book;
 
@Repository("bookRepository")
public interface BookRepository extends CrudRepository<Book, String> {
 
    public Iterable<Book> findBooksByAuthor(@Param("author") String author);
}
