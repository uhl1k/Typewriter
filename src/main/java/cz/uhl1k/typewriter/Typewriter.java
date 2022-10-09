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

package cz.uhl1k.typewriter;

import cz.uhl1k.typewriter.gui.MainWindow;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/** Main class of the typewriter application. */
public class Typewriter {

  /** Version of the application */
  public static final String version = "0.8 ALPHA";
  private static final ResourceBundle bundle = ResourceBundle.getBundle("translations/bundle");

  /**
   * Main method of this application.
   *
   * @param args Arguments from command line.
   */
  public static void main(String... args) {

    Logging.log("Typewriter is starting.", Level.INFO);

    File fileToOpen = null;

    if (args.length > 0) {
      for (String arg : args) {
        switch (arg.toLowerCase()) {
          case "-h":
            printHelp();
            Logging.log("Printed short help.", Level.INFO);
            return;

          case "-l":
            printLicenseLong();
            Logging.log("Printed licensing information.", Level.INFO);
            return;

          default:
            try {
              fileToOpen = new File(arg);
              Logging.log("Opening file on start-up: " + fileToOpen.getAbsolutePath(), Level.INFO);
              if (!fileToOpen.exists()) {
                fileToOpen = null;
                Logging.log("Could not open file on startup! File does not exist.", Level.INFO);
                throw new IOException("No such file exists!");
              }
            } catch (IOException ex) {
              JOptionPane.showMessageDialog(null, bundle.getString("fileError"), bundle.getString("error"), JOptionPane.ERROR_MESSAGE);
              Logging.log("Could not open file on startup! CAUSE: " + ex.getMessage(), Level.INFO, ex.getCause().getStackTrace());
            }
        }
      }
    }
    printLicenseShort();
    new MainWindow(fileToOpen);
  }

  private static void printLicenseShort() {
    System.out.println("Typewriter  Copyright (C) 2021  uhl1k (Roman Janků)");
    System.out.println(
        "This program comes with ABSOLUTELY NO WARRANTY; for details use option -l.");
    System.out.println("This is free software, and you are welcome to redistribute it");
    System.out.println("under certain conditions.");
  }

  private static void printLicenseLong() {
    System.out.println("Typewriter - simple novel and poem writing software");
    System.out.println("Copyright (C) 2021  uhl1k (Roman Janků)");
    System.out.println();
    System.out.println("This program is free software: you can redistribute it and/or modify");
    System.out.println("it under the terms of the GNU General Public License as published by");
    System.out.println("the Free Software Foundation, either version 3 of the License, or");
    System.out.println("(at your option) any later version.");
    System.out.println();
    System.out.println("This program is distributed in the hope that it will be useful,");
    System.out.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
    System.out.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
    System.out.println("GNU General Public License for more details.");
    System.out.println();
    System.out.println("You should have received a copy of the GNU General Public License");
    System.out.println("along with this program.  If not, see <https://www.gnu.org/licenses/>.");
  }

  private static void printHelp() {
    System.out.println("Typewriter is a simple program for writing poems and novels.");
    System.out.println("This program requires GUI (Graphical User Interface) to run!");
    System.out.println();
    System.out.println("Run:");
    System.out.println("\tjava -jar typewriter.jar [OPTION] [FILENAME]");
    System.out.println();
    System.out.println("FILENAME:\tName of the file to open. Must be typewriter (.tpw) file.");
    System.out.println();
    System.out.println("Options:");
    System.out.println("\t-h\tShows this short help.");
    System.out.println();
    System.out.println("\t-l\tShow licensing information.");
    System.out.println();
    printLicenseShort();
  }
}
