package ua.mytreo.java.soltestws.entity;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mytreo   27.01.2016.
 * @version 1.2
 */

@XmlRootElement(name = "catalog")
@XmlType(propOrder = {"books"})
@XmlSeeAlso(Book.class)
public class Catalog {

    private List<Book> books=new ArrayList<>();


    @XmlElement(name = "book")
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "books=" + books +
                '}';
    }
}
