/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.tinylog.Logger;
import org.tinylog.configuration.Configuration;

public class LogConfig {
  private static final String format =
      "{date:yyyy-MM-dd HH:mm:ss} - [{tag: none}] - {level}: {message}";

  private static final String consoleLevel = "debug";

  private static final String dataLogLevel = "trace";

  private static final String dataLogEntryKey = "messages";

  public static void config() {
    Configuration.set("writer1", "org.pikerobodevils.lib.logging.DataLogWriter");
    Configuration.set("writer1.level", dataLogLevel);
    Configuration.set("writer1.format", format);

    Configuration.set("writer2", "org.pikerobodevils.lib.logging.DsConsoleWriter");
    Configuration.set("writer2.level", consoleLevel);
    Configuration.set("writer2.format", format);
    Configuration.set("writer2.entryKey", dataLogEntryKey);

    CommandScheduler.getInstance()
        .onCommandFinish(
            (cmd) -> Logger.tag("Scheduler").trace("Finish Command: {}", cmd.getName()));
    CommandScheduler.getInstance()
        .onCommandInitialize(
            (cmd) -> Logger.tag("Scheduler").trace("Initialize Command: {}", cmd.getName()));
    CommandScheduler.getInstance()
        .onCommandInterrupt(
            (cmd) -> Logger.tag("Scheduler").trace("Interrupted Command: {}", cmd.getName()));
  }
}
