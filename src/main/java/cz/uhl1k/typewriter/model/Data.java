/*
  Typewriter - simple novel and poem writing software
  Copyright (C) 2021  uhl1k (Roman Janků)

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package cz.uhl1k.typewriter.model;

import cz.uhl1k.typewriter.exceptions.NoFileSpecifiedException;
import org.xml.sax.SAXException;
import javax.swing.DefaultListModel;
import javax.xml.parsers.ParserConfigurationException;
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
public final class Data implements DataChangeListener, DataChangeSource, FileChangeSource{

  private File openedFile;
  private boolean unsavedChanges;
  private DefaultListModel<Book> books;
  private static Data instance;

  private List<DataChangeListener> dataChangeListeners;
  private List<FileChangeListener> fileChangeListeners;

  private Data() {
    unsavedChanges = false;
    books = new DefaultListModel<>();
    dataChangeListeners = new ArrayList<>();
    fileChangeListeners = new ArrayList<>();
  }

  /**
   * Returns the only instance of this class.
   * @return The only instance of this class.
   */
  public static Data getInstance() {
    if(instance == null) {
      synchronized (Data.class) {
        if (instance == null) {
          instance = new Data();
        }
      }
    }
    return instance;
  }

  public void load(File file) {

  }

  /**
   * Saves
   * data to a current file.
   * @throws NoFileSpecifiedException When there is no current file specified.
   * @throws IOException When an error occurred when saving file.
   */
  public void save() throws NoFileSpecifiedException, IOException {
    if (openedFile == null) {
      throw new NoFileSpecifiedException("No file specified!");
    }

    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    try {
      XMLStreamWriter writer = factory.createXMLStreamWriter(new BufferedWriter(new FileWriter(openedFile)));
      writer.writeStartDocument("UTF-8","1.0");
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
      unsavedChanges = false;
    } catch (XMLStreamException ex) {

    }
  }

  /**
   * Saves data to anew file and sets this file as a opened file.
   * @param file File to save to and set as opened file.
   * @throws IOException When an error occurred during saving file.
   */
  public void saveAs(File file) throws IOException {
    openedFile = file;
    fireFileChange();
    try {
      save();
    } catch (NoFileSpecifiedException ex) {
      // This should never happen
    }
  }

  /**
   * Opens a new file. If there is file opened, it will be closed without saving changes.
   * @param file File to open.
   * @throws SAXException When an error occurred when parsing the file.
   * @throws IOException When an error occurred when opening the file.
   * @throws ParserConfigurationException When an error occurred when parsing the file.
   */
  public void open(File file) throws SAXException, IOException, ParserConfigurationException {
    clear();
    openedFile = file;
    fireFileChange();

    TpwFileHandler handler = TpwFileHandler.getHandler();
    handler.parseFile(openedFile);

    unsavedChanges = false;
  }

  /**
   * Clears the data.
   */
  public void clear() {
    books.clear();
    unsavedChanges = false;
    openedFile = null;
    fireFileChange();
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
      int index = 0;
      while (index < books.getSize() && book.compareTo(books.getElementAt(index)) < 0) {
        index++;
      }
      index++;
      if (index >= books.getSize()) {
        books.addElement(book);
      } else {
        books.insertElementAt(book, index);
      }
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
    dataChangeListeners.forEach(l -> l.dataChanged());
  }

  private void fireFileChange() {
    fileChangeListeners.forEach(l -> l.fileChanged(openedFile));
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
    if (!dataChangeListeners.contains(listener)) {
      dataChangeListeners.add(listener);
    }
  }

  @Override
  public void unregisterListener(DataChangeListener listener) {
    dataChangeListeners.remove(listener);
  }

  @Override
  public void registerListener(FileChangeListener listener) {
    if (!fileChangeListeners.contains(listener)) {
      fileChangeListeners.add(listener);
    }
  }

  @Override
  public void unregisterListener(FileChangeListener listener) {
    fileChangeListeners.remove(listener);
  }
}
