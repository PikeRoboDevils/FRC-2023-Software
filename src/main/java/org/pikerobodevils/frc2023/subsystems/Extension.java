/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import static org.pikerobodevils.frc2023.Constants.ExtensionConstants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Extension extends SubsystemBase {
  private final DoubleSolenoid upper =
      new DoubleSolenoid(PneumaticsModuleType.REVPH, UPPER_FORWARD, UPPER_REVERSE);
  private final DoubleSolenoid lower =
      new DoubleSolenoid(PneumaticsModuleType.REVPH, LOWER_FORWARD, LOWER_REVERSE);

  public Extension() {}

  public void extend() {
    upper.set(DoubleSolenoid.Value.kForward);
    lower.set(DoubleSolenoid.Value.kForward);
  }

  public void retract() {
    upper.set(DoubleSolenoid.Value.kReverse);
    lower.set(DoubleSolenoid.Value.kReverse);
  }


}
