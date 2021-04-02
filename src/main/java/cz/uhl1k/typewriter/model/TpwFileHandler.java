package cz.uhl1k.typewriter.model;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.time.LocalDateTime;

class TpwFileHandler {
  private boolean meta = false;
  private boolean data = false;
  private boolean book = false;
  private boolean chapter = false;
  private boolean poem = false;

  private Book lastBook = null;

  private static TpwFileHandler handler = new TpwFileHandler();

  private TpwFileHandler() {

  }

  public static TpwFileHandler getHandler() {
    return handler;
  }

  public void parseFile(File file) {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    try (BufferedReader br = new BufferedReader(new FileReader(file))){
      XMLEventReader reader = factory.createXMLEventReader(br);

      while (reader.hasNext()) {
        XMLEvent event = reader.nextEvent();
        String name = "";

        switch (event.getEventType()) {
          case XMLStreamConstants.START_ELEMENT:
            StartElement startElement = event.asStartElement();
            name = startElement.getName().getLocalPart();
            switch (name.toLowerCase()) {
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
                      startElement.getAttributeByName(new QName("title")).getValue(),
                      startElement.getAttributeByName(new QName("author")).getValue(),
                      LocalDateTime.parse(startElement.getAttributeByName(new QName("created")).getValue()),
                      LocalDateTime.parse(startElement.getAttributeByName(new QName("modified")).getValue())
                  );
                  Data.getInstance().addBook(book);
                  lastBook = book;
                }
                break;

              case "poem":
                if (book) {
                  poem = true;
                  Poem poem = new Poem(
                      startElement.getAttributeByName(new QName("title")).getValue(),
                    "",
                    LocalDateTime.parse(startElement.getAttributeByName(new QName("created")).getValue()),
                    LocalDateTime.parse(startElement.getAttributeByName(new QName("modified")).getValue())
                  );
                  lastBook.addSection(poem);
                }
                break;

              case "chapter":
                if (book) {
                  chapter = true;
                  Chapter chapter = new Chapter(
                      startElement.getAttributeByName(new QName("title")).getValue(),
                      "",
                      LocalDateTime.parse(startElement.getAttributeByName(new QName("created")).getValue()),
                      LocalDateTime.parse(startElement.getAttributeByName(new QName("modified")).getValue())
                  );
                  lastBook.addSection(chapter);
                }
                break;
            }
            break;
          case XMLStreamConstants.END_ELEMENT:
            EndElement endElement = event.asEndElement();
            name = endElement.getName().getLocalPart();
            switch (name.toLowerCase()) {
              case "meta":
                meta = false;
                break;

              case "data":
                data = false;
                break;

              case "book":
                book = false;
                break;

              case "poem":
                poem = false;
                break;

              case "chapter":
                chapter = false;
                break;
            }
            break;
          case XMLStreamConstants.CHARACTERS:
            if (poem || chapter) {
              getLastSection().setContent(event.asCharacters().getData());
            }
            break;
        }
      }

    } catch (FileNotFoundException ex) {

    } catch (IOException ex) {

    } catch (XMLStreamException ex) {

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
