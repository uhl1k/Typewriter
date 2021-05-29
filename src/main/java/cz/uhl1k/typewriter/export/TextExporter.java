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

package cz.uhl1k.typewriter.export;

import cz.uhl1k.typewriter.exceptions.LineTooShortException;
import cz.uhl1k.typewriter.model.Book;
import cz.uhl1k.typewriter.model.Chapter;
import cz.uhl1k.typewriter.model.Poem;
import cz.uhl1k.typewriter.model.Section;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/** Exporter for exporting to a text file. Default length of line is 75 characters; */
public class TextExporter extends Exporter {

  private int lineLength = 75;

  /**
   * Returns the length of line.
   *
   * @return The length of line.
   */
  public int getLineLength() {
    return lineLength;
  }

  /**
   * Sets a line length for export. Line must be at least 20 characters long.
   *
   * @param lineLength New length of a line.
   * @throws LineTooShortException When the line length is too short.
   */
  public void setLineLength(int lineLength) throws LineTooShortException {
    if (lineLength < 20) {
      throw new LineTooShortException("Line must be at least 20 characters wide.");
    }
    this.lineLength = lineLength;
  }

  @Override
  public void exportToFile(Book book, File file) {
    try (PrintWriter writer = new PrintWriter(file)) {
      //  empty space at the beginning of document
      writer.printf("%n%n%n%n%n");

      //  first page of the book
      for (int i = 0; i < lineLength; i++) {
        writer.printf("=");
      }
      writer.printf(
          "%n%n          %-" + lineLength + "s%n%n          %-" + lineLength + "s%n%n",
          book.getTitle(),
          book.getAuthor());
      for (int i = 0; i < lineLength; i++) {
        writer.printf("=");
      }

      //  empty space between title and content
      writer.printf("%n%n%n%n%n");

      //  printing the content
      for (int i = 0; i < book.getSections().getSize(); i++) {
        Section section = book.getSections().get(i);

        writer.printf("%n%n    %-" + lineLength + "s%n", section.getTitle());
        for (int j = 0; j < lineLength; j++) {
          writer.printf("-");
        }
        writer.printf("%n%n");

        if (section instanceof Poem) {
          writer.printf("%-" + lineLength + "s%n%n", section.getContent());
        } else if (section instanceof Chapter) {
          String[] pars = section.getContent().split("\n");
          for (String par : pars) {
            writer.printf("   %-" + lineLength + "s%n%n", par);
          }
        }
      }

    } catch (IOException ex) {

    }
  }
}
