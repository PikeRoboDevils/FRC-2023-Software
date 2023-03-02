/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import static org.pikerobodevils.frc2023.Constants.IntakeConstants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  DoubleSolenoid intakeCylinders =
      new DoubleSolenoid(PneumaticsModuleType.REVPH, FORWARD_CHANNEL, REVERSE_CHANNEL);

  public Intake() {}

  public void setOpen() {
    intakeCylinders.set(DoubleSolenoid.Value.kForward);
  }

  public void setClose() {
    intakeCylinders.set(DoubleSolenoid.Value.kReverse);
  }
}
