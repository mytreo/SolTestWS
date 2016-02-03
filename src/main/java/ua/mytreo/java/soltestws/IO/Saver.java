package ua.mytreo.java.soltestws.IO;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import ua.mytreo.java.soltestws.entity.Book;
import ua.mytreo.java.soltestws.entity.Catalog;
import ua.mytreo.java.soltestws.parser.Parser;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

/**
 * @author mytreo 29.01.2016.
 * @version 1.0
 */
public class Saver implements Runnable {
    private final int SLEEPING_TIME;
    private AtomicInteger outerCounter;
    private List<Book> saveList;
    private String filepath;
    int innerCounter;
    private ApplicationContext ctx;

    public Saver(ApplicationContext ctx, AtomicInteger outerCounter, List<Book> saveList, String filepath, int sleepingTime){
        this.outerCounter=outerCounter;
        this.innerCounter=0;
        this.saveList=saveList;
        this.filepath=filepath;
        this.SLEEPING_TIME=sleepingTime;
        this.ctx=ctx;
    }
    @Override
    public void run() {
        while(true){
            if(outerCounter.intValue()>innerCounter){
                innerCounter=outerCounter.intValue();
                try {
                    saveToFile();
                } catch (Exception e) {
                    innerCounter--;
                }
                try {
                    sleep(SLEEPING_TIME);
                } catch (InterruptedException e) {
                    System.out.println("Interrupt sleeping saver ");
                }
            }

        }

    }

    private void saveToFile() throws Exception {
        Catalog cat = new Catalog();
        cat.setBooks(saveList);
        String toWriteString;
        try {
            Parser parser = (Parser) ctx.getBean("parserJAXB");//new ParserJaxbImpl();
            toWriteString=parser.marshall(cat);
        } catch ( JAXBException | NoSuchBeanDefinitionException e) {
           throw new Exception("Can't parse!");
        }

        //Writing File
        System.out.println("Write upd "+innerCounter);
        try(FileWriter writer = new FileWriter(filepath, false))
        {
            writer.write(toWriteString);
            writer.flush();
        }
        catch(IOException ex){
            throw new Exception("Can't write! "+ex.getMessage());
        }

    }
}
