/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import static edu.wpi.first.wpilibj2.command.Commands.print;

import edu.wpi.first.wpilibj2.command.Command;
import io.github.oblarg.oblog.Logger;
import org.pikerobodevils.frc2023.simulation.ArmSim;
import org.pikerobodevils.frc2023.subsystems.*;

public class RobotContainer {
  public final Drivetrain drivetrain = new Drivetrain();
  public final Arm arm = new Arm();

  public final Intake intake = new Intake();

  public final Extension extension = new Extension();
  public final ControlBoard controlboard = new ControlBoard();
  private final ArmSim armSim = new ArmSim(arm);

  private final Pneumatics pneumatics = new Pneumatics();

  public RobotContainer() {
    /**
     * arm.setDefaultCommand(arm.run(() -> { arm.setVoltage(-controlboard.operator.getLeftY() * 6);
     * }));
     */
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
    controlboard.operator.a().onTrue(arm.setGoalCommand(Arm.ArmPosition.STOW.valueRadians));
    controlboard.operator.b().onTrue(arm.setGoalCommand(Arm.ArmPosition.PICKUP.valueRadians));
    controlboard.operator.x().onTrue(arm.setGoalCommand(Arm.ArmPosition.FLOOR_PICKUP.valueRadians));
    controlboard.operator.y().onTrue(arm.setGoalCommand(Arm.ArmPosition.SCORE.valueRadians));

    controlboard.operator.leftBumper().onTrue(extension.runOnce(extension::extend));
    controlboard.operator.rightBumper().onTrue(extension.runOnce(extension::retract));

    /**
     * controlboard.operator.x().onTrue(intake.runOnce(intake::setOpen));
     * controlboard.operator.y().onTrue(intake.runOnce(intake::setClose));*
     */
  }

  public void simulationPeriodic() {
    armSim.update();
  }

  public Command getAutonomousCommand() {
    return print("No autonomous command configured");
  }
}
