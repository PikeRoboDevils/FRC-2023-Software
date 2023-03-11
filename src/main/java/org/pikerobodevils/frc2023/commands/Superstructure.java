/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.commands;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.pikerobodevils.frc2023.subsystems.Arm;
import org.pikerobodevils.frc2023.subsystems.Extension;
import org.pikerobodevils.frc2023.subsystems.Intake;

public class Superstructure extends SubsystemBase implements Loggable {
  private boolean brakeDisplay = false;
  AddressableLED led = new AddressableLED(0);
  AddressableLEDBuffer ledBuffer = new AddressableLEDBuffer(200);

  private final Arm arm;
  private final Intake intake;
  private final Extension extension;

  public enum GamePiece {
    Cube,
    Cone
  }

  private GamePiece currentState = GamePiece.Cube;

  public Superstructure(Arm arm, Intake intake, Extension extension) {
    this.arm = arm;
    this.intake = intake;
    this.extension = extension;
    led.setLength(200);
    led.start();
  }

  public void setGamePiece(GamePiece gamePiece) {
    this.currentState = gamePiece;
  }

  public void setCurrentState(GamePiece state) {
    this.currentState = state;
  }

  public GamePiece getCurrentState() {
    return currentState;
  }

  @Log
  public String getCurrentStateString() {
    return currentState.toString();
  }

  public CommandBase intakeSubstationPosition() {
    return setArmGoalCommand(Arm.ArmPosition.SUBSTATION_PICKUP).alongWith(intake.openCommand());
  }

  public CommandBase runIntake() {
    return stateCondition(
        intake.runOnce(
            () -> {
              intake.stop();
              intake.close();
            }),
        intake.intakeCubeCommand());
  }

  public CommandBase updateArmController() {
    return arm.holdPositionCommand();
  }

  public CommandBase setArmGoalCommand(Arm.ArmPosition position) {
    return extension.retractCommand().andThen(arm.setGoalCommand(position).asProxy());
  }

  public CommandBase scoreLowPosition() {
    return stateCondition(
        setArmGoalCommand(Arm.ArmPosition.SCORE_CONE_LOW),
        setArmGoalCommand(Arm.ArmPosition.SCORE_CUBE_LOW));
  }

  public CommandBase scoreMidPosition() {
    return stateCondition(
        setArmGoalCommand(Arm.ArmPosition.SCORE_CONE_MID).andThen(extension.extendCommand()),
        setArmGoalCommand(Arm.ArmPosition.SCORE_CUBE_MID));
  }

  public CommandBase scoreHighPosition() {
    return stateCondition(Commands.none(), setArmGoalCommand(Arm.ArmPosition.SCORE_CUBE_HIGH));
  }

  public CommandBase score() {
    return stateCondition(
        intake.runOnce(
            () -> {
              intake.open();
              intake.stop();
            }),
        Commands.either(
            intake.ejectCubeCommand(0.2),
            intake.ejectCubeCommand(),
            () -> arm.getGoalPosition() == Arm.ArmPosition.SCORE_CUBE_LOW.valueRadians));
  }

  public CommandBase stateCondition(Command onCone, Command onCube) {
    return Commands.either(onCone, onCube, () -> getCurrentState() == GamePiece.Cone);
  }

  public CommandBase stowCommand() {
    return extension.retractCommand().andThen(setArmGoalCommand(Arm.ArmPosition.STOW));
  }

  public CommandBase floorPickupCube() {
    return setArmGoalCommand(Arm.ArmPosition.FLOOR_PICKUP)
        .alongWith(intake.openCommand())
        .andThen(extension.extendCommand().raceWith(updateArmController()));
  }

  public void setBrakeDisplay(boolean brakeDisplay) {
    this.brakeDisplay = brakeDisplay;
  }

  @Override
  public void periodic() {
    Color color;
    if (DriverStation.isDisabled()) {
      color = DriverStation.getAlliance() == DriverStation.Alliance.Blue ? Color.kBlue : Color.kRed;
    } else if (brakeDisplay) {
      color = Color.kGreen;
    } else if (getCurrentState() == GamePiece.Cone) {
      color = Color.kYellow;
    } else {
      color = Color.kPurple;
    }
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setLED(i, color);
    }
    led.setData(ledBuffer);
  }
}
