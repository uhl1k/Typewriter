package cz.uhl1k.typewriter.model;

/**
 * Describes the type of data that had been changed in this event.
 */
public enum DataChangeEvent {
  BOOK_TITLE,
  BOOK_AUTHOR,
  CHAPTER_TITLE,
  CHAPTER_CONTENT,
  SECTIONS_CHANGED,
  BOOKS_CHANGED
}
