/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import static org.pikerobodevils.frc2023.Constants.DrivetrainConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
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

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
