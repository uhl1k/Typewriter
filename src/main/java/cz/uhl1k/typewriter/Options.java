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

import cz.uhl1k.typewriter.exceptions.SettingsNotSavedException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for handling options and settings of the application. This class is singleton.
 */
public class Options {

  private static final Options INSTANCE = new Options();

  private Options() {
    settings = new HashMap<>();
  }

  /**
   * Returns the only instance of this class.
   * @return The only instance.
   */
  public static Options getInstance() {
    return INSTANCE;
  }

  /**
   * Tells weather the settings were successfully loaded.
   * @return
   */
  public static boolean isInitialised() {
    return INSTANCE != null;
  }

  private Map<String, String> settings;

  /**
   * Gets value from settings for a given key or null if the key is not in settings.
   * @param key Key to look for.
   * @return Value for given key or null.
   */
  public String getValue(String key) {
    return settings.get(key);
  }

  /**
   * Sets new key-value pair in settings. If there is the key already, the value is overwritten with new value.
   * @param key Key of the setting.
   * @param value Value of the settings.
   * @throws SettingsNotSavedException Thrown when error occurred while saving settings.
   */
  public void setValue(String key, String value) throws SettingsNotSavedException {
    settings.put(key, value);
    save();
  }

  private void save() throws SettingsNotSavedException {
    throw new SettingsNotSavedException();
  }
}
