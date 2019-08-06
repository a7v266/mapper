package ms.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "writer")
public class Writer extends BaseEntity {


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_writer",
            joinColumns = @JoinColumn(name = "writer_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books;

    @Column(name = "writer_name")
    private String writerName;

    public Writer() {
    }

    public Writer(Long id, String writerName) {
        super(id);
        this.writerName = writerName;
    }

    public void addBook(Book book) {
        if (books == null) {
            books = new HashSet<>();
        }
        books.add(book);
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }
}
