package cz.uhl1k.typewriter.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Section implements XmlSerializable, DataChangeSource {
  protected String title;
  protected String content;

  protected LocalDateTime created;
  protected LocalDateTime modified;

  private List<DataChangeListener> listeners = new ArrayList<>();

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public LocalDateTime getModified() {
    return modified;
  }

  @Override
  public void registerListener(DataChangeListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  @Override
  public void unregisterListener(DataChangeListener listener) {
    listeners.remove(listener);
  }

  private void fireDataChange() {
    listeners.forEach(l -> l.dataChanged());
  }
}
