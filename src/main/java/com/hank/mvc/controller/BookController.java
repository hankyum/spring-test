package com.hank.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hank.dao.BookRepository;
import com.hank.domain.Book;

@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

	@Autowired
	private BookRepository bookRepository;

	@RequestMapping
	public Iterable<Book> books() {
		return bookRepository.findAll(); 
	}

	@RequestMapping(value = "/{isbn}")
	public Book book(@PathVariable("isbn") String isbn) {
		return bookRepository.findOne(isbn);
	}

	@RequestMapping(value = "/{isbn}", method = RequestMethod.DELETE)
	public String deleteBook(@PathVariable("isbn") String isbn) {
		try {
			bookRepository.delete(isbn);
			return String.format("Book [%s] successfully deleted", isbn);
		} catch (Exception e) {
			return String.format("A problem occurred when deleting Book [%s]",
					e.getMessage());
		}
	}

	@RequestMapping("/author/{author}")
	public Iterable<Book> booksByAuthor(@PathVariable("author") String author) {
		return bookRepository.findBooksByAuthor(author);
	}

	public BookRepository getBookRepository() {
		return bookRepository;
	}

	public void setBookRepository(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

}
