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

package cz.uhl1k.typewriter.data;

import cz.uhl1k.typewriter.Logging;
import cz.uhl1k.typewriter.model.Book;
import cz.uhl1k.typewriter.model.Chapter;
import cz.uhl1k.typewriter.model.Poem;
import cz.uhl1k.typewriter.model.Section;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/** Handler for parsing .tpw files. */
public class TpwFileHandler {
  private static final TpwFileHandler handler = new TpwFileHandler();
  private boolean meta = false;
  private boolean data = false;
  private boolean book = false;
  private boolean chapter = false;
  private boolean poem = false;
  private Book lastBook = null;

  private TpwFileHandler() {}

  private static ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  public static TpwFileHandler getHandler() {
    return handler;
  }

  public void parseFile(File file) {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      XMLEventReader reader = factory.createXMLEventReader(br);

      while (reader.hasNext()) {
        XMLEvent event = reader.nextEvent();
        String name = "";

        switch (event.getEventType()) {
          case XMLStreamConstants.START_ELEMENT:
            StartElement startElement = event.asStartElement();
            name = startElement.getName().getLocalPart();
            switch (name.toLowerCase()) {
              case "meta":
                if (!data) {
                  meta = true;
                }
                break;

              case "data":
                if (!meta) {
                  data = true;
                }
                break;

              case "book":
                if (data) {
                  book = true;
                  Book book =
                      new Book(
                          startElement.getAttributeByName(new QName("title")).getValue(),
                          startElement.getAttributeByName(new QName("author")).getValue(),
                          LocalDateTime.parse(
                              startElement.getAttributeByName(new QName("created")).getValue()),
                          LocalDateTime.parse(
                              startElement.getAttributeByName(new QName("modified")).getValue()));
                  Data.getInstance().addBook(book);
                  lastBook = book;
                }
                break;

              case "poem":
                if (book) {
                  poem = true;
                  Poem poem =
                      new Poem(
                          startElement.getAttributeByName(new QName("title")).getValue(),
                          "",
                          LocalDateTime.parse(
                              startElement.getAttributeByName(new QName("created")).getValue()),
                          LocalDateTime.parse(
                              startElement.getAttributeByName(new QName("modified")).getValue()));
                  lastBook.addSection(poem);
                }
                break;

              case "chapter":
                if (book) {
                  chapter = true;
                  Chapter chapter =
                      new Chapter(
                          startElement.getAttributeByName(new QName("title")).getValue(),
                          "",
                          LocalDateTime.parse(
                              startElement.getAttributeByName(new QName("created")).getValue()),
                          LocalDateTime.parse(
                              startElement.getAttributeByName(new QName("modified")).getValue()));
                  lastBook.addSection(chapter);
                }
                break;
            }
            break;
          case XMLStreamConstants.END_ELEMENT:
            EndElement endElement = event.asEndElement();
            name = endElement.getName().getLocalPart();
            switch (name.toLowerCase()) {
              case "meta":
                meta = false;
                break;

              case "data":
                data = false;
                break;

              case "book":
                book = false;
                break;

              case "poem":
                poem = false;
                break;

              case "chapter":
                chapter = false;
                break;
            }
            break;
          case XMLStreamConstants.CHARACTERS:
            if (poem || chapter) {
              getLastSection()
                  .setContent(getLastSection().getContent() + event.asCharacters().getData());
            }
            break;
        }
      }

    } catch (FileNotFoundException ex) {
      Logging.log("Error while opening .tpw file! Cause: " + ex.getMessage(), Level.SEVERE, ex.getStackTrace());
      JOptionPane.showMessageDialog(null, bundle.getString("wrongOpeningFile"), bundle.getString("error"), JOptionPane.ERROR_MESSAGE);
      Data.getInstance().clear();
    } catch (IOException ex) {
      Logging.log("Error while opening .tpw file! Cause: " + ex.getMessage(), Level.SEVERE, ex.getStackTrace());
      JOptionPane.showMessageDialog(null, bundle.getString("wrongOpeningFile"), bundle.getString("error"), JOptionPane.ERROR_MESSAGE);
      Data.getInstance().clear();
    } catch (XMLStreamException ex) {
      Logging.log("Error while opening .tpw file! Cause: " + ex.getMessage(), Level.SEVERE, ex.getStackTrace());
      JOptionPane.showMessageDialog(null, bundle.getString("wrongOpeningFile"), bundle.getString("error"), JOptionPane.ERROR_MESSAGE);
      Data.getInstance().clear();
    }
  }

  private Section getLastSection() {
    if (lastBook == null || lastBook.getSections().getSize() < 1) {
      return null;
    } else {
      return lastBook.getSections().lastElement();
    }
  }
}
