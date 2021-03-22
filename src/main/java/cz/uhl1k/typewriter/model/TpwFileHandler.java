package cz.uhl1k.typewriter.model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDateTime;

class TpwFileHandler extends DefaultHandler {
  private boolean meta = false;
  private boolean data = false;
  private boolean book = false;
  private boolean chapter = false;
  private boolean poem = false;

  private Book lastBook = null;

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    switch (qName.toLowerCase()) {
      case "meta":
        meta = false;
        break;

      case "data":
        data = false;
        break;

      case "book":
        book = false;
        lastBook = null;
        break;

      case "poem":
        poem = false;
        break;

      case "chapter":
        chapter = false;
        break;
    }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    switch (qName.toLowerCase()) {
      case "meta":
        if (!data) {
          meta = true;
        }
        break;

      case "data":
        if (!meta) {
          data = true;
        }
        break;

      case "book":
        if (data) {
          book = true;
          Book book = new Book(
              attributes.getValue("title"),
              attributes.getValue("author"),
              LocalDateTime.parse(attributes.getValue("created")),
              LocalDateTime.parse(attributes.getValue("modified"))
          );
          Data.getInstance().addBook(book);
          lastBook = book;
        }
        break;

      case "poem":
        if (book) {
          poem = true;
          Poem poem = new Poem(
              attributes.getValue("title"),
              "",
              LocalDateTime.parse(attributes.getValue("created")),
              LocalDateTime.parse(attributes.getValue("modified"))
          );
          lastBook.addSection(poem);
        }
        break;

      case "chapter":
        if (book) {
          chapter = true;
          Chapter chapter = new Chapter(
              attributes.getValue("title"),
              "",
              LocalDateTime.parse(attributes.getValue("created")),
              LocalDateTime.parse(attributes.getValue("modified"))
          );
          lastBook.addSection(chapter);
        }
        break;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (poem || chapter) {
      getLastSection().setContent(new String(ch, start, length));
    }
  }

  private Section getLastSection() {
    if (lastBook == null || lastBook.getSections().getSize() < 1) {
      return null;
    } else {
      return lastBook.getSections().lastElement();
    }
  }
}
