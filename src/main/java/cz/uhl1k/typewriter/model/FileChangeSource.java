package cz.uhl1k.typewriter.model;

public interface FileChangeSource {
  void registerListener (FileChangeListener listener);
  void unregisterListener (FileChangeListener listener);
}
