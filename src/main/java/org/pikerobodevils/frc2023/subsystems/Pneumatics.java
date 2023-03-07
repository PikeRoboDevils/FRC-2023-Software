/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

public class Pneumatics implements Loggable {
  // VXC8701
  private final Compressor compressor = new Compressor(PneumaticsModuleType.REVPH);

  public Pneumatics() {
    compressor.enableAnalog(100, 120);
  }

  @Log(name = "Current")
  public double getCurrent() {
    return compressor.getCurrent();
  }

  @Log(name = "Pressure")
  public double getPressure() {
    return compressor.getPressure();
  }
}
