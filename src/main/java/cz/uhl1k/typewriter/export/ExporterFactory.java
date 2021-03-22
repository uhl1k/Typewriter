package cz.uhl1k.typewriter.export;

public class ExporterFactory {
  public static TextExporter getNewTextExporter() {
    return new TextExporter();
  }
}
