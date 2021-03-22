package cz.uhl1k.typewriter.exceptions;

public class LineTooShortException extends Exception {
  public LineTooShortException(String description) {
    super(description);
  }
}
