/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import io.github.oblarg.oblog.Logger;
import org.pikerobodevils.frc2023.commands.Autos;
import org.pikerobodevils.frc2023.simulation.ArmSim;
import org.pikerobodevils.frc2023.subsystems.*;

public class RobotContainer {
  public final Drivetrain drivetrain = new Drivetrain();
  public final Arm arm = new Arm();

  public final Intake intake = new Intake();

  public final Extension extension = new Extension();
  public final ControlBoard controlboard = new ControlBoard();

  private final Superstructure superstructure = new Superstructure(arm, intake, extension);
  private final ArmSim armSim = new ArmSim(arm);

  private final Pneumatics pneumatics = new Pneumatics();

  Autos autos = new Autos(drivetrain, superstructure);
  private final ShuffleboardTab driverTab = Shuffleboard.getTab("Driver Dashboard");
  SendableChooser<CommandBase> autoChooser = new SendableChooser<>();

  public RobotContainer() {
    /**
     * arm.setDefaultCommand(arm.run(() -> { arm.setVoltage(-controlboard.operator.getLeftY() * 6);
     * }));
     */
    drivetrain.setDefaultCommand(
        drivetrain
            .arcadeDriveCommand(controlboard::getSpeed, controlboard::getTurn)
            .withName("Default Drive Command"));

    /*arm.setDefaultCommand(
    arm.run(() -> {
      arm.setVoltage(12 * controlboard.operator.getLeftX());
    }));*/
    configureBindings();
    Logger.configureLoggingAndConfig(this, false);

    autoChooser.setDefaultOption("No auto", Commands.none());
    autoChooser.addOption("Drive Back", autos.driveBackAuto());
    autoChooser.addOption("Score low cube only", autos.scoreLowCube());
    autoChooser.addOption("Score mid cube only", autos.scoreMidCube());
    autoChooser.addOption("Score high cube only", autos.scoreHighCube());
    autoChooser.addOption("Low cube drive back", autos.scoreLowCubeDriveBack());
    autoChooser.addOption("Mid cube drive back", autos.scoreMidCubeDriveBack());
    autoChooser.addOption("High cube drive back", autos.scoreHighCubeDriveBack());
    autoChooser.addOption("Low cube balance", autos.scoreLowThenBalance());
    autoChooser.addOption("Mid cube balance", autos.scoreMidThenBalance());
    autoChooser.addOption("High cube balance", autos.scoreHighThenBalance());
    autoChooser.addOption("Auto Balance Forwards", autos.autoBalanceForwards());
    autoChooser.addOption("Auto Balance Backwards", autos.autoBalanceBackwards());

    driverTab.add("Auto", autoChooser).withSize(2, 1);
  }

  private void configureBindings() {
    controlboard
        .operator
        .x()
        .onTrue(Commands.runOnce(() -> superstructure.setGamePiece(Superstructure.GamePiece.Cube)));
    controlboard
        .operator
        .y()
        .onTrue(Commands.runOnce(() -> superstructure.setGamePiece(Superstructure.GamePiece.Cone)));

    controlboard.operator.leftTrigger().whileTrue(superstructure.runIntake());

    controlboard.operator.rightTrigger().onTrue(superstructure.score());

    controlboard.operator.b().onTrue(superstructure.intakeSubstationPosition());
    controlboard
        .operator
        .axisGreaterThan(XboxController.Axis.kLeftY.value, .5)
        .onTrue(superstructure.floorPickupCube());
    controlboard
        .operator
        .axisLessThan(XboxController.Axis.kLeftY.value, -.5)
        .onTrue(superstructure.setStateCommand(Superstructure.SuperstructureState.CUBE_SHOOT));

    controlboard.operator.a().onTrue(superstructure.stowCommand());

    controlboard.operator.pov(0).onTrue(superstructure.scoreHighPosition());
    controlboard.operator.pov(90).onTrue(superstructure.scoreMidPosition());
    controlboard.operator.pov(180).onTrue(superstructure.scoreLowPosition());

    controlboard
        .driver
        .a()
        .toggleOnTrue(
            Commands.startEnd(
                    () -> {
                      drivetrain.setIdleMode(CANSparkMax.IdleMode.kBrake);
                      superstructure.setBrakeDisplay(true);
                    },
                    () -> {
                      drivetrain.setIdleMode(CANSparkMax.IdleMode.kCoast);
                      superstructure.setBrakeDisplay(false);
                    })
                .ignoringDisable(true));

    /*controlboard.operator.x().onTrue(arm.setGoalCommand(Arm.ArmPosition.FLOOR_PICKUP.valueRadians));
    controlboard.operator.y().onTrue(arm.setGoalCommand(Arm.ArmPosition.SCORE.valueRadians));

    controlboard.operator.leftBumper().onTrue(extension.runOnce(extension::extend));
    controlboard.operator.rightBumper().onTrue(extension.runOnce(extension::retract));*/

    /*
     * controlboard.operator.x().onTrue(intake.runOnce(intake::setOpen));
     * controlboard.operator.y().onTrue(intake.runOnce(intake::setClose));*
     */
  }

  public void simulationPeriodic() {
    armSim.update();
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
