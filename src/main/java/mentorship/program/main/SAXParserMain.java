package mentorship.program.main;


import mentorship.program.model.Book;
import mentorship.program.parser.SAXHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class SAXParserMain {

    private static final String TEST_FILE_PATH = "XML_TASK.xml";

    public static void main (String argv []) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream xmlStream = classloader.getResourceAsStream(TEST_FILE_PATH);

            SAXParser saxParser = factory.newSAXParser();
            SAXHandler handler   = new SAXHandler();
            saxParser.parse(xmlStream, handler);

            for(Map.Entry<String,List<Book>> entry : handler.books.entrySet()){
                System.out.println("catalogue title : " + entry.getKey());
                for(Book curBook : entry.getValue()){
                    System.out.println(curBook);
                }
            }
        } catch (Throwable err) {
            err.printStackTrace ();
        }
    }

}
