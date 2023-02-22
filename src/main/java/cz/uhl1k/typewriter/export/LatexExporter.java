package cz.uhl1k.typewriter.export;

import cz.uhl1k.typewriter.model.Book;
import cz.uhl1k.typewriter.model.Poem;
import cz.uhl1k.typewriter.model.Section;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class LatexExporter extends Exporter {

  private String pageSize = "A5";

  public String getPageSize() {
    return pageSize;
  }

  public void setPageSize(String pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  public void exportToFile(Book book, File file) {
    try(PrintWriter writer = new PrintWriter(file)) {
      writer.println("\\documentclass[11pt]{book}");
      writer.println("\\usepackage[" + pageSize.toLowerCase() + "paper]{geometry}");
      writer.println("\\usepackage[utf8]{inputenc}");
      writer.println("\\usepackage[T1]{fontenc}");
      writer.println("\\usepackage{babel}");
      writer.println("\\usepackage[nodayofweek]{datetime}");
      writer.println();
      writer.println("\\title{" + book.getTitle() + "}");
      writer.println("\\author{" + book.getAuthor() + "}");
      writer.println("\\date{\\today}");
      writer.println();
      writer.println("\\begin{document}");
      writer.println();
      writer.println("\\maketitle");
      writer.println("\\tableofcontents");

      for (int i = 0; i < book.getSections().getSize(); i++) {
        Section section = book.getSections().get(i);
        writer.println();

        if(section instanceof Poem) {
          writer.println("\\newpage");
          writer.println("\\section*{" + section.getTitle() + "}");
          writer.println("\\addcontentsline{toc}{section}{" + section.getTitle() + "}");
          writer.println("\\noindent");
          section.getContent().lines().forEach(line -> writer.println(line + "\\\\"));
        } else {
          writer.println("\\chapter{" + section.getTitle() + "}");
          section.getContent().lines().forEach(line -> {
            writer.println(line);
            writer.println();
          });
        }
      }

      writer.println();
      writer.println("\\end{document}");
    } catch (IOException ex) {

    }
  }
}
