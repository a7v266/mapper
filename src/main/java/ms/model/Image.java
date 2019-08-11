package ms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "image")
public class Image extends BaseEntity {

    @Column(name = "content")
    private byte[] content;

    public Image() {
    }

    public Image(Long id) {
        super(id);
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
