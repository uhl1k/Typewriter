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

package cz.uhl1k.typewriter.model;

/**
 * Source for data change events.
 */
public interface DataChangeSource {
  /**
   * Registers a new listener for data changes.
   * @param listener Listener to register.
   */
  void registerListener(DataChangeListener listener);

  /**
   * Removes listener for data changes.
   * @param listener Listener to remove.
   */
  void unregisterListener(DataChangeListener listener);
}
