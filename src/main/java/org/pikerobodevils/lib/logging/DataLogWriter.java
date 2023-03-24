/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.lib.logging;

import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import java.util.Map;
import org.tinylog.core.LogEntry;
import org.tinylog.writers.AbstractFormatPatternWriter;

public class DataLogWriter extends AbstractFormatPatternWriter {

  StringLogEntry m_log;

  public DataLogWriter(Map<String, String> properties) {
    super(properties);
    String entryKey = properties.get("entryKey");
    if (entryKey == null || entryKey.isBlank()) {
      entryKey = "tinylog";
    }
    m_log = new StringLogEntry(DataLogManager.getLog(), entryKey);
  }

  @Override
  public void write(LogEntry logEntry) throws Exception {
    m_log.append(render(logEntry));
  }

  @Override
  public void flush() throws Exception {}

  @Override
  public void close() throws Exception {}
}
