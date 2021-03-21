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

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of book in typewriter.
 */
public class Book implements DataChangeSource, DataChangeListener, Comparable<Book> {

  private String title;
  private String author;
  private LocalDateTime created;
  private LocalDateTime modified;

  private List<DataChangeListener> listeners;
  private DefaultListModel<Section> sections;

  /**
   * Creates a new book with given parameters.
   * @param title Title of the book.
   * @param author Author of the book.
   */
  public Book(String title, String author) {
    this(title, author, LocalDateTime.now(), LocalDateTime.now());
  }

  /**
   * Creates a new book with given parameters.
   * @param title Title of the book.
   * @param author Author of the book.
   * @param created Creation date of the book.
   * @param modified Date of last changes in the book.
   */
  public Book(String title, String author, LocalDateTime created, LocalDateTime modified) {
    this.title = title;
    this.author = author;
    this.created = created;
    this.modified = modified;

    listeners = new ArrayList<>();
    sections = new DefaultListModel<>();
  }

  /**
   * Returns the title of the book.
   * @return The title of the book.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets a new title of the book. If the new title is same as the old one, nothing happens.
   * @param title New title of the book.
   */
  public void setTitle(String title) {
    if (!this.title.equals(title)) {
      this.title = title;
      fireDataChange();
    }
  }

  /**
   * Returns the author of the book.
   * @return The author of the book.
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Sets new author of the book. If the new author is same as the old one, nothing happens.
   * @param author
   */
  public void setAuthor(String author) {
    if (!this.author.equals(author)) {
      this.author = author;
      fireDataChange();
    }
  }

  /**
   * Returns the date of creation.
   * @return The date of creation.
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the date of last modification.
   * @return The date of last modification.
   */
  public LocalDateTime getModified() {
    return modified;
  }

  /**
   * Returns all the sections (chapters and poems) in the book.
   * @return All the sections (chapters and poems) in the book.
   */
  public DefaultListModel<Section> getSections() {
    return sections;
  }

  /**
   * Adds section to the book. If there is a section with a same name, nothing will happen.
   * @param section Section to add.
   */
  public void addSection(Section section) {
    if (!sections.contains(section)) {
      sections.addElement(section);
      fireDataChange();
    }
  }

  /**
   * Removes section from the book. If there is no section with same name, nothing will happen.
   * @param section Section to remove.
   */
  public void removeSection(Section section) {
    if (sections.contains(section)) {
      sections.removeElement(section);
      fireDataChange();
    }
  }

  /**
   * Tells weather the book contains given section.
   * @param section Section to look for.
   * @return TRUE if book contains given section, FALSE otherwise.
   */
  public boolean hasSection(Section section) {
    return sections.contains(section);
  }

  private void fireDataChange() {
    modified = LocalDateTime.now();
    listeners.forEach(l -> l.dataChanged());
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

  @Override
  public int compareTo(Book book) {
    return book.getTitle().compareTo(this.title);
  }

  @Override
  public int hashCode() {
    return title.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    } else if (obj instanceof Book) {
      Book b = (Book) obj;
      return b.getTitle().equals(this.title);
    }
    return false;
  }

  @Override
  public String toString() {
    return title;
  }
}
