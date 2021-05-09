package cz.uhl1k.typewriter.gui;

import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;

public class AboutWindow extends JDialog {

  ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  public AboutWindow() {
    super();

    buildGui();
    pack();

    setModal(true);
    setResizable(false);
    setTitle(bundle.getString("aboutTypewriter"));
    setVisible(true);
  }

  private void buildGui() {
    JButton close = new JButton(bundle.getString("close"));
    close.addActionListener(e -> dispose());
    add(close);
  }
}
