package cz.uhl1k.typewriter.model;

/**
 * Marks the classes that are serializable to XML.
 */
public interface XmlSerializable {
  /**
   * Method for serialization to XML.
   */
  void toXml();
}
