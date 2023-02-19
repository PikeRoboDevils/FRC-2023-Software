/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import static org.pikerobodevils.frc2023.Constants.ArmConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {

  CANSparkMax leftController =
      new CANSparkMax(LEFT_CONTROLLER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
  CANSparkMax rightController =
      new CANSparkMax(RIGHT_CONTROLLER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

  Encoder encoder =
      new Encoder(ENCODER_QUAD_A, ENCODER_QUAD_B, false, CounterBase.EncodingType.k4X);
  DutyCycleEncoder absoluteEncoder = new DutyCycleEncoder(ENCODER_ABS_DIO);
  ArmFeedforward feedforward = new ArmFeedforward(KS, KG, KV, KA);
  ProfiledPIDController controller = new ProfiledPIDController(KP, KI, KD, CONSTRAINTS);

  public Arm() {
    encoder.setDistancePerPulse(RAD_PER_QUAD_TICK);
    absoluteEncoder.setDistancePerRotation(RAD_PER_ENCODER_ROTATION);
    absoluteEncoder.setPositionOffset(ENCODER_OFFSET);

    setDefaultCommand(holdPositionCommand());
  }

  /**
   * Sets the voltage applied to the arm motors
   *
   * @param volts voltage to apply.
   */
  public void setVoltage(double volts) {
    leftController.setVoltage(volts);
    rightController.setVoltage(volts);
  }

  public double getPosition() {
    return absoluteEncoder.getDistance();
  }

  /**
   * Returns the velocity of the arm in radians / second.
   *
   * @return velocity of the arm in radians / second
   */
  public double getVelocity() {
    return encoder.getRate();
  }

  /**
   * Return whether the arm is at the desired goal.
   *
   * @return true if the arm is at the goal otherwise false.
   */
  public boolean atGoal() {
    return controller.atGoal();
  }

  /**
   * Sets the goal position of the arm controller, with a velocity goal of 0 rad/s. Does not update
   * motor outputs.
   *
   * @param goal goal position of the arm in radians.
   */
  public void setGoal(double goal) {
    controller.setGoal(goal);
  }

  public void updatePositionController(double setpoint) {
    setGoal(setpoint);
    updatePositionController();
  }

  public void updatePositionController() {
    // Update controller with new measurement
    // This updates the controllers setpoint internally.
    var feedbackOutput = controller.calculate(absoluteEncoder.getDistance());
    // use the newly updated setpoint to calculate a feedforward.
    var setpoint = controller.getSetpoint();
    var feedforwardOutput = feedforward.calculate(setpoint.position, setpoint.velocity);

    var totalOutputVolts = feedbackOutput + feedforwardOutput;
    setVoltage(totalOutputVolts);
  }

  public CommandBase holdPositionCommand() {
    return run(this::updatePositionController);
  }

  public CommandBase setGoalCommand(double goalPosition) {
    return runOnce(
            () -> {
              setGoal(goalPosition);
            })
        .andThen(holdPositionCommand())
        .until(this::atGoal);
  }
}
