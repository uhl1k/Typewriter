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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base for Chapter and Poem.
 */
public abstract class Section implements XmlSerializable, DataChangeSource {
  protected String title;
  protected String content;

  protected LocalDateTime created;
  protected LocalDateTime modified;

  private List<DataChangeListener> listeners = new ArrayList<>();

  /**
   * Returns the title.
   * @return The title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title. If the new title is same as the old one, nothing will happen.
   * @param title New title.
   */
  public void setTitle(String title) {
    if (!this.title.equals(title)) {
      this.title = title;
      fireDataChange(DataChangeEvent.CHAPTER_TITLE);
      modified = LocalDateTime.now();
    }
  }

  /**
   * Returns the content.
   * @return The content.
   */
  public String getContent() {
    return content;
  }

  /**
   * Sets the new content. If the new content is same as the odl one, nothing will happen.
   * @param content New content.
   */
  public void setContent(String content) {
    if (!this.content.equals(content)) {
      this.content = content;
      fireDataChange(DataChangeEvent.CHAPTER_CONTENT);
      modified = LocalDateTime.now();
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

  private void fireDataChange(DataChangeEvent event) {
    listeners.forEach(l -> l.dataChanged(event));
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (o instanceof Section) {
      Section s = (Section) o;
      return s.getTitle().equals(this.title);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return title.hashCode();
  }

  @Override
  public String toString() {
    return title;
  }
}
