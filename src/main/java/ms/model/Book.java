package ms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book extends BaseEntity {

    @ManyToMany(mappedBy = "books", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Writer> writers;

    @Basic
    @Column(name = "book_name")
    private String bookName;

    public Book() {
    }

    public Book(Long id, String bookName) {
        super(id);
        this.bookName = bookName;
    }

    @JsonIgnore
    public Set<Writer> getWriters() {
        return writers;
    }

    public void setWriters(Set<Writer> writers) {
        this.writers = writers;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
