package ms.service;

import ms.model.Book;
import ms.model.Image;
import ms.model.Writer;
import ms.service.repository.BookRepository;
import ms.service.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WriterRepository writerRepository;

    @Override
    public void generateData() {
        Writer writer1 = writerRepository.save(new Writer(1L, "Писатель 1", new Date(56, 1, 1)));
        Writer writer2 = writerRepository.save(new Writer(2L, "Писатель 2", new Date(55, 2, 2)));
        Writer writer3 = writerRepository.save(new Writer(3L, "Писатель 3", new Date(57, 3, 3)));

        writer1.setImage(new Image(1L));
        writer2.setImage(new Image(2L));
        writer3.setImage(new Image(3L));

        Book book1 = new Book(1L, "Книга 1");
        Book book2 = new Book(2L, "Книга 2");
        Book book3 = new Book(3L, "Книга 3");

        writer1.addBook(book1);
        writer1.addBook(book2);

        writer2.addBook(book1);
        writer2.addBook(book2);

        writer3.addBook(book3);
    }
}
