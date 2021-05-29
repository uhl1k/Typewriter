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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** Dialog showing license information. */
public class LicenseWindow extends JDialog {

  ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  /**
   * Builds new license window.
   *
   * @param parent Parent JFrame.
   */
  public LicenseWindow(JFrame parent) {
    super();

    buildGui();

    setLocationRelativeTo(parent);
    setMinimumSize(new Dimension(500, 250));
    setModal(true);
    setTitle(bundle.getString("license") + ": " + bundle.getString("gnugpl"));
    setVisible(true);
  }

  private void buildGui() {
    setLayout(new BorderLayout());
    JTextArea text = new JTextArea(loadLicenseText());
    text.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    text.setEditable(false);
    add(new JScrollPane(text), BorderLayout.CENTER);
  }

  private String loadLicenseText() {
    try (InputStreamReader reader =
        new InputStreamReader(getClass().getResourceAsStream("/text/license"))) {
      return new BufferedReader(reader).lines().collect(Collectors.joining("\n"));
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(
          null,
          bundle.getString("licenseNotFound"),
          bundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
      return "";
    }
  }
}
