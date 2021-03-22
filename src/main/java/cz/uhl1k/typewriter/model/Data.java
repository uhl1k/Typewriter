package cz.uhl1k.typewriter.model;

import cz.uhl1k.typewriter.exceptions.NoFileSpecifiedException;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for manipulation with data. This class is singleton.
 */
public final class Data implements DataChangeListener, DataChangeSource{

  private File openedFile;
  private boolean unsavedChanges;
  private DefaultListModel<Book> books;
  private static Data INSTANCE;

  private List<DataChangeListener> listeners;

  private Data() {
    unsavedChanges = false;
    books = new DefaultListModel<>();
    listeners = new ArrayList<>();
  }

  /**
   * Returns the only instance of this class.
   * @return The only instance of this class.
   */
  public static Data getInstance() {
    if(INSTANCE == null) {
      synchronized (Data.class) {
        if (INSTANCE == null) {
          INSTANCE = new Data();
        }
      }
    }
    return INSTANCE;
  }

  public void load(File file) {

  }

  public void save() throws NoFileSpecifiedException, IOException {
    if (openedFile == null) {
      throw new NoFileSpecifiedException("No file specified!");
    }

    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    try {
      XMLStreamWriter writer = factory.createXMLStreamWriter(new BufferedWriter(new FileWriter(openedFile)));
      writer.writeStartDocument();
      writer.writeStartElement("library");

      writer.writeStartElement("meta");
      writer.writeEndElement();

      writer.writeStartElement("data");

      for (int i = 0; i < books.getSize(); i++) {
        books.getElementAt(i).toXml(writer);
      }

      writer.writeEndElement();

      writer.writeEndElement();
      writer.writeEndDocument();

      writer.flush();
      writer.close();
    } catch (XMLStreamException ex) {

    }
  }

  public void saveAs(File file) throws IOException {
    openedFile = file;
    try {
      save();
    } catch (NoFileSpecifiedException ex) {
      // This should never happen
    }
  }

  public void open(File file) throws SAXException, IOException, ParserConfigurationException {
    clear();
    openedFile = file;

    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();
    TpwFileHandler handler = new TpwFileHandler();
    parser.parse(openedFile, handler);

    unsavedChanges = false;
  }

  public void clear() {
    books.clear();
    unsavedChanges = false;
    openedFile = null;
  }

  /**
   * Tells weather there are unsaved changes in the data.
   * @return TRUE if there are unsaved changes, FALSE otherwise.
   */
  public boolean hasUnsavedChanges() {
    return unsavedChanges;
  }

  /**
   * Returns all the books.
   * @return All the books.
   */
  public DefaultListModel<Book> getBooks() {
    return books;
  }

  /**
   * Adds a new book. If there is a book with a same name, nothing will happen.
   * @param book A new book to add.
   */
  public void addBook(Book book) {
    if (!books.contains(book)) {
      books.addElement(book);
      book.registerListener(this);
      fireDataChange();
    }
  }

  /**
   * Tells weather there is a book with same name.
   * @param book Book to look for.
   * @return TRUE if there is a book with a same name, FALSE otherwise.
   */
  public boolean hasBook(Book book) {
    return books.contains(book);
  }

  /**
   * Removes book. If there is no book with a same name, nothing will happen.
   * @param book Book to remove.
   */
  public void removeBook(Book book) {
    if (books.contains(book)) {
      books.removeElement(book);
      book.unregisterListener(this);
      fireDataChange();
    }
  }

  private void fireDataChange() {
    unsavedChanges = true;
    listeners.forEach(l -> l.dataChanged());
  }

  /**
   * Tells weather a file is opened.
   * @return TRUE if file is opened, FALSE otherwise.
   */
  public File getOpenedFile() {
    return openedFile;
  }

  /**
   * Returns the opened file.
   * @return Opened file or null if no file is opened.
   */
  public boolean hasOpenedFile() {
    return openedFile != null;
  }

  @Override
  public void dataChanged() {
    fireDataChange();
  }

  @Override
  public void registerListener(DataChangeListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  @Override
  public void unregisterListener(DataChangeListener listener) {
    listeners.remove(listener);
  }
}
