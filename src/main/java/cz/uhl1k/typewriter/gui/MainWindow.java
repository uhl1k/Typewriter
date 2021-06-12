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

import cz.uhl1k.typewriter.Options;
import cz.uhl1k.typewriter.exceptions.NoFileSpecifiedException;
import cz.uhl1k.typewriter.exceptions.SettingsNotSavedException;
import cz.uhl1k.typewriter.export.ExporterFactory;
import cz.uhl1k.typewriter.export.TextExporter;
import cz.uhl1k.typewriter.model.Book;
import cz.uhl1k.typewriter.model.Chapter;
import cz.uhl1k.typewriter.data.Data;
import cz.uhl1k.typewriter.model.DataChangeEvent;
import cz.uhl1k.typewriter.model.DataChangeListener;
import cz.uhl1k.typewriter.model.FileChangeListener;
import cz.uhl1k.typewriter.model.Poem;
import cz.uhl1k.typewriter.model.Section;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/** The main window of this application. */
public class MainWindow extends JFrame implements DataChangeListener, FileChangeListener {

  ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  JList<Book> books;
  JList<Section> sections;
  JTextArea content;

  /** Creates and shows a main window. */
  public MainWindow() {
    buildGui();

    Data.getInstance().registerListener((DataChangeListener) this);
    Data.getInstance().registerListener((FileChangeListener) this);

    books.setModel(Data.getInstance().getBooks());

    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            close();
          }
        });

    addWindowStateListener(e ->{
      try {
        if ((e.getOldState() & Frame.MAXIMIZED_BOTH) == 0 && (e.getNewState() & Frame.MAXIMIZED_BOTH) != 0) {
          Options.getInstance().setValue("maximized", "yes");
        } else {
          Options.getInstance().setValue("maximized", "no");
        }
      } catch (SettingsNotSavedException settingsNotSavedException) {
        settingsNotSavedException.printStackTrace();
      }
    });

    String maximized = Options.getInstance().getValue("maximized");

    if (maximized == null) {
      try {
        Options.getInstance().setValue("maximized", "no");
      } catch (SettingsNotSavedException e) {
        //  just ignore, not much more we can do;
      }
    } else if (maximized.equalsIgnoreCase("yes")) {
      setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

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

    books.addListSelectionListener(e -> bookSelectionChanged());
    sections.addListSelectionListener(e -> sectionSelectionChanged());

    books.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
          editBook();
        }
      }
    });

    sections.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
          editSection();
        }
      }
    });

    books.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    sections.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    content
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
              @Override
              public void insertUpdate(DocumentEvent e) {
                textChanged();
              }

              @Override
              public void removeUpdate(DocumentEvent e) {
                textChanged();
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
                textChanged();
              }
            });

    content.setEnabled(false);
    content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    Font font = new Font(
        Options.getInstance().getValue("font-name"),
        Integer.decode(Options.getInstance().getValue("font-style")),
        Integer.decode(Options.getInstance().getValue("font-size")));
    content.setFont(font);

    var vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(books), new JScrollPane(sections));
    var horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vertical, new JScrollPane(content));

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
    create.addActionListener(e -> create());
    file.add(create);

    var open = new JMenuItem(bundle.getString("open"));
    open.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
    open.addActionListener(e -> open());
    file.add(open);

    var save = new JMenuItem(bundle.getString("save"));
    save.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
    save.addActionListener(e -> save());
    file.add(save);

    var saveAs = new JMenuItem(bundle.getString("saveAs"));
    saveAs.addActionListener(e -> saveAs());
    file.add(saveAs);

    file.addSeparator();

    var exportText = new JMenuItem(bundle.getString("exportText"));
    exportText.addActionListener(e -> exportText());
    file.add(exportText);

    file.addSeparator();

    var close = new JMenuItem(bundle.getString("close"));
    close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
    close.addActionListener(s -> close());
    file.add(close);

    //  Options menu
    var options = new JMenu(bundle.getString("options"));
    options.setMnemonic('O');
    menu.add(options);

    var fontOption = new JMenuItem(bundle.getString("fontOptions"));
    fontOption.addActionListener(e -> JOptionPane.showMessageDialog(null, "Not implemented."));
    options.add(fontOption);

    //  Help menu
    var help = new JMenu(bundle.getString("help"));
    help.setMnemonic('H');
    menu.add(help);

    var helpI = new JMenuItem(bundle.getString("help"));
    helpI.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_DOWN_MASK));
    help.add(helpI);
    helpI.addActionListener(e -> showHelp());
    help.addSeparator();

    var license = new JMenuItem(bundle.getString("license"));
    license.addActionListener(e -> new LicenseWindow(this));
    help.add(license);

    var about = new JMenuItem(bundle.getString("about"));
    about.addActionListener(e -> new AboutWindow(this));
    help.add(about);

    return menu;
  }

  private JToolBar buildToolBar() {
    var toolBar = new JToolBar(bundle.getString("typewriter") + "| " + bundle.getString("toolbar"));

    var newBook = new JButton(new ImageIcon(getClass().getResource("/ico/addBook.png")));
    newBook.setToolTipText(bundle.getString("newBook"));
    newBook.addActionListener(e -> addBook());
    toolBar.add(newBook);

    var editBook = new JButton(new ImageIcon(getClass().getResource("/ico/editBook.png")));
    editBook.setToolTipText(bundle.getString("editBook"));
    editBook.addActionListener(e -> editBook());
    toolBar.add(editBook);

    var deleteBook = new JButton(new ImageIcon(getClass().getResource("/ico/deleteBook.png")));
    deleteBook.setToolTipText(bundle.getString("deleteBook"));
    deleteBook.addActionListener(e -> removeBook());
    toolBar.add(deleteBook);

    toolBar.addSeparator();

    var addChapter = new JButton(new ImageIcon(getClass().getResource("/ico/addChapter.png")));
    addChapter.setToolTipText(bundle.getString("newChapter"));
    addChapter.addActionListener(e -> addChapter());
    toolBar.add(addChapter);

    var addPoem = new JButton(new ImageIcon(getClass().getResource("/ico/addPoem.png")));
    addPoem.setToolTipText(bundle.getString("newPoem"));
    addPoem.addActionListener(e -> addPoem());
    toolBar.add(addPoem);

    var editSection = new JButton(new ImageIcon(getClass().getResource("/ico/editSection.png")));
    editSection.setToolTipText(bundle.getString("editSection"));
    editSection.addActionListener(e -> editSection());
    toolBar.add(editSection);

    var deleteSection =
        new JButton(new ImageIcon(getClass().getResource("/ico/deleteSection.png")));
    deleteSection.setToolTipText(bundle.getString("deleteSection"));
    deleteSection.addActionListener(e -> removeSection());
    toolBar.add(deleteSection);

    var moveUp = new JButton(new ImageIcon(getClass().getResource("/ico/moveUp.png")));
    moveUp.setToolTipText(bundle.getString("moveUp"));
    moveUp.addActionListener(e -> moveUp());
    toolBar.add(moveUp);

    var moveDown = new JButton(new ImageIcon(getClass().getResource("/ico/moveDown.png")));
    moveDown.setToolTipText(bundle.getString("moveDown"));
    moveDown.addActionListener(e -> moveDown());
    toolBar.add(moveDown);

    toolBar.addSeparator();

    var exportText = new JButton(new ImageIcon(getClass().getResource("/ico/exportText.png")));
    exportText.setToolTipText(bundle.getString("exportSelectedBookAsText"));
    exportText.addActionListener(e -> exportText());
    toolBar.add(exportText);

    return toolBar;
  }

  private void close() {
    if (Data.getInstance().hasUnsavedChanges()) {
      int option =
          JOptionPane.showOptionDialog(
              this,
              bundle.getString("unsavedChanges"),
              bundle.getString("quitQuestion"),
              JOptionPane.YES_NO_CANCEL_OPTION,
              JOptionPane.WARNING_MESSAGE,
              null,
              new String[] {
                bundle.getString("yes"), bundle.getString("no"), bundle.getString("cancel")
              },
              0);

      switch (option) {
        case 0:
          save();
        case 1:
          System.exit(0);
          break;
        default:
          return;
      }
    }
    System.exit(0);
  }

  private void addBook() {
    String name = JOptionPane.showInputDialog(bundle.getString("enterBookName"));

    if (name == null) {
      return;
    } else if (name.length() == 0) {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("shortBookName"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }

    var book = new Book(name, System.getProperty("user.name"));

    if (!Data.getInstance().hasBook(book)) {
      Data.getInstance().addBook(book);
    } else {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("existingBook"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void removeBook() {
    if (books.getSelectedIndex() >= 0) {
      int option =
          JOptionPane.showOptionDialog(
              this,
              bundle.getString("reallyDeleteBook"),
              bundle.getString("areYouSure"),
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.WARNING_MESSAGE,
              null,
              new String[] {bundle.getString("yes"), bundle.getString("no")},
              1);

      if (option == 0) {
        Data.getInstance().removeBook(books.getSelectedValue());
      }
    } else {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("noBookSelected"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void editBook() {
    if (books.getSelectedIndex() >= 0) {
      new EditBook(books.getSelectedValue(), this);
    } else {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("noBookSelected"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void addChapter() {
    if (books.getSelectedIndex() >= 0) {
      String name = JOptionPane.showInputDialog(bundle.getString("enterChapterName"));

      if (name == null) {
        return;
      } else if (name.length() == 0) {
        JOptionPane.showMessageDialog(
            this,
            bundle.getString("shortChapterName"),
            bundle.getString("error"),
            JOptionPane.ERROR_MESSAGE);
      }

      var chapter = new Chapter(name);

      if (!books.getSelectedValue().hasSection(chapter)) {
        books.getSelectedValue().addSection(chapter);
      } else {
        JOptionPane.showMessageDialog(
            this,
            bundle.getString("existingChapterOrPoem"),
            bundle.getString("error"),
            JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("noBookSelected"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void addPoem() {
    if (books.getSelectedIndex() >= 0) {
      String name = JOptionPane.showInputDialog(bundle.getString("enterPoemName"));

      if (name == null) {
        return;
      } else if (name.length() == 0) {
        JOptionPane.showMessageDialog(
            this,
            bundle.getString("shortPoemName"),
            bundle.getString("error"),
            JOptionPane.ERROR_MESSAGE);
      }

      var poem = new Poem(name);

      if (!books.getSelectedValue().hasSection(poem)) {
        books.getSelectedValue().addSection(poem);
      } else {
        JOptionPane.showMessageDialog(
            this,
            bundle.getString("existingChapterOrPoem"),
            bundle.getString("error"),
            JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("noBookSelected"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void removeSection() {
    if (sections.getSelectedIndex() >= 0) {
      int option =
          JOptionPane.showOptionDialog(
              this,
              bundle.getString("reallyDeleteSection"),
              bundle.getString("areYouSure"),
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.WARNING_MESSAGE,
              null,
              new String[] {bundle.getString("yes"), bundle.getString("no")},
              1);

      if (option == 0) {
        books.getSelectedValue().removeSection(sections.getSelectedValue());
      }
    } else {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("noSectionSelected"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void editSection() {
    if (sections.getSelectedIndex() >= 0) {
      new EditSection(sections.getSelectedValue(), this);
    } else {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("noSectionSelected"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void moveUp() {
    if (sections.getSelectedIndex() > 0) {
      int index = sections.getSelectedIndex();
      var temp = books.getSelectedValue().getSections().get(index);
      books
          .getSelectedValue()
          .getSections()
          .set(index, books.getSelectedValue().getSections().get(index - 1));
      books.getSelectedValue().getSections().set(index - 1, temp);
      sections.setSelectedIndex(index - 1);
    } else if (sections.getSelectedIndex() == 0) {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("firstCannotBeMoved"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("noSectionSelected"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void moveDown() {
    if (sections.getSelectedIndex() < sections.getModel().getSize() - 1
        && sections.getSelectedIndex() >= 0) {
      int index = sections.getSelectedIndex();
      var temp = books.getSelectedValue().getSections().get(index);
      books
          .getSelectedValue()
          .getSections()
          .set(index, books.getSelectedValue().getSections().get(index + 1));
      books.getSelectedValue().getSections().set(index + 1, temp);
      sections.setSelectedIndex(index + 1);
    } else if (sections.getSelectedIndex() == sections.getModel().getSize() - 1) {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("lastCannotBeMoved"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("noSectionSelected"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void bookSelectionChanged() {
    if (books.getSelectedIndex() >= 0) {
      sections.setModel(books.getSelectedValue().getSections());
      sections.setEnabled(true);
    } else {
      sections.setModel(new DefaultListModel<>());
      sections.setEnabled(false);
    }
  }

  private void sectionSelectionChanged() {
    if (sections.getSelectedIndex() >= 0) {
      content.setText(sections.getSelectedValue().getContent());
      content.setEnabled(true);
    } else {
      content.setText("");
      content.setEnabled(false);
    }
  }

  private void textChanged() {
    if (sections.getSelectedIndex() >= 0) {
      sections.getSelectedValue().setContent(content.getText());
    }
  }

  private void save() {
    try {
      Data.getInstance().save();
      if (getTitle().endsWith("*")) {
        setTitle(getTitle().substring(0, getTitle().length() - 2));
      }
    } catch (NoFileSpecifiedException ex) {
      saveAs();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("wrongSavingFile") + "\n" + ex.getMessage(),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void saveAs() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(
        new FileNameExtensionFilter("Typewriter library file (*.tpw)", "tpw"));
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
      try {
        var file = fileChooser.getSelectedFile();
        if (!file.getPath().endsWith(".tpw")) {
          file = new File(file + ".tpw");
        }
        Data.getInstance().saveAs(file);
        if (getTitle().endsWith("*")) {
          setTitle(getTitle().substring(0, getTitle().length() - 2));
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(
            this,
            bundle.getString("wrongSavingFile") + "\n" + ex.getMessage(),
            bundle.getString("error"),
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void create() {
    if (Data.getInstance().hasUnsavedChanges()) {
      int option =
          JOptionPane.showOptionDialog(
              this,
              bundle.getString("unsavedChanges"),
              bundle.getString("quitQuestion"),
              JOptionPane.YES_NO_CANCEL_OPTION,
              JOptionPane.WARNING_MESSAGE,
              null,
              new String[] {
                bundle.getString("yes"), bundle.getString("no"), bundle.getString("cancel")
              },
              0);

      switch (option) {
        case 0:
          save();
        case 1:
          break;
        default:
          return;
      }
    }
    Data.getInstance().clear();
  }

  private void open() {
    if (Data.getInstance().hasUnsavedChanges()) {
      int option =
          JOptionPane.showOptionDialog(
              this,
              bundle.getString("unsavedChanges"),
              bundle.getString("quitQuestion"),
              JOptionPane.YES_NO_CANCEL_OPTION,
              JOptionPane.WARNING_MESSAGE,
              null,
              new String[] {
                bundle.getString("yes"), bundle.getString("no"), bundle.getString("cancel")
              },
              0);

      switch (option) {
        case 0:
          save();
        case 1:
          break;
        default:
          return;
      }
    }
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(
        new FileNameExtensionFilter("Typewriter library file (*.tpw)", "tpw"));
    int optionSave = fileChooser.showOpenDialog(this);
    if (optionSave == JFileChooser.APPROVE_OPTION) {
      try {
        Data.getInstance().open(fileChooser.getSelectedFile());
        if (getTitle().endsWith("*")) {
          setTitle(getTitle().substring(0, getTitle().length() - 2));
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(
            this,
            bundle.getString("wrongOpeningFile") + "\n" + ex.getMessage(),
            bundle.getString("error"),
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void exportText() {
    if (books.getSelectedIndex() < 0) {
      JOptionPane.showMessageDialog(
          this,
          bundle.getString("noBookSelected"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("Plain text file (*.txt)", "txt"));
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
      try {
        var file = fileChooser.getSelectedFile();
        if (!file.getPath().endsWith(".txt")) {
          file = new File(file + ".txt");
        }
        TextExporter exporter = ExporterFactory.getNewTextExporter();
        exporter.exportToFile(books.getSelectedValue(), file);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(
            this,
            bundle.getString("wrongSavingFile") + "\n" + ex.getMessage(),
            bundle.getString("error"),
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void showHelp() {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
      try {
        File tmp = File.createTempFile("TYPEWRITER", ".html");
        tmp.deleteOnExit();
        Files.copy(
            getClass().getResourceAsStream("/text/help.html"),
            tmp.toPath(),
            StandardCopyOption.REPLACE_EXISTING);
        desktop.browse(tmp.toURI());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void dataChanged(DataChangeEvent event) {
    books.updateUI();
    sections.updateUI();

    if (!getTitle().endsWith("*")) {
      setTitle(getTitle() + " *");
    }
  }

  @Override
  public void fileChanged(File file) {
    if (file == null) {
      setTitle(bundle.getString("typewriter"));
    } else {
      setTitle(bundle.getString("typewriter") + " - " + file.getName());
    }
  }
}
