package cz.uhl1k.typewriter.model;

public interface DataChangeSource {
  void registerListener(DataChangeListener listener);
  void unregisterListener(DataChangeListener listener);
}
