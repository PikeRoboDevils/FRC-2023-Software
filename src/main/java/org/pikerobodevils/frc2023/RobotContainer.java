/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import static edu.wpi.first.wpilibj2.command.Commands.print;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import io.github.oblarg.oblog.Logger;
import org.pikerobodevils.frc2023.simulation.ArmSim;
import org.pikerobodevils.frc2023.subsystems.Arm;
import org.pikerobodevils.frc2023.subsystems.Drivetrain;

public class RobotContainer {
  public final Drivetrain drivetrain = new Drivetrain();
  public final Arm arm = new Arm();
  public final ControlBoard controlboard = new ControlBoard();

  private final ArmSim armSim = new ArmSim(arm);

  public RobotContainer() {

    drivetrain.setDefaultCommand(
        drivetrain.arcadeDriveCommand(controlboard::getSpeed, controlboard::getTurn));

    /*arm.setDefaultCommand(
    arm.run(() -> {
      arm.setVoltage(12 * controlboard.operator.getLeftX());
    }));*/
    configureBindings();
    Logger.configureLoggingAndConfig(this, false);
  }

  private void configureBindings() {
    controlboard.operator.a().onTrue(arm.setGoalCommand(Units.degreesToRadians(-70)));
    controlboard.operator.b().onTrue(arm.setGoalCommand(Units.degreesToRadians(0)));
    controlboard.operator.x().onTrue(arm.setGoalCommand(Units.degreesToRadians(45)));
    controlboard.operator.y().onTrue(arm.setGoalCommand(Units.degreesToRadians(-15)));
  }

  public void simulationPeriodic() {
    armSim.update();
  }

  public Command getAutonomousCommand() {
    return print("No autonomous command configured");
  }
}
