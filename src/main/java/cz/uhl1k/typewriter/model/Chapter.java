package cz.uhl1k.typewriter.model;

import java.time.LocalDateTime;

/**
 * Representation of chapter in typewriter.
 */
public class Chapter extends Section {

  /**
   * Creates a new chapter with given parameters.
   * @param title Title of the chapter.
   */
  public Chapter(String title) {
    this(title, "");
  }

  /**
   * Creates a new chapter with given parameters.
   * @param title Title of the new chapter.
   * @param content Content of the new chapter.
   */
  public Chapter(String title, String content) {
    this(title, content, LocalDateTime.now(), LocalDateTime.now());
  }

  /**
   * Creates a new chapter with given parameters.
   * @param title Title of the new chapter.
   * @param content Content of the new chapter.
   * @param created Date of creation of the chapter.
   * @param modified Date of last modification of the chapter.
   */
  public Chapter(String title, String content, LocalDateTime created, LocalDateTime modified) {
    this.title = title;
    this.content = content;
    this.created = created;
    this.modified = modified;
  }

  @Override
  public void toXml() {

  }
}
