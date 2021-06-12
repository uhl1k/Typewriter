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

/** Main class of the typewriter application. */
public class Typewriter {

  /** Version of the application */
  public static final String version = "0.8 ALPHA";

  /**
   * Main method of this application.
   *
   * @param args Arguments from command line.
   */
  public static void main(String... args) {
    if (args.length > 0) {
      for (String arg : args) {
        switch (arg.toLowerCase()) {
          case "-h":
            printHelp();
            return;

          case "-l":
            printLicenseLong();
            return;

          default:
            // TODO  here will be handling file to open
        }
      }
    }
    printLicenseShort();
    new MainWindow();
  }

  private static void printLicenseShort() {
    System.out.println("Typewriter  Copyright (C) 2021  uhl1k (Roman Janků)");
    System.out.println(
        "This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.");
    System.out.println("This is free software, and you are welcome to redistribute it");
    System.out.println("under certain conditions; type `show c' for details.");
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
