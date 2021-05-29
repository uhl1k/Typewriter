package cz.uhl1k.typewriter.gui;

import cz.uhl1k.typewriter.Typewriter;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AboutWindow extends JDialog {

  ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  public AboutWindow(JFrame parent) {
    super();

    buildGui();
    //pack();

    setLocationRelativeTo(parent);
    setMinimumSize(new Dimension(300, 250));
    setModal(true);
    setResizable(false);
    setTitle(bundle.getString("aboutTypewriter"));
    setVisible(true);
  }

  private void buildGui() {
    setLayout(new GridLayout(7, 1));
    ((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

    add(new JLabel(new ImageIcon(getClass().getResource("/icon.png"))));

    add(new JLabel(bundle.getString("typewriter"), SwingConstants.CENTER));
    add(new JLabel(Typewriter.version, SwingConstants.CENTER));
    add(new JLabel(bundle.getString("uhl1k"), SwingConstants.CENTER));
    add(new JLabel(bundle.getString("repoUrl"), SwingConstants.CENTER));
    add(new JLabel(bundle.getString("gnugpl"), SwingConstants.CENTER));
    add(new JLabel(bundle.getString("licenseUrl"), SwingConstants.CENTER));
  }
}
