package mentorship.program.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.Date;

@Data
@XmlRootElement(name = "book")
@XmlAccessorType (XmlAccessType.FIELD)
public class Book {
    @XmlAttribute
    private String id;
    @XmlElement
    private String author;
    @XmlElement
    private String title;
    @XmlElement
    private String genre;
    @XmlElement
    private double price;
    @XmlElement(name = "publish_date")
    private Date publishDate;
    @XmlElement
    private String description;

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                ", publishDate=" + publishDate +
                ", description='" + description + '\'' +
                '}';
    }
}
