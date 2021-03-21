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
import cz.uhl1k.typewriter.model.Section;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
    setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
    setLayout(new BorderLayout());
    setJMenuBar(buildMenuBar());
    add(buildToolBar(), BorderLayout.NORTH);

    books = new JList<>();
    sections = new JList<>();
    content = new JTextArea();



    var vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, books, sections);
    var horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vertical, content);

    vertical.setDividerLocation(100);
    horizontal.setDividerLocation(200);

    add(horizontal, BorderLayout.CENTER);
  }

  private JMenuBar buildMenuBar() {
    var menu = new JMenuBar();

    //  File menu
    var file = new JMenu(bundle.getString("file"));
    file.setMnemonic('F');
    menu.add(file);

    var create = new JMenuItem(bundle.getString("create"));
    create.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
    file.add(create);

    var open = new JMenuItem(bundle.getString("open"));
    open.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
    file.add(open);

    var save = new JMenuItem(bundle.getString("save"));
    save.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
    file.add(save);

    var saveAs = new JMenuItem(bundle.getString("saveAs"));
    file.add(saveAs);

    file.addSeparator();

    var exportText = new JMenuItem(bundle.getString("exportText"));
    file.add(exportText);

    file.addSeparator();

    var close = new JMenuItem(bundle.getString("close"));
    close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
    close.addActionListener(s -> close());
    file.add(close);

    //  Help menu
    var help = new JMenu(bundle.getString("help"));
    help.setMnemonic('H');
    menu.add(help);

    var helpI = new JMenuItem(bundle.getString("help"));
    helpI.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_DOWN_MASK));
    help.add(helpI);

    help.addSeparator();

    var license = new JMenuItem(bundle.getString("license"));
    help.add(license);

    var about = new JMenuItem(bundle.getString("about"));
    help.add(about);

    return menu;
  }

  private JToolBar buildToolBar() {
    var toolBar = new JToolBar(bundle.getString("typewriter") + "| " + bundle.getString("toolbar"));

    var newBook = new JButton(new ImageIcon(getClass().getResource("/ico/addBook.png")));
    newBook.setToolTipText(bundle.getString("newBook"));
    toolBar.add(newBook);

    var editBook = new JButton(new ImageIcon(getClass().getResource("/ico/editBook.png")));
    editBook.setToolTipText(bundle.getString("editBook"));
    toolBar.add(editBook);

    var deleteBook = new JButton(new ImageIcon(getClass().getResource("/ico/deleteBook.png")));
    deleteBook.setToolTipText(bundle.getString("deleteBook"));
    toolBar.add(deleteBook);

    toolBar.addSeparator();

    var addChapter = new JButton(new ImageIcon(getClass().getResource("/ico/addChapter.png")));
    addChapter.setToolTipText(bundle.getString("newChapter"));
    toolBar.add(addChapter);

    var addPoem = new JButton(new ImageIcon(getClass().getResource("/ico/addPoem.png")));
    addPoem.setToolTipText(bundle.getString("newPoem"));
    toolBar.add(addPoem);

    var editSection = new JButton(new ImageIcon(getClass().getResource("/ico/editSection.png")));
    editSection.setToolTipText(bundle.getString("editSection"));
    toolBar.add(editSection);

    var deleteSection = new JButton(new ImageIcon(getClass().getResource("/ico/deleteSection.png")));
    deleteSection.setToolTipText(bundle.getString("deleteSection"));
    toolBar.add(deleteSection);

    var moveUp = new JButton(new ImageIcon(getClass().getResource("/ico/moveUp.png")));
    moveUp.setToolTipText(bundle.getString("moveUp"));
    toolBar.add(moveUp);

    var moveDown = new JButton(new ImageIcon(getClass().getResource("/ico/moveDown.png")));
    moveDown.setToolTipText(bundle.getString("moveDown"));
    toolBar.add(moveDown);

    toolBar.addSeparator();

    var exportText = new JButton(new ImageIcon(getClass().getResource("/ico/exportText.png")));
    exportText.setToolTipText(bundle.getString("exportSelectedBookAsText"));
    toolBar.add(exportText);

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
