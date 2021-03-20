package cz.uhl1k.typewriter.gui;

import cz.uhl1k.typewriter.model.Book;
import cz.uhl1k.typewriter.model.Data;
import cz.uhl1k.typewriter.model.Section;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.util.ResourceBundle;

public class MainWindow extends JFrame {

  ResourceBundle bundle = ResourceBundle.getBundle("bundle");

  JList<Book> books;
  JList<Section> sections;
  JTextArea content;

  public MainWindow() {
    buildGui();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(600, 400));
    setTitle(bundle.getString("typewriter"));
    setVisible(true);
  }

  private void buildGui() {
    setLayout(new BorderLayout());
    setJMenuBar(buildMenuBar());
    add(buildToolBar(), BorderLayout.NORTH);

    books = new JList<>();
    sections = new JList<>();
    content = new JTextArea();



    var vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, books, sections);
    var horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vertical, content);

    vertical.setDividerLocation(300);
    horizontal.setDividerLocation(150);

    add(horizontal, BorderLayout.CENTER);
  }

  private JMenuBar buildMenuBar() {
    var menu = new JMenuBar();

    var file = new JMenu(bundle.getString("file"));
    menu.add(file);

    file.addSeparator();

    JMenuItem close = new JMenuItem(bundle.getString("close"));
    close.addActionListener(s -> close());
    file.add(close);

    return menu;
  }

  private JToolBar buildToolBar() {
    var toolBar = new JToolBar();
    return toolBar;
  }

  private void close() {
    if (true || Data.getInstance().hasUnsavedChanges()) {
      int option = JOptionPane.showOptionDialog(
          this,
          bundle.getString("unsavedChanges"),
          bundle.getString("quitQuestion"),
          JOptionPane.YES_NO_CANCEL_OPTION,
          JOptionPane.WARNING_MESSAGE,
          null,
          new String[]{bundle.getString("yes"), bundle.getString("no"), bundle.getString("cancel")},
          1);

      switch (option) {
        case 0:
          //TODO - saving the changes
        case 1:
          System.exit(0);
          break;
        default:
          return;
      }
    }
  }
}
