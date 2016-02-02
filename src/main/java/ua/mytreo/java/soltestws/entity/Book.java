package ua.mytreo.java.soltestws.entity;

import ua.mytreo.java.soltestws.parser.ParserDateFormatter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mytreo   27.01.2016.
 * @version 1.2
 */
@XmlType(propOrder = {"id","author","title","genre","price","publishDate","description"})
public class Book {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String id;
    private String author;
    private String title;
    private String genre;
    private float price;
    private Date publishDate;
    private String description;

    public Book(){
    }

    public Book(String id, String author, String title, String genre, float price, Date publishDate, String description){
        this.id = id;
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.publishDate = publishDate;
        this.description = description;
    }

    public boolean isBookForDel(){
        return  (this.getTitle().isEmpty() && this.getGenre().isEmpty() && this.getAuthor().isEmpty());
    }

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(name = "genre")
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @XmlElement(name = "price")
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @XmlElement(name = "publish_date")
    @XmlJavaTypeAdapter(ParserDateFormatter.class)
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate){
        this.publishDate = publishDate;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                ", publishDate='" + publishDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (Float.compare(book.getPrice(), getPrice()) != 0) return false;
        if (format != null ? !format.equals(book.format) : book.format != null) return false;
        if (!getId().equals(book.getId())) return false;
        if (getAuthor() != null ? !getAuthor().equals(book.getAuthor()) : book.getAuthor() != null) return false;
        if (getTitle() != null ? !getTitle().equals(book.getTitle()) : book.getTitle() != null) return false;
        if (getGenre() != null ? !getGenre().equals(book.getGenre()) : book.getGenre() != null) return false;
        if (getPublishDate() != null ? !getPublishDate().equals(book.getPublishDate()) : book.getPublishDate() != null)
            return false;
        return getDescription() != null ? getDescription().equals(book.getDescription()) : book.getDescription() == null;

    }

    @Override
    public int hashCode() {
        int result = format != null ? format.hashCode() : 0;
        result = 31 * result + getId().hashCode();
        result = 31 * result + (getAuthor() != null ? getAuthor().hashCode() : 0);
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getGenre() != null ? getGenre().hashCode() : 0);
        result = 31 * result + (getPrice() != +0.0f ? Float.floatToIntBits(getPrice()) : 0);
        result = 31 * result + (getPublishDate() != null ? getPublishDate().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
}
