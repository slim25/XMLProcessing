package mentorship.program.main;

import mentorship.program.model.Book;
import mentorship.program.model.Catalog;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SecondaryTaskMain {

    private static final String TEST_FILE_PATH = "XML_TASK.xml";
    private static final String SECOND_NAME = "O'Brien";

    private static final Float priceTrashhold = 5.95f;

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, JAXBException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File testFile = new File(classloader.getResource(TEST_FILE_PATH).getFile());

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(testFile);

        XPath xPath = XPathFactory.newInstance().newXPath();

        System.out.println("All books where second name " + SECOND_NAME + " : ");
        getAllBooksWhereSecondNameDOM(document, xPath, testFile, SECOND_NAME);
        System.out.println(" -------------------------------------------------- ");
        System.out.println("Books where price bigger than  " + priceTrashhold + " : ");
        getBooksWherePriceBiggerThan(document, xPath, priceTrashhold);
        System.out.println(" -------------------------------------------------- ");
        System.out.println("JaxB parser : ");
        jaxBParser(testFile);
    }

    private static void getAllBooksWhereSecondNameDOM(Document document, XPath xPath, File testFile, String secondName) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        NodeList nodes = (NodeList)xPath.evaluate("//book[starts-with(author,\"O'Brien\")] ",
                document.getDocumentElement(), XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); ++i) {
            Node outerNode = nodes.item(i);
            System.out.println(outerNode.getNodeName() + " : " + outerNode.getAttributes().item(0).getNodeValue());
            NodeList nodeList = outerNode.getChildNodes();
            int childNodesAmount = nodeList.getLength();

            for (int a = 0; a < childNodesAmount; a++){
                Node innerNode = nodeList.item(a);
                if(innerNode.getNodeType() == Node.ELEMENT_NODE){
                    System.out.println(innerNode.getNodeName() + " : " + innerNode.getTextContent());
                }
            }
        }
    }

    private static void getBooksWherePriceBiggerThan(Document doc, XPath xpath, float price) {
        try {
            XPathExpression expr =
                    xpath.compile("//book[price>" + price + "]/title/text()");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                System.out.println("Book title : " + nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private static void jaxBParser(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Catalog catalogue = (Catalog) jaxbUnmarshaller.unmarshal(file);
        System.out.println("Catalogue title : " + catalogue.getTitle());
        for (Book book : catalogue.getBooks()){
            System.out.println("id : " + book.getId());
            System.out.println("Title : " + book.getTitle());
            System.out.println("Author : " + book.getAuthor());
            System.out.println("Genre : " + book.getGenre());
            System.out.println("Price : " + book.getPrice());
            System.out.println("Publish date : " + book.getPublishDate());
            System.out.println("Description : " + book.getDescription());
            System.out.println(" ---");
        }
    }

}
