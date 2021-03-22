package cz.uhl1k.typewriter.export;

import cz.uhl1k.typewriter.model.Book;

import java.io.File;

public abstract class Exporter {
  public abstract void exportToFile(Book book, File file);
}
