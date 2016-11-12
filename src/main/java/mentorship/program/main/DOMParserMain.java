package mentorship.program.main;


import mentorship.program.model.Book;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DOMParserMain {

    private static final String TEST_FILE_PATH = "XML_TASK.xml";
    private static Map<String, List<Book>> books = new HashMap<String, List<Book>>();
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

    private static void printResult(){

        for(Map.Entry<String,List<Book>> entry : books.entrySet()){
            System.out.println("catalogue title : " + entry.getKey());
            entry.getValue().forEach(System.out::println);
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ParseException {
        populateModuleByDOM();
        printResult();
    }

    private static void populateModuleByDOM()throws ParserConfigurationException, IOException, SAXException, ParseException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File testFile = new File(classloader.getResource(TEST_FILE_PATH).getFile());

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(testFile);


        NodeList nodeList = document.getDocumentElement().getChildNodes();

        List<Book> currentBookList = null;
        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Book currentBook = null;
                if ("title".equals(((Element) node).getTagName())) {
                    String categoryTitle = node.getTextContent();
                    currentBookList = new ArrayList<>();
                    books.put(categoryTitle, currentBookList);
                } else if ("booksList".equals(((Element) node).getTagName())) {

                    NodeList childNodes = node.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node cNode = childNodes.item(j);

                        //Identifying the child tag of employee encountered.
                        if (cNode instanceof Element && "book".equals((cNode.getNodeName()))) {
                                currentBook = new Book();
                                NodeList bookChildNodes = cNode.getChildNodes();
                                for (int a = 0; a < childNodes.getLength(); a++) {

                                    Node bookAttribute = bookChildNodes.item(a);

                                    if (bookAttribute instanceof Element) {
                                        String bookAttributeContent = bookAttribute.
                                                getTextContent().trim();
                                        switch (bookAttribute.getNodeName()) {
                                            case "author":
                                                currentBook.setAuthor(bookAttributeContent);
                                                break;
                                            case "title":
                                                currentBook.setTitle(bookAttributeContent);
                                                break;
                                            case "genre":
                                                currentBook.setGenre(bookAttributeContent);
                                                break;
                                            case "price":
                                                currentBook.setPrice(Double.valueOf(bookAttributeContent));
                                                break;
                                            case "publish_date":
                                                currentBook.setPublishDate(formatter.parse(bookAttributeContent));
                                                break;
                                            case "description":
                                                currentBook.setDescription(bookAttributeContent);
                                                break;


                                        }
                                    }

                                }
                            if (currentBookList != null && currentBook != null)
                                currentBookList.add(currentBook);
                        }
                    }

                }
            }
        }
    }
}