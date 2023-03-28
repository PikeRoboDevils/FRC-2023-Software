/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.lib.logging;

import edu.wpi.first.hal.DriverStationJNI;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.tinylog.Level;
import org.tinylog.core.ConfigurationParser;
import org.tinylog.core.LogEntry;
import org.tinylog.core.LogEntryValue;
import org.tinylog.provider.InternalLogger;
import org.tinylog.writers.AbstractFormatPatternWriter;

public class DsConsoleWriter extends AbstractFormatPatternWriter {

  private final Level errorLevel;

  public DsConsoleWriter() {
    this(Collections.<String, String>emptyMap());
  }

  /**
   * @param properties Configuration for writer
   */
  public DsConsoleWriter(final Map<String, String> properties) {
    super(properties);

    // Set the default level for stderr logging
    Level levelStream = Level.WARN;

    // Check stream property
    String stream = getStringValue("stream");
    if (stream != null) {
      // Check whether we have the err@LEVEL syntax
      String[] streams = stream.split("@", 2);
      if (streams.length == 2) {
        levelStream = ConfigurationParser.parse(streams[1], levelStream);
        if (!streams[0].equals("err")) {
          InternalLogger.log(
              Level.ERROR,
              "Stream with level must be \"err\", \"" + streams[0] + "\" is an invalid name");
        }
        stream = null;
      }
    }

    if (stream == null) {
      errorLevel = levelStream;
    } else if ("err".equalsIgnoreCase(stream)) {
      errorLevel = Level.TRACE;
    } else if ("out".equalsIgnoreCase(stream)) {
      errorLevel = Level.OFF;
    } else {
      InternalLogger.log(
          Level.ERROR,
          "Stream must be \"out\" or \"err\", \"" + stream + "\" is an invalid stream name");
      errorLevel = levelStream;
    }
  }

  @Override
  public Collection<LogEntryValue> getRequiredLogEntryValues() {
    Collection<LogEntryValue> logEntryValues = super.getRequiredLogEntryValues();
    logEntryValues.add(LogEntryValue.LEVEL);
    return logEntryValues;
  }

  @Override
  public void write(final LogEntry logEntry) {
    if (logEntry.getLevel().ordinal() < errorLevel.ordinal()) {
      System.out.print(render(logEntry));
    } else {
      DriverStationJNI.sendError(
          logEntry.getLevel().equals(Level.ERROR), 1, false, render(logEntry), "", "", true);
    }
  }

  @Override
  public void flush() {}

  @Override
  public void close() {}
}
