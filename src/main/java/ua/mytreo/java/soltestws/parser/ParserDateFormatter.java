package ua.mytreo.java.soltestws.parser;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс необходимый для того что бы при маршаллинге и анмаршаллинге корректно воспринималась дата
 * @author mytreo   27.01.2016.
 * @version 1.0
 */
public class ParserDateFormatter extends XmlAdapter<String, Date> {
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public Date unmarshal(String date) throws Exception {
        return formatter.parse(date);
    }

    public String marshal(Date date) throws Exception {
        return formatter.format(date);
    }
}
