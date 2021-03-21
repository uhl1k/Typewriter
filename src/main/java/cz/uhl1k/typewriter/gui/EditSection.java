package cz.uhl1k.typewriter.gui;

import cz.uhl1k.typewriter.model.Section;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ResourceBundle;

public class EditSection extends JDialog {

  ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  private JTextField title;

  Section section;

  public EditSection(Section section) {
    this.section = section;

    setLayout(new GridLayout(3, 1));
    ((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

    add(new JLabel(bundle.getString("title"), SwingConstants.CENTER));
    title = new JTextField(section.getTitle());
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

    JButton ok = new JButton(bundle.getString("ok"));
    ok.addActionListener(e -> clickedOk());
    ok.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(10, 0, 0, 0),
        ok.getBorder()
    ));
    add(ok);

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

  private void clickedOk () {
    dispose();
  }
}
