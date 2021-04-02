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
