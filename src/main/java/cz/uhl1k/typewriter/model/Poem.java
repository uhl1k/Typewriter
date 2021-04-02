package cz.uhl1k.typewriter.model;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representation of poem in Typewriter.
 */
public class Poem extends Section {

  /**
   * Creates a new poem with given parameters.
   * @param title Title of the new poem.
   */
  public Poem(String title) {
    this(title, "");
  }

  /**
   * Creates a new poem with given parameters.
   * @param title Title of the new poem.
   * @param content Content of the new poem.
   */
  public Poem(String title, String content) {
    this(title, content, LocalDateTime.now(), LocalDateTime.now());
  }

  /**
   * Creates a new poem with given parameters.
   * @param title Title of the new poem.
   * @param content Content of the new poem.
   * @param created Date of creation of the new poem.
   * @param modified Date of modification of the new poem.
   */
  public Poem(String title, String content, LocalDateTime created, LocalDateTime modified) {
    this.title = title;
    this.content = content;
    this.created = created;
    this.modified = modified;
  }

  @Override
  public void toXml(XMLStreamWriter writer) throws XMLStreamException {
    writer.writeStartElement("poem");
    writer.writeAttribute("title", title);
    writer.writeAttribute("created", created.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    writer.writeAttribute("modified", modified.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    writer.writeCharacters(content);
    writer.writeEndElement();
  }
}
