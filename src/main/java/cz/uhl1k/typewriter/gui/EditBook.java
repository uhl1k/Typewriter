package cz.uhl1k.typewriter.gui;

import cz.uhl1k.typewriter.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ResourceBundle;

public class EditBook extends JDialog {

  ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  private JTextField title;
  private JTextField author;

  Book book;


  public EditBook(JFrame parent, Book book) {
    this.book = book;

    setLayout(new GridLayout(5, 1));
    ((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

    add(new JLabel(bundle.getString("title"), SwingConstants.CENTER));
    title = new JTextField(book.getTitle());
    title.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        titleChanged();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        titleChanged();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        titleChanged();
      }
    });
    add(title);

    add(new JLabel(bundle.getString("author"), SwingConstants.CENTER));
    author = new JTextField(book.getAuthor());
    author.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        authorChanged();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        authorChanged();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        authorChanged();
      }
    });
    add(author);

    JButton ok = new JButton(bundle.getString("ok"));
    ok.addActionListener(e -> clickedOk());
    ok.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(10, 0, 0, 0),
        ok.getBorder()
    ));
    add(ok);

    setResizable(false);
    setMinimumSize(new Dimension(200, 50));
    pack();
    setModal(true);
    setTitle(bundle.getString("bookEditing") + " " + book.getTitle());
    setVisible(true);
  }

  private void titleChanged() {
    book.setTitle(title.getText());
  }

  private void authorChanged() {
    book.setAuthor(author.getText());
  }

  private void clickedOk () {
    dispose();
  }
}
