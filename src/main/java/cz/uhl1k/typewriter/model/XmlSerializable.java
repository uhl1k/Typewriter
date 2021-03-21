package cz.uhl1k.typewriter.model;

import org.w3c.dom.Document;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Marks the classes that are serializable to XML.
 */
public interface XmlSerializable {
  /**
   * Method for serialization to XML.
   */
  void toXml(XMLStreamWriter writer) throws XMLStreamException;
}
