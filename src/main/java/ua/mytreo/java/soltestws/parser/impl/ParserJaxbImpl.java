package ua.mytreo.java.soltestws.parser.impl;


import org.xml.sax.SAXException;
import ua.mytreo.java.soltestws.parser.Parser;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Реализованный JAXB parser для маршкаллинга-анмаршаллинга Catalog+Book
 * @author mytreo   27.01.2016.
 * @version 1.1
 */

public class ParserJaxbImpl implements Parser {
    private Schema schema = null;

    public ParserJaxbImpl() throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schema = schemaFactory.newSchema(ParserJaxbImpl.class.getResource("/reqSchema.xsd"));
    }

    @Override
    public Object unMarshall(String inputXml, Class c) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(schema);
        unmarshaller.setEventHandler(event -> false);

        //unMarshall object from xml string
        InputStream reqXmlStream = new ByteArrayInputStream(inputXml.getBytes(StandardCharsets.UTF_8));

        return unmarshaller.unmarshal(reqXmlStream);
    }


    @Override
    public String marshall(Object o) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(o.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setSchema(schema);
        marshaller.setEventHandler(event -> false);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        //write XML to an array of bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(o, baos);

        return baos.toString();
    }
}
