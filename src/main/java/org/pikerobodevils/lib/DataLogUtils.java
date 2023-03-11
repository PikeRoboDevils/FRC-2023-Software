/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.lib;

import static edu.wpi.first.wpilibj.DataLogManager.log;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.util.WPILibVersion;

public class DataLogUtils {
  public static final String METADATA_PATH = "/RealMetadata/";

  public static void logMetadataEntry(String key, Object value) {
    var entry = new StringLogEntry(DataLogManager.getLog(), key);
    entry.append(value.toString());
    entry.finish();
    DataLogManager.log(String.format("%-25s%s", key + ":", value));
  }

  public static void logManifestMetadata(Object object) {
    log("Build debug info:");
    Util.getManifestAttributesForClass(object)
        .forEach(
            (key, value) -> {
              DataLogUtils.logMetadataEntry(key.toString(), value.toString());
            });
  }

  public static void logSoftwareVersionMetadata() {
    log("Software Versions:");
    logMetadataEntry(
        "Java Version",
        System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
    logMetadataEntry("WPILib Version", WPILibVersion.Version);
    logMetadataEntry("RevLib Version", CANSparkMax.kAPIVersion);
  }
}
