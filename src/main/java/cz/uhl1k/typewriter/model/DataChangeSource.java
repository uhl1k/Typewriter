package cz.uhl1k.typewriter.model;

/**
 * Source for data change events.
 */
public interface DataChangeSource {
  /**
   * Registers a new listener for data changes.
   * @param listener Listener to register.
   */
  void registerListener(DataChangeListener listener);

  /**
   * Removes listener for data changes.
   * @param listener Listener to remove.
   */
  void unregisterListener(DataChangeListener listener);
}
