# Typewriter

A simple program for writing novels, poems and books.

## Author

My name is Roman Janků and currently I am a student at Faculty of Electrical Engineering at
Czech Technical University in Prague. In my free time I write mostly poems, but some novels as 
well. However, I wasn't happy with what other novel writing software has to offer, so I created 
my own. Then I decided to publish since other people might find it useful for their writing.

## License

This program is licensed under GNU General Public License v3. More information on 
[https://www.gnu.org/licenses/gpl-3.0.html](https://www.gnu.org/licenses/gpl-3.0.html).

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

## How to run this program

1. You need to install Java Runtime Environment version 11 or newer.

2. You need to add `java/bin` path to your `PATH` variable.

3. Download the JAR archive with application (available in releases on GitHub) or build your JAR
   from sources (also available on GitHub).

4. Use command `java -jar -[NAME_OF_THE_JAR_FILE]` to run the application packaged in JAR archive.
   Replace `[NAME_OF_THE_JAR_FILE]` with the name of the JAR file with the application.

## Logging and options

The application outputs its logs to file named ``logs`` placed in the ``.tpw`` directory placed in 
the home directory of the user running the application. Logs are done in standard Java logging format

    úno 04, 2022 4:00:53 ODP. cz.uhl1k.typewriter.Logging initiate 
    INFO: Initiated logger.

The left part of the first line contains date and time of the logging event. The right half contains
information from where the logging was done. The second line is logging message. There can be a 
stack trace appended if the logging was done because of an exception.

File named ``options`` is stored at the same location as ``logs`` file and it contains options
for the program. It looks like this:

    maximized:	no
    font-size:	15
    font-name:	Times New Roman
    font-style:	0

* ``maximized`` says weather the main window should be maximized. Two values are allowed ``yes`` 
  and ``no``.
* ``font-size`` defines the size of the font used in the poem and chapter editing area. It is in 
  pixels and positive integer is allowed.
* ``font-name`` name of the font used in the poem and chapter editing area. Font with this name 
  must be present in the system.
* ``font-style`` defines the style of the font. Should be ``0``.
 
## Technologies I used

- Java 11 OpenJDK
- IntelliJ Idea
- Gimp

For code formatting I use [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).