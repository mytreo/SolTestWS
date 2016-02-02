package ua.mytreo.java.soltestws.IO;

import org.xml.sax.SAXException;
import ua.mytreo.java.soltestws.entity.Book;
import ua.mytreo.java.soltestws.entity.Catalog;
import ua.mytreo.java.soltestws.parser.Parser;
import ua.mytreo.java.soltestws.parser.impl.ParserJaxbImpl;

import javax.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author mytreo   29.01.2016.
 * @version 1.0
 */
public class Loader {

    public List<Book> getBooksFromFile(String mainXmlPath){

        StringBuilder sb = new StringBuilder();
        Parser parser;
        Catalog cat;
        String mainXml;
        try (FileReader reader = new FileReader(mainXmlPath)) {
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException ex) {
            return new ArrayList<>();
        }
         mainXml = sb.toString();
        try {
            parser = new ParserJaxbImpl();
            cat = (Catalog) parser.unMarshall(mainXml, Class.forName("ua.mytreo.java.soltestws.entity.Catalog"));
        } catch (SAXException | ClassNotFoundException | JAXBException e) {
            return new ArrayList<>();
        }
        return cat.getBooks();
    }
}
