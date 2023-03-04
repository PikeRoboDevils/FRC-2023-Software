/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.pikerobodevils.frc2023.subsystems.Drivetrain;

public final class Autos {
  Drivetrain drivetrain;
  Superstructure superstructure;

  public Autos(Drivetrain drivetrain, Superstructure superstructure) {
    this.drivetrain = drivetrain;
    this.superstructure = superstructure;
  }

  public CommandBase driveBackAuto() {
    return drivetrain
        .run(
            () -> {
              drivetrain.setLeftRightVoltage(-3, -3);
            })
        .withTimeout(4);
  }

  public CommandBase scoreMidCubeDriveBack() {
    return scoreMidCube().andThen(driveBackAuto());
  }

  public CommandBase scoreMidCube() {
    return superstructure
        .scoreMidPosition()
        .andThen(superstructure.score())
        .andThen(superstructure.stowCommand());
  }
}
