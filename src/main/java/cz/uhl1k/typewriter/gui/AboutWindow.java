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

/** Dialog about this application. */
public class AboutWindow extends JDialog {

  ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  /**
   * Opens new about window.
   * @param parent Parent of this dialog.
   */
  public AboutWindow(JFrame parent) {
    super();

    buildGui();
    // pack();

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
