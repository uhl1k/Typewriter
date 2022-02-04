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

import cz.uhl1k.typewriter.Typewriter;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/** Dialog about this application. */
public class AboutWindow extends JDialog {

  ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  /**
   * Opens new about window.
   *
   * @param parent Parent of this dialog.
   */
  public AboutWindow(JFrame parent) {
    super();

    //  call method that builds the entire GUI
    buildGui();

    //  set some basic window parameters
    setLocationRelativeTo(parent);
    setMinimumSize(new Dimension(300, 250));
    setModal(true);
    setResizable(false);
    setTitle(bundle.getString("aboutTypewriter"));
    setVisible(true);
  }

  private void buildGui() {
    //  grid layout with seven rows
    setLayout(new GridLayout(7, 1));
    ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

    //  icon of the program
    add(new JLabel(new ImageIcon(getClass().getResource("/icon.png"))));

    //  name of the program
    JLabel typewriter = new JLabel(bundle.getString("typewriter"), SwingConstants.CENTER);
    typewriter.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
    typewriter.setFont(
        new Font(
            typewriter.getFont().getName(),
            typewriter.getFont().getStyle(),
            typewriter.getFont().getSize() * 2));
    add(typewriter);

    //  version of the program
    JLabel version = new JLabel(Typewriter.version, SwingConstants.CENTER);
    version.setBorder(BorderFactory.createEmptyBorder(1, 0, 5, 0));
    version.setFont(new Font(version.getFont().getName(), Font.PLAIN, version.getFont().getSize()));
    add(version);

    //  author of the program
    JLabel author = new JLabel(bundle.getString("uhl1k"), SwingConstants.CENTER);
    author.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
    add(author);

    //  link to the author of the program
    JLabel authorLink =
        new JLabel(
            "<html><a href=\"\">" + bundle.getString("repoUrl") + "</a></html>",
            SwingConstants.CENTER);
    authorLink.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    authorLink.setFont(
        new Font(authorLink.getFont().getName(), Font.PLAIN, authorLink.getFont().getSize()));
    authorLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
    authorLink.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            try {
              Desktop.getDesktop().browse(new URI(bundle.getString("repoUrl")));
            } catch (URISyntaxException | IOException ex) {
              JOptionPane.showMessageDialog(
                  null,
                  bundle.getString("linkError"),
                  bundle.getString("error"),
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        });
    add(authorLink);

    //  information about license
    JLabel licenseName = new JLabel(bundle.getString("gnugpl"), SwingConstants.CENTER);
    version.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    add(licenseName);

    //  link to the text of the license
    JLabel licenseLink =
        new JLabel(
            "<html><a href=\"\">" + bundle.getString("licenseUrl") + "</a></html>",
            SwingConstants.CENTER);
    licenseLink.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    licenseLink.setFont(
        new Font(licenseLink.getFont().getName(), Font.PLAIN, licenseLink.getFont().getSize()));
    licenseLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
    licenseLink.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            try {
              Desktop.getDesktop().browse(new URI(bundle.getString("licenseUrl")));
            } catch (URISyntaxException | IOException ex) {
              JOptionPane.showMessageDialog(
                  null,
                  bundle.getString("linkError"),
                  bundle.getString("error"),
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        });
    add(licenseLink);
  }
}
