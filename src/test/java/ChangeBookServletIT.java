import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.mytreo.java.soltestws.entity.Book;
import ua.mytreo.java.soltestws.parser.Parser;
import ua.mytreo.java.soltestws.servlets.ChangeBookServlet;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author mytreo   28.01.2016.
 * @version 1.0
 */

//TODO тесты не должны зависить от маин.хмл
public class ChangeBookServletIT {
    private ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"appConfig.xml"});
    private List<Book> mainBookList = new ArrayList<>();
    private String testXmlIns = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<catalog>\n" +
            "    <book id=\"bk101\">\n" +
            "        <author>Gambardella, Matthew</author>\n" +
            "        <title>XML Developer's Guide</title>\n" +
            "        <genre>Computer</genre>\n" +
            "        <price>44.95</price>\n" +
            "        <publish_date>2000-12-17</publish_date>\n" +
            "        <description>An in-depth look at creating applications </description>\n" +
            "    </book>\n" +
            "</catalog>";
    private String testXmlUpd = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<catalog>\n" +
            "    <book id=\"bk101\">\n" +
            "        <author>Matthew</author>\n" +
            "        <title>XML</title>\n" +
            "        <genre>Computer</genre>\n" +
            "        <price>44.95</price>\n" +
            "        <publish_date>2000-10-01</publish_date>\n" +
            "        <description>An</description>\n" +
            "    </book>\n" +
            "</catalog>";
    private String testXmlDel = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<catalog>\n" +
            "    <book id=\"bk101\">\n" +
            "        <author></author>\n" +
            "        <title></title>\n" +
            "        <genre></genre>\n" +
            "        <price>44.95</price>\n" +
            "        <publish_date>2000-10-01</publish_date>\n" +
            "        <description>An</description>\n" +
            "    </book>\n" +
            "</catalog>";
    private Date date_;
    @Before
    public void setUp() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.DECEMBER, 17);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date_ = cal.getTime();

    }

    private HttpServletResponse getMockedResponse(StringWriter stringWriter) throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        final PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    private HttpServletRequest getMockedRequest(String url,String xml,boolean good) throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(xml)));
        if(good){
        when(request.getContentType()).thenReturn("text/xml");}
        else{
            when(request.getContentType()).thenReturn("text");
        }
        return request;
    }

    private ServletConfig getMockedConfig(){
        ServletConfig config = mock(ServletConfig.class);
        return config;
    }

    @Test
    public void testDoPostError() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest(ChangeBookServlet.PAGE_URL,"",false);
        ChangeBookServlet changeBookSrv =new ChangeBookServlet();
        changeBookSrv.doPost(request, response);
        assertEquals("400 BAD_REQUEST", stringWriter.toString().trim());
    }

   @Test
    public void testDoPostCreate() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest(ChangeBookServlet.PAGE_URL,testXmlIns,true);
        ChangeBookServlet changeBookSrv =new ChangeBookServlet();
        int i = changeBookSrv.getMainBookList().size();
        changeBookSrv.doPost(request, response);
        assertEquals(++i, changeBookSrv.getMainBookList().size());
    }

    @Test
    public void testDoPostRead() throws Exception {
        Book book1 = new Book("bk101", "Gambardella, Matthew", "XML Developer's Guide", "Computer", 44.95f,date_ , "An in-depth look at creating applications ");
        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest(ChangeBookServlet.PAGE_URL,"",true);
        ChangeBookServlet changeBookSrv =new ChangeBookServlet();
        changeBookSrv.getMainBookList().add(book1);
        changeBookSrv.doPost(request, response);
        changeBookSrv.getMainBookList().add(book1);
        assertEquals(testXmlIns, stringWriter.toString().trim());
    }
    @Test
    public void testDoPostUpdate() throws Exception {
        Book book1 = new Book("bk101", "Matthew", "XML", "Computer", 44.95f, new Date(), "An in-depth look at creating applications ");
        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest(ChangeBookServlet.PAGE_URL,testXmlUpd,true);
        ChangeBookServlet changeBookSrv =new ChangeBookServlet();
        changeBookSrv.getMainBookList().add(book1);
        int i = changeBookSrv.getMainBookList().size();
        assertTrue(i>0);
        changeBookSrv.doPost(request, response);
        assertEquals(i, changeBookSrv.getMainBookList().size());
        assertTrue((stringWriter.toString().trim()).contains("<description>An</description>"));
    }

    @Test
    public void testDoPostDelete() throws Exception {
        Book book1 = new Book("bk101", "Gambardella, Matthew", "XML Developer's Guide", "Computer", 44.95f, new Date(), "An in-depth look at creating applications \nwith XML.");
        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest(ChangeBookServlet.PAGE_URL,testXmlDel,true);
        ChangeBookServlet changeBookSrv =new ChangeBookServlet();
        changeBookSrv.getMainBookList().add(book1);
        int i = changeBookSrv.getMainBookList().size();
        assertTrue(i>0);
        changeBookSrv.doPost(request, response);
        assertEquals(--i, changeBookSrv.getMainBookList().size());
        assertTrue(!(stringWriter.toString().trim()).contains("bk101"));//id
    }
}