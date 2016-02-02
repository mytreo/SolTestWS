package ua.mytreo.java.soltestws.servlets;

import org.xml.sax.SAXException;
import ua.mytreo.java.soltestws.entity.Book;
import ua.mytreo.java.soltestws.entity.Catalog;
import ua.mytreo.java.soltestws.parser.Parser;
import ua.mytreo.java.soltestws.parser.impl.ParserJaxbImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * Сервлет,отвечающий за работу со списком книг
 *
 * @author mytreo   27.01.2016.
 * @version 1.3
 */

@WebServlet(ChangeBookServlet.PAGE_URL)
public class ChangeBookServlet extends HttpServlet {
    public static final String PAGE_URL = "/changeBook";
    private List<Book> mainBookList;


    public ChangeBookServlet(List<Book> mainBookList) {
        this.mainBookList = mainBookList;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {


        response.setContentType("text/xml;charset=utf-8");

        if (!(request.getContentType().equals("text/xml") ||
                request.getContentType().equals("application/xml"))) {
            response.getWriter().println("400 BAD_REQUEST");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //read xml
        String value;
        BufferedReader b = new BufferedReader(request.getReader());
        StringBuilder workBuffer = new StringBuilder();
        while ((value = b.readLine()) != null) {
            workBuffer.append(value);
        }
        value = workBuffer.toString();
        // b.close();

        try {
            value = responseHelper(value);
        } catch (SAXException | ClassNotFoundException e) {
            response.getWriter().println("500 SC_INTERNAL_SERVER_ERROR");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (JAXBException e) {
            response.getWriter().println("400 BAD_REQUEST BAD_XML");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        response.getWriter().println(value);
        response.setContentType("text/xml;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private String responseHelper(String reqXml) throws SAXException, ClassNotFoundException, JAXBException {
        Parser parser = new ParserJaxbImpl();
        Catalog catMain = new Catalog();
        //READ
        if (reqXml.equals("")) {
            catMain.setBooks(mainBookList);
            return parser.marshall(catMain);
        }

        Catalog catReq = (Catalog) parser.unMarshall(reqXml, Class.forName("ua.mytreo.java.soltestws.entity.Catalog"));
        Book bookReq = catReq.getBooks().get(0);

        //INSERT UPDATE DELETE
        boolean haveSame = false;
        for (Book bookMainXml : mainBookList) {
            if (bookMainXml.getId().equals(bookReq.getId())) {
                haveSame = true;
                if (bookReq.isBookForDel()) {
                    mainBookList.remove(bookMainXml);
                    break;
                }
                bookMainXml.setAuthor(bookReq.getAuthor());
                bookMainXml.setTitle(bookReq.getTitle());
                bookMainXml.setGenre(bookReq.getGenre());
                bookMainXml.setDescription(bookReq.getDescription());
                bookMainXml.setPrice(bookReq.getPrice());
                bookMainXml.setPublishDate(bookReq.getPublishDate());
            }
        }
        if (!haveSame) {
            mainBookList.add(bookReq);
        }
        catMain.setBooks(mainBookList);
        return parser.marshall(catMain);
    }
}
