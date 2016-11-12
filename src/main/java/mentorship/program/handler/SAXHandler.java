package mentorship.program.handler;

import mentorship.program.model.Book;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SAXHandler extends DefaultHandler {

    public Map<String, List<Book>> books  = new HashMap<String, List<Book>>();

    private Stack<String> elementStack = new Stack<String>();
    private Stack<Object> objectStack  = new Stack<Object>();

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

    @Override
    public void startElement (String uri, String localName,
                              String qName, org.xml.sax.Attributes attributes)
            throws SAXException{

        this.elementStack.push(qName);

        if("book".equals(qName)){
            Book book = new Book();
            this.objectStack.push(book);
        }
    }

    public void endElement (String uri, String localName, String qName)
            throws SAXException{
        this.elementStack.pop();

        if("book".equals(qName)){
            Book book = (Book)this.objectStack.pop();
            List<Book> catalogueBookList = (List<Book>) this.objectStack.peek();
            catalogueBookList.add(book);
         }
    }

    public void characters(char ch[], int start, int length)
            throws org.xml.sax.SAXException {

        String value = new String(ch, start, length).trim();
        if(value.length() == 0) return; // ignore white space

        if("author".equals(currentElement())){
            Book book = (Book) this.objectStack.peek();
            book.setAuthor(value);
        }else if("title".equals(currentElement() ) && !"catalog".equals(previousElement())){
             Book book = (Book)this.objectStack.peek();
            book.setTitle(value);
        }else if("title".equals(currentElement()) && "catalog".equals(previousElement())){
            List<Book> catalogueBookList = new ArrayList<Book>();
            this.objectStack.push(catalogueBookList);
            this.books.put(value, catalogueBookList);
        }else if("genre".equals(currentElement())){
            Book book = (Book) this.objectStack.peek();
            book.setGenre(value);
        } else if("price".equals(currentElement())){
            Book book = (Book) this.objectStack.peek();
            book.setPrice(Double.valueOf(value));
        } else if("publish_date".equals(currentElement())){
            Book book = (Book) this.objectStack.peek();
            try {
                book.setPublishDate(formatter.parse(value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if("description".equals(currentElement())){
            Book book = (Book) this.objectStack.peek();
            book.setDescription(value);
        }
    }

    private String currentElement() {
        return this.elementStack.peek();
    }

    private String previousElement() {
        if(this.elementStack.size() < 2) return null;
        return this.elementStack.get(this.elementStack.size()-2);
    }

}
