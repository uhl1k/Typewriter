package cz.uhl1k.typewriter.model;

import javax.swing.*;
import java.io.File;

public class Data {

  private File openedFile;

  private boolean unsavedChanges;

  private DefaultListModel<Book> books;

  public void load(File file) {

  }

  public void save() {

  }

  public void saveAs() {

  }

  public void close() {

  }

  public boolean hasUnsavedChanges() {
    return unsavedChanges;
  }

  public DefaultListModel<Book> getBooks() {

  }

  public void addBook(Book book) {

  }

  public boolean hasBook(Book book) {

  }

  public void removeBook(Book book) {

  }

  public File getOpenedFile() {
    return openedFile;
  }

  public boolean hasOpenedFile() {
    return openedFile != null;
  }
}
