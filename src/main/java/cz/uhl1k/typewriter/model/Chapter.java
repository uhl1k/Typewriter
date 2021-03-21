package cz.uhl1k.typewriter.model;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representation of chapter in typewriter.
 */
public class Chapter extends Section {

  /**
   * Creates a new chapter with given parameters.
   * @param title Title of the chapter.
   */
  public Chapter(String title) {
    this(title, "");
  }

  /**
   * Creates a new chapter with given parameters.
   * @param title Title of the new chapter.
   * @param content Content of the new chapter.
   */
  public Chapter(String title, String content) {
    this(title, content, LocalDateTime.now(), LocalDateTime.now());
  }

  /**
   * Creates a new chapter with given parameters.
   * @param title Title of the new chapter.
   * @param content Content of the new chapter.
   * @param created Date of creation of the chapter.
   * @param modified Date of last modification of the chapter.
   */
  public Chapter(String title, String content, LocalDateTime created, LocalDateTime modified) {
    this.title = title;
    this.content = content;
    this.created = created;
    this.modified = modified;
  }

  @Override
  public void toXml(XMLStreamWriter writer) throws XMLStreamException {
    writer.writeStartElement("chapter");
    writer.writeAttribute("title", title);
    writer.writeAttribute("created", created.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    writer.writeAttribute("modified", modified.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    writer.writeCharacters(content);
    writer.writeEndElement();
  }
}
