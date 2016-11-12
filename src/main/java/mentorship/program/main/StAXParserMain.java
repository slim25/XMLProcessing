package mentorship.program.main;


import mentorship.program.model.Book;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StAXParserMain {

    private static final String TEST_FILE_PATH = "XML_TASK.xml";
    private static Map<String, List<Book>> categoryAndBooks  = new HashMap<String, List<Book>>();
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");


    public static void main(String[] args) throws XMLStreamException, ParseException {
        populateModuleByStAX();
        printResult();
    }

    private static void printResult(){

        for(Map.Entry<String,List<Book>> entry : categoryAndBooks.entrySet()){
            System.out.println("catalogue title : " + entry.getKey());
            for(Book curBook : entry.getValue()){
                System.out.println(curBook);
            }
        }
    }

    private static void populateModuleByStAX() throws XMLStreamException, ParseException {
        boolean bCategoryTitle = false;
        boolean bBook = false;
        boolean bAuthor = false;
        boolean bTitle = false;
        boolean bGenre = false;
        boolean bPrice = false;
        boolean bPublishDate = false;
        boolean bDescription = false;
        Book currentBook = null;
        String currentCategoryTitle = null;

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream xmlFileStream = classloader.getResourceAsStream(TEST_FILE_PATH);

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(xmlFileStream);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();

                    if (qName.equalsIgnoreCase("title") && categoryAndBooks.get(qName) == null) {
                        System.out.println("Start Category title : " + qName);
                        currentCategoryTitle = qName;
                        categoryAndBooks.put(qName, new ArrayList<Book>());
                    } else if (qName.equalsIgnoreCase("book")) {
                        System.out.println("Create new book");
                        currentBook = new Book();
                    } else if (qName.equalsIgnoreCase("author")) {
                        bAuthor = true;
                    } else if (qName.equalsIgnoreCase("title")) {
                        bTitle = true;
                    } else if (qName.equalsIgnoreCase("genre")) {
                        bGenre = true;
                    } else if (qName.equalsIgnoreCase("price")) {
                        bPrice = true;
                    } else if (qName.equalsIgnoreCase("publish_date")) {
                        bPublishDate = true;
                    } else if (qName.equalsIgnoreCase("description")) {
                        bDescription = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    Characters characters = event.asCharacters();
                    String currentValue = characters.getData();
                    if (bAuthor) {
                        System.out.println("set book author : " + currentValue);
                        currentBook.setAuthor(currentValue);
                        bAuthor = false;
                    }
                    if (bTitle) {
                        System.out.println("set book title : " + currentValue);
                        currentBook.setTitle(currentValue);
                        bTitle = false;
                    }
                    if (bGenre) {
                        System.out.println("set book genre : " + currentValue);
                        currentBook.setGenre(currentValue);
                        bGenre = false;
                    }
                    if (bPrice) {
                        System.out.println("set book price : " + currentValue);
                        currentBook.setPrice(Double.valueOf(currentValue));
                        bPrice = false;
                    }
                    if (bPublishDate) {
                        System.out.println("set book publish date : " + currentValue);
                        currentBook.setPublishDate(formatter.parse(currentValue));
                        bPublishDate = false;
                    }
                    if (bDescription) {
                        System.out.println("set book description : " + currentValue);
                        currentBook.setDescription(currentValue);
                        bDescription = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("book")) {
                        System.out.println("End Element : book");
                        categoryAndBooks.get(currentCategoryTitle).add(currentBook);
                    }
                    break;

            }
        }
    }
}
