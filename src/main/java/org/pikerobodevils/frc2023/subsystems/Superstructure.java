/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.*;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.util.function.DoubleSupplier;
import org.pikerobodevils.frc2023.Constants;
import org.pikerobodevils.frc2023.subsystems.Arm.ArmPosition;
import org.tinylog.Logger;

public class Superstructure extends SubsystemBase implements Loggable {
  private boolean brakeDisplay = false;
  AddressableLED led = new AddressableLED(0);
  AddressableLEDBuffer ledBuffer = new AddressableLEDBuffer(200);

  @Log.Exclude private final Arm arm;
  @Log.Exclude private final Intake intake;
  @Log.Exclude private final Extension extension;

  public enum GamePiece {
    Cube,
    Cone;
  }

  public enum SuperstructureState {
    STOW(ArmPosition.STOW),
    SUBSTATION_PICKUP(ArmPosition.SUBSTATION_PICKUP),
    SCORE_CONE_LOW(ArmPosition.SCORE_CONE_LOW),
    SCORE_CONE_MID(
        ArmPosition.SCORE_CONE_MID,
        Extension.State.Extended,
        Constants.IntakeConstants.DEFAULT_OUTTAKE),
    SCORE_CUBE_LOW(ArmPosition.SCORE_CUBE_LOW, .2),
    SCORE_CUBE_MID(ArmPosition.SCORE_CUBE_MID, .5),
    SCORE_CUBE_HIGH(ArmPosition.SCORE_CUBE_HIGH, Constants.IntakeConstants.DEFAULT_OUTTAKE),
    FLOOR_PICKUP_CUBE(
        ArmPosition.FLOOR_PICKUP_CUBE,
        Extension.State.Extended,
        Constants.IntakeConstants.DEFAULT_OUTTAKE),
    FLOOR_PICKUP_CONE(
        ArmPosition.FLOOR_PICKUP_CONE,
        Extension.State.Extended,
        Constants.IntakeConstants.DEFAULT_OUTTAKE),
    CUBE_SHOOT(ArmPosition.SHOOT_CUBE, 1);
    public final double outtakeSpeed;
    public final ArmPosition armPosition;
    public final Extension.State extensionState;

    SuperstructureState(ArmPosition armPosition, double outtakeSpeed) {
      this(armPosition, Extension.State.Retracted, outtakeSpeed);
    }

    SuperstructureState(
        ArmPosition armPosition, Extension.State extensionState, double outtakeSpeed) {
      this.armPosition = armPosition;
      this.extensionState = extensionState;
      this.outtakeSpeed = outtakeSpeed;
    }

    SuperstructureState(ArmPosition armPosition) {
      this(armPosition, Constants.IntakeConstants.DEFAULT_OUTTAKE);
    }
  }

  private GamePiece currentGamePiece = GamePiece.Cube;
  private SuperstructureState lastSetState = SuperstructureState.STOW;

  public Superstructure(Arm arm, Intake intake, Extension extension) {
    this.arm = arm;
    this.intake = intake;
    this.extension = extension;
    led.setLength(200);
    led.start();
  }

  public void setGamePiece(GamePiece gamePiece) {
    this.currentGamePiece = gamePiece;
  }

  public GamePiece getCurrentGamePiece() {
    return currentGamePiece;
  }

  @Log(name = "Current Game Piece")
  public String getCurrentGamePieceString() {
    return getCurrentGamePiece().toString();
  }

  private void setState(SuperstructureState state) {
    Logger.tag("Superstructure").info("Setting state to {}", state.toString());
    this.lastSetState = state;
  }

  public SuperstructureState getLastSetState() {
    return lastSetState;
  }

  @Log(name = "Last Set State")
  public String getLastSetStateString() {
    return getLastSetState().toString();
  }

  public double getCurrentOuttakeSpeed() {
    return getLastSetState().outtakeSpeed;
  }

  /**
   * Returns whether the arm should be allowed to move from its current position to a given position
   * without retracting the extension.
   *
   * @param newState the new position to move to
   * @return whether the arm should be allowed to move without retracting.
   */
  public boolean allowMoveWhileExtended(SuperstructureState newState) {
    return Math.abs(arm.getGoalPosition() - newState.armPosition.valueRadians)
        < Units.degreesToRadians(8);
  }

  public CommandBase intakeSubstationPosition() {
    return setStateCommand(SuperstructureState.SUBSTATION_PICKUP).alongWith(intake.openCommand());
  }

  public CommandBase runIntake() {
    return stateCondition(
        intake.startEnd(
            () -> {
              intake.stop();
              // intake.setRollers(-1);
              intake.close();
            },
            intake::stop),
        intake.intakeCubeCommand());
  }

  public CommandBase updateArmController() {
    return arm.holdPositionCommand();
  }

  public CommandBase setArmGoalCommand(ArmPosition position) {
    return arm.setGoalCommand(position).asProxy();
  }

  public CommandBase setArmGoalCommand(DoubleSupplier position) {
    return arm.setGoalCommand(position).asProxy();
  }

  public CommandBase setStateCommand(SuperstructureState state) {
    return extension
        .retractCommand()
        .unless(() -> allowMoveWhileExtended(state))
        .andThen(setArmGoalCommand(state.armPosition).alongWith(runOnce(() -> setState(state))))
        .andThen(extension.setStateCommand(state.extensionState));
  }

  public CommandBase scoreLowPosition() {
    return stateCondition(
        setStateCommand(SuperstructureState.SCORE_CONE_LOW),
        setStateCommand(SuperstructureState.SCORE_CUBE_LOW));
  }

  public CommandBase scoreMidPosition() {
    return stateCondition(
        setStateCommand(SuperstructureState.SCORE_CONE_MID),
        setStateCommand(SuperstructureState.SCORE_CUBE_MID));
  }

  public CommandBase scoreHighPosition() {
    return stateCondition(Commands.none(), setStateCommand(SuperstructureState.SCORE_CUBE_HIGH));
  }

  public CommandBase score() {
    return stateCondition(
        intake.runOnce(
            () -> {
              intake.open();
              intake.stop();
            }),
        intake.ejectCubeCommand(() -> getLastSetState().outtakeSpeed));
  }

  public CommandBase stateCondition(Command onCone, Command onCube) {
    return Commands.either(onCone, onCube, () -> getCurrentGamePiece() == GamePiece.Cone);
  }

  public CommandBase stowCommand() {
    return setStateCommand(SuperstructureState.STOW);
  }

  public CommandBase floorPickup() {
    return setStateCommand(SuperstructureState.FLOOR_PICKUP_CUBE).alongWith(intake.openCommand());
  }

  public CommandBase bumpUp() {
    return setArmGoalCommand(
        () ->
            getLastSetState().armPosition.valueRadians
                + Constants.SuperstructureConstants.BUMP_UP_ANGLE);
  }

  public CommandBase bumpDown() {
    return setArmGoalCommand(
        () ->
            getLastSetState().armPosition.valueRadians
                + Constants.SuperstructureConstants.BUMP_DOWN_DOUBLE);
  }

  public CommandBase resetArmState() {
    return setArmGoalCommand(() -> getLastSetState().armPosition.valueRadians);
    // return deferred(() -> setStateCommand(getLastSetState()), extension, this);
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
    } else if (getCurrentGamePiece() == GamePiece.Cone) {
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
