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

import cz.uhl1k.typewriter.model.Section;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ResourceBundle;
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

/** Dialog for editing Section. */
public class EditSection extends JDialog {

  private final JTextField title;
  ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");
  Section section;

  /**
   * Creates and opens a new dialog for editing section.
   *
   * @param section Section to edit.
   */
  public EditSection(Section section, JFrame parent) {
    this.section = section;

    setLayout(new GridLayout(3, 1));
    ((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

    //  title of the section
    add(new JLabel(bundle.getString("title"), SwingConstants.CENTER));
    title = new JTextField(section.getTitle());
    title.setBorder(
        BorderFactory.createCompoundBorder(
            title.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    title
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
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

    //  ok button
    JButton ok = new JButton(bundle.getString("ok"));
    ok.addActionListener(e -> clickedOk());
    ok.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 0, 0, 0), ok.getBorder()));
    add(ok);

    //  setting basic parameters of the window
    setLocationRelativeTo(parent);
    setResizable(false);
    setMinimumSize(new Dimension(250, 10));
    pack();
    setModal(true);
    setTitle(bundle.getString("sectionEditing") + " " + section.getTitle());
    setVisible(true);
  }

  private void titleChanged() {
    section.setTitle(title.getText());
  }

  private void clickedOk() {
    dispose();
  }
}
