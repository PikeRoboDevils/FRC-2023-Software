/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.lib.vendor;

/*
 * Adapted from 3005's 2022 Code
 * Original source published at https://github.com/FRC3005/Rapid-React-2022-Public/tree/d499655448ed592c85f9cfbbd78336d8841f46e2
 */

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.HashMap;

/** This is a basic monitor class separate from the HealthMonitor setup. */
public class SparkMaxMonitor extends SubsystemBase {
  private HashMap<SparkMax, Short> m_sparkMaxs = new HashMap<>();
  private int m_runCount = 0;

  /** Creates a new SparkMaxMonitor. */
  public SparkMaxMonitor() {}

  public boolean add(SparkMax sparkMax) {
    if (m_sparkMaxs.containsKey(sparkMax)) {
      return false;
    }
    m_sparkMaxs.put(sparkMax, (short) 0);
    return true;
  }

  @Override
  public void periodic() {
    // Run at 1 second
    if (m_runCount++ < 50) {
      return;
    }
    m_runCount = 0;

    m_sparkMaxs.forEach(
        (sparkMax, prevFault) -> {
          short faults = sparkMax.getStickyFaults();
          if (faults != prevFault.shortValue()) {
            /*Logger.tag("Spark Max Monitor")
            .warn(
                "Spark Max ID {} faults: {}",
                sparkMax.getDeviceId(),
                SparkMaxUtils.faultWordToString(faults));*/
          }
          m_sparkMaxs.put(sparkMax, faults);
        });
  }
}
