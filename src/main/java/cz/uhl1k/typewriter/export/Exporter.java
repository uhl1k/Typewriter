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

package cz.uhl1k.typewriter.export;

import cz.uhl1k.typewriter.model.Book;
import java.io.File;

/**
 * Abstract exporter class that defines methods in exporters.
 */
public abstract class Exporter {
  /**
   * Export given book to a given file.
   * @param book Book to export.
   * @param file File to export to.
   */
  public abstract void exportToFile(Book book, File file);
}
