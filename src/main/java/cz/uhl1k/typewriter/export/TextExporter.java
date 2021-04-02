package cz.uhl1k.typewriter.export;

import cz.uhl1k.typewriter.exceptions.LineTooShortException;
import cz.uhl1k.typewriter.model.Book;
import cz.uhl1k.typewriter.model.Chapter;
import cz.uhl1k.typewriter.model.Poem;
import cz.uhl1k.typewriter.model.Section;

import java.io.*;

public class TextExporter extends Exporter{

  private int lineLength = 75;

  public int getLineLength() {
    return lineLength;
  }

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
      writer.printf("%n%n          %-" + lineLength + "s%n%n          %-" + lineLength + "s%n%n",
          book.getTitle(), book.getAuthor());
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
