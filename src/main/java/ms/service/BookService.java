package ms.service;

import ms.model.Book;

public interface BookService {
    Iterable<Book> getBooks();
}
