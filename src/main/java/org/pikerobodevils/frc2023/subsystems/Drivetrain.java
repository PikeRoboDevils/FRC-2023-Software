/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import static org.pikerobodevils.frc2023.Constants.AutoBalanceConstants.BALANCED_THRESHOLD;
import static org.pikerobodevils.frc2023.Constants.AutoBalanceConstants.BANG_BANG_VOLTS;
import static org.pikerobodevils.frc2023.Constants.DrivetrainConstants.*;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.util.function.DoubleSupplier;

public class Drivetrain extends SubsystemBase implements Loggable {

  private final CANSparkMax leftLeader = new CANSparkMax(LEFT_LEADER_ID, MotorType.kBrushless);
  private final CANSparkMax leftFollowerOne =
      new CANSparkMax(LEFT_FOLLOWER_ONE_ID, MotorType.kBrushless);
  private final CANSparkMax leftFollowerTwo =
      new CANSparkMax(LEFT_FOLLOWER_TWO_ID, MotorType.kBrushless);

  private final CANSparkMax rightLeader = new CANSparkMax(RIGHT_LEADER_ID, MotorType.kBrushless);
  private final CANSparkMax rightFollowerOne =
      new CANSparkMax(RIGHT_FOLLOWER_ONE_ID, MotorType.kBrushless);
  private final CANSparkMax rightFollowerTwo =
      new CANSparkMax(RIGHT_FOLLOWER_TWO_ID, MotorType.kBrushless);

  private final AHRS navX = new AHRS();

  LinearFilter pitchRate = LinearFilter.backwardFiniteDifference(1, 2, 0.02);

  double currentPitchRate = 0;

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    leftLeader.restoreFactoryDefaults();
    leftLeader.setIdleMode(IDLE_MODE);
    leftLeader.setSmartCurrentLimit(40);
    leftLeader.burnFlash();

    leftFollowerOne.restoreFactoryDefaults();
    leftFollowerOne.setIdleMode(IDLE_MODE);
    leftFollowerOne.follow(leftLeader);
    leftFollowerTwo.setSmartCurrentLimit(CURRENT_LIMIT);
    leftFollowerOne.burnFlash();

    leftFollowerTwo.restoreFactoryDefaults();
    leftFollowerTwo.setIdleMode(IDLE_MODE);
    leftFollowerTwo.follow(leftLeader);
    leftFollowerTwo.setSmartCurrentLimit(CURRENT_LIMIT);
    leftFollowerTwo.burnFlash();

    rightLeader.restoreFactoryDefaults();
    rightLeader.setIdleMode(IDLE_MODE);
    rightLeader.setInverted(true);
    rightLeader.setSmartCurrentLimit(CURRENT_LIMIT);
    rightLeader.burnFlash();

    rightFollowerOne.restoreFactoryDefaults();
    rightFollowerOne.setIdleMode(IDLE_MODE);
    rightFollowerOne.follow(rightLeader);
    rightFollowerTwo.setSmartCurrentLimit(CURRENT_LIMIT);
    rightFollowerOne.burnFlash();

    rightFollowerTwo.restoreFactoryDefaults();
    rightFollowerTwo.setIdleMode(IDLE_MODE);
    rightFollowerTwo.follow(rightLeader);
    rightFollowerTwo.setSmartCurrentLimit(CURRENT_LIMIT);
    rightFollowerTwo.burnFlash();
  }

  public void setLeftRight(double left, double right) {
    leftLeader.set(left);
    rightLeader.set(right);
  }

  public void setLeftRightVoltage(double left, double right) {
    leftLeader.setVoltage(left);
    rightLeader.setVoltage(right);
  }

  public void setIdleMode(CANSparkMax.IdleMode mode) {
    leftLeader.setIdleMode(mode);
    leftFollowerOne.setIdleMode(mode);
    leftFollowerTwo.setIdleMode(mode);

    rightLeader.setIdleMode(mode);
    rightFollowerOne.setIdleMode(mode);
    rightFollowerTwo.setIdleMode(mode);
  }

  @Log(name = "Yaw")
  public double getYaw() {
    return navX.getYaw();
  }

  @Log(name = "Pitch")
  public double getPitch() {
    return navX.getPitch();
  }

  @Log(name = "Pitch Rate")
  public double getPitchRate() {
    return currentPitchRate;
  }

  @Log(name = "Roll")
  public double getRoll() {
    return navX.getRoll();
  }

  @Log
  public double getLeftVoltage() {
    return leftLeader.getAppliedOutput() * leftLeader.getBusVoltage();
  }

  @Log
  public double getRightVoltage() {
    return leftLeader.getAppliedOutput() * leftLeader.getBusVoltage();
  }

  public void arcadeDrive(double speed, double rotation) {
    DifferentialDrive.WheelSpeeds speeds = DifferentialDrive.arcadeDriveIK(speed, rotation, false);
    setLeftRight(speeds.left, speeds.right);
  }

  public CommandBase arcadeDriveCommand(DoubleSupplier speed, DoubleSupplier rotation) {
    return run(() -> arcadeDrive(speed.getAsDouble(), rotation.getAsDouble()));
  }

  /*public CommandBase driveTrajectoryCommand(Trajectory trajectory) {
    return driveTrajectoryCommand(() -> trajectory);
  }*/

  /*public CommandBase driveTrajectoryCommand(Supplier<Trajectory> trajectory) {
    RamseteController ramsete = new RamseteController();
    PIDController leftController = new PIDController(0,0,0);
    PIDController rightController = new PIDController(0,0,0);
    return run(() -> {

    })
  }*/
  public CommandBase setLeftRightVoltageCommand(double leftVoltage, double rightVoltage) {
    return run(() -> {
          setLeftRightVoltage(leftVoltage, rightVoltage);
        })
        .finallyDo(
            (interrupted) -> {
              setLeftRightVoltage(0, 0);
            });
  }

  public CommandBase bangBangBalance() {
    return run(
        () -> {
          var pitch = getPitch();
          double outputVolts;
          if (pitch > BALANCED_THRESHOLD) {
            // too far back; go forwards
            outputVolts = BANG_BANG_VOLTS;
          } else if (pitch < -BALANCED_THRESHOLD) {
            // too far forwards; go back
            outputVolts = -BANG_BANG_VOLTS;
          } else {
            outputVolts = 0;
          }
          setLeftRightVoltage(outputVolts, outputVolts);
        });
  }

  @Override
  public void periodic() {
    currentPitchRate = pitchRate.calculate(getPitch());
  }
}
