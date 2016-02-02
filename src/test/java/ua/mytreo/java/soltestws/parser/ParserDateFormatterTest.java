package ua.mytreo.java.soltestws.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author mytreo   28.01.2016.
 * @version 1.0
 */
public class ParserDateFormatterTest {
    private ParserDateFormatter parserDateFormatter;


    @Before
    public void setUp() throws Exception {
        parserDateFormatter=new ParserDateFormatter();
    }


    @Test
    public void testDateUnmarshal() throws Exception {
        Date date=parserDateFormatter.unmarshal("2000-12-17");

        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.DECEMBER, 17);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date dateCorrect = cal.getTime();
        assertEquals("wrong date format",dateCorrect,date);
    }

    @Test
    public void testDateMarshal() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.DECEMBER, 17);
        Date date = cal.getTime();
        assertEquals("wrong date format","2000-12-17",parserDateFormatter.marshal(date));
    }
}