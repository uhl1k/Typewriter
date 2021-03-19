package cz.uhl1k.typewriter.model;

/**
 * The listener for data changes in books, chapters and poems.
 */
public interface DataChangeListener {
  /**
   * Called when there is a data change.
   */
  void dataChanged();
}
