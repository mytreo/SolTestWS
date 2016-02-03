package ua.mytreo.java.soltestws.IO;

import ua.mytreo.java.soltestws.entity.Book;

import java.util.List;

/**
 * @author mytreo   04.02.2016.
 * @version 1.0
 */
public interface Loader {
    List<Book> getBooks(String mainXmlPath);
}
