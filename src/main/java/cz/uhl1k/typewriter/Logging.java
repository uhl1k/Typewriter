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

import cz.uhl1k.typewriter.exceptions.CannotLogException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Class handling logging.
 */
public class Logging {

  private static Logger LOGGER = null;

  private static final Object lock = new Object();

  private static void initiate() throws CannotLogException {
    LOGGER = Logger.getLogger("typewriter");
    LOGGER.setUseParentHandlers(false);

    //  create directory for logs file in home directory if it does not exist
    if (!Files.exists(Path.of(System.getProperty("user.home") + File.separator + ".tpw"))) {
      new File(System.getProperty("user.home") + File.separator + ".tpw").mkdir();
    }

    try {
      var fh = new FileHandler(System.getProperty("user.home") + File.separator + ".tpw" + File.separator + "logs");
      fh.setFormatter(new SimpleFormatter());
      LOGGER.addHandler(fh);
      LOGGER.info("Initiated logger.");
    } catch (IOException ex) {
      throw new CannotLogException(ex);
    }
  }

  private static void checkInit() {
    if (LOGGER == null) {
      synchronized (lock) {
        if (LOGGER == null) {
          try {
            initiate();
          } catch (CannotLogException ex) {
            System.out.println("Could not initiate log! Cause: " + ex.getCause().getMessage());
          }
        }
      }
    }
  }

  /**
   * Logs a given message with given level into a log file.
   * @param message Message to log.
   * @param loggingLevel Level of the message.
   */
  public static void log(String message, Level loggingLevel) {
    checkInit();
    LOGGER.log(loggingLevel, message);
  }

  /**
   * Logs a given message with given level into a log file.
   * @param message Message to log.
   * @param loggingLevel Level of the message.
   * @param stackTrace Stack trace array of the exception.
   */
  public static void log(String message, Level loggingLevel, StackTraceElement[] stackTrace) {
    checkInit();
    var sb = new StringBuilder();
    for (StackTraceElement element : stackTrace) {
        sb.append("\n" + element.toString());
    }
    LOGGER.log(loggingLevel, message + sb);
  }
}
