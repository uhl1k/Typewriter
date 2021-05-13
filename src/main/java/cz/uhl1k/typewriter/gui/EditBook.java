/*
  Typewriter - simple novel and poem writing software
  Copyright (C) 2021  uhl1k (Roman Janků)

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package cz.uhl1k.typewriter.gui;

import cz.uhl1k.typewriter.model.Book;
import cz.uhl1k.typewriter.model.Data;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ResourceBundle;

/**
 * Dialog for editing book.
 */
public class EditBook extends JDialog {

  ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  private JTextField title;
  private JTextField author;

  Book book;

  /**
   * Creates and opens a new dialog for editing book.
   * @param book Book to edit.
   */
  public EditBook(Book book, JFrame parent) {
    this.book = book;

    setLayout(new GridLayout(5, 1));
    ((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

    add(new JLabel(bundle.getString("title"), SwingConstants.CENTER));
    title = new JTextField(book.getTitle());
    title.setBorder(BorderFactory.createCompoundBorder(
        title.getBorder(),
        BorderFactory.createEmptyBorder(5,5,5,5)));
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
    author.setBorder(BorderFactory.createCompoundBorder(
        author.getBorder(),
        BorderFactory.createEmptyBorder(5,5,5,5)));
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

    setLocationRelativeTo(parent);
    setResizable(false);
    setMinimumSize(new Dimension(250, 50));
    pack();
    setModal(true);
    setTitle(bundle.getString("bookEditing") + " " + book.getTitle());
    setVisible(true);
  }

  private void titleChanged() {
    if (
        !Data.getInstance().hasBook(new Book(title.getText(), "")) &&
        title.getText().length() > 0
    ) {
      book.setTitle(title.getText());
      setTitle(bundle.getString("bookEditing") + " " + title.getText());
    }
  }

  private void authorChanged() {
    book.setAuthor(author.getText());
  }

  private void clickedOk () {
    dispose();
  }
}
