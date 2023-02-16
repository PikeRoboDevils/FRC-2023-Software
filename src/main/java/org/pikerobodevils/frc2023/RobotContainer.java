/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import static edu.wpi.first.wpilibj2.command.Commands.print;

import edu.wpi.first.wpilibj2.command.Command;
import org.pikerobodevils.frc2023.subsystems.Drivetrain;

public class RobotContainer {
  Drivetrain drivetrain = new Drivetrain();
  ControlBoard controlboard = new ControlBoard();

  public RobotContainer() {

    drivetrain.setDefaultCommand(
        drivetrain.arcadeDriveCommand(controlboard::getSpeed, controlboard::getTurn));
    configureBindings();
  }

  private void configureBindings() {}

  public Command getAutonomousCommand() {
    return print("No autonomous command configured");
  }
}
