package ua.mytreo.java.soltestws.servlets;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import ua.mytreo.java.soltestws.IO.Loader;
import ua.mytreo.java.soltestws.IO.impl.LoaderImpl;
import ua.mytreo.java.soltestws.IO.Saver;
import ua.mytreo.java.soltestws.entity.Book;
import ua.mytreo.java.soltestws.entity.Catalog;
import ua.mytreo.java.soltestws.parser.Parser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Сервлет,отвечающий за работу со списком книг
 *
 * @author mytreo   27.01.2016.
 * @version 1.4
 */

@WebServlet(name="changeBook",urlPatterns={ChangeBookServlet.PAGE_URL})
public class ChangeBookServlet extends HttpServlet {
    public ApplicationContext ctx;
    public static final String PAGE_URL = "/changeBook";
    public static final String MAIN_XML_PATH="D:/main.xml";
    public static final int BETWEEN_SAVES_PAUSE=10000;
    private List<Book> mainBookList;
    private AtomicInteger countInsUpdDel;
    private Thread saverSrv;
    public List<Book> getMainBookList(){
        return mainBookList;
    }

    @Override
    public void init() throws ServletException {
        this.mainBookList = new CopyOnWriteArrayList<>();//ArrayList<>();
        this.countInsUpdDel= new AtomicInteger(0);
        this.ctx = (ApplicationContext) this.getServletContext().getAttribute("appContext");
        Loader loader= (Loader) ctx.getBean("loader");  //new LoaderImpl();
        mainBookList.addAll(loader.getBooks(MAIN_XML_PATH));

        saverSrv = new Thread(new Saver(ctx,countInsUpdDel,mainBookList,MAIN_XML_PATH,BETWEEN_SAVES_PAUSE));
        saverSrv.start();
    }

    @Override
    public void destroy() {
        saverSrv.interrupt();
        super.destroy();
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
        } catch ( ClassNotFoundException | NoSuchBeanDefinitionException e) {
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

    private String responseHelper(String reqXml) throws  ClassNotFoundException, JAXBException,NoSuchBeanDefinitionException {
        Parser parser = (Parser) ctx.getBean("parserJAXB");//new ParserJaxbImpl();
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
        countInsUpdDel.incrementAndGet();
        return parser.marshall(catMain);
    }
}
