package cz.uhl1k.typewriter.model;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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

  public void save() {

  }

  public void saveAs(File file) {

  }

  public void close() {

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
