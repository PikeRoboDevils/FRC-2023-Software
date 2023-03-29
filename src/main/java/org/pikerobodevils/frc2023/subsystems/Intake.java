/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import static org.pikerobodevils.frc2023.Constants.IntakeConstants.*;

import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.util.function.DoubleSupplier;
import org.pikerobodevils.frc2023.Constants;
import org.pikerobodevils.lib.vendor.SparkMax;

public class Intake extends SubsystemBase implements Loggable {
  DoubleSolenoid intakeCylinders =
      new DoubleSolenoid(Constants.PM_TYPE, FORWARD_CHANNEL, REVERSE_CHANNEL);

  // left
  private final SparkMax main = new SparkMax(LEFT_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
  // right
  private final SparkMax follower =
      new SparkMax(RIGHT_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

  LinearFilter currentFilter = LinearFilter.movingAverage(5);
  private double filteredCurrent;

  public Intake() {
    main.setInverted(true);
    main.setSmartCurrentLimit(CURRENT_LIMIT);
    follower.setSmartCurrentLimit(CURRENT_LIMIT);
    follower.follow(main, true);
  }

  public void open() {
    intakeCylinders.set(DoubleSolenoid.Value.kForward);
  }

  public CommandBase openCommand() {
    return runOnce(this::open);
  }

  public CommandBase closeCommand() {
    return runOnce(this::close);
  }

  public void close() {
    intakeCylinders.set(DoubleSolenoid.Value.kReverse);
  }

  public void setRollers(double speed) {
    main.set(speed);
  }

  public void stop() {
    main.set(0);
  }

  @Log(name = "Current")
  public double getCurrent() {
    return main.getOutputCurrent();
  }

  @Log
  public double getFilteredCurrent() {
    return filteredCurrent;
  }

  @Log
  public double getOutput() {
    return main.getAppliedOutput();
  }

  @Log
  public double getControllerVoltage() {
    return main.getBusVoltage();
  }

  public CommandBase intakeCubeCommand() {
    Debouncer debounce = new Debouncer(1, Debouncer.DebounceType.kRising);
    // Open arms
    return runOnce(
            () -> {
              debounce.calculate(false);
              this.open();
            })
        // set the intake to cube intaking speed
        .andThen(
            run(() -> {
                  setRollers(INTAKE_CUBE_SPEED);
                })
                // Wait until current spike is detected for more than 1s
                .until(
                    () -> debounce.calculate(getFilteredCurrent() > INTAKE_CUBE_STALL_DETECTION)))
        // Reduce motor power to holding power
        .finallyDo(
            (interrupted) -> {
              setRollers(HOLD_CUBE_SPEED);
            });
  }

  public CommandBase intakeConeCommand() {
    Debouncer debounce = new Debouncer(5, Debouncer.DebounceType.kRising);
    // Open arms
    return runOnce(
            () -> {
              debounce.calculate(false);
              this.close();
            })
        // set the intake to cube intaking speed
        .andThen(
            run(() -> {
                  setRollers(INTAKE_CONE_SPEED);
                })
                // Wait until current spike is detected for more than 1s
                .until(
                    () -> debounce.calculate(getFilteredCurrent() > INTAKE_CONE_STALL_DETECTION)))
        // Reduce motor power to holding power
        .finallyDo(
            (interrupted) -> {
              setRollers(HOLD_CUBE_SPEED);
            });
  }

  public CommandBase ejectCubeCommand() {
    return ejectCubeCommand(DEFAULT_OUTTAKE);
  }

  public CommandBase ejectCubeCommand(double speed) {
    return ejectCubeCommand(() -> speed).withName("Eject Cube: " + speed);
  }

  public CommandBase ejectCubeCommand(DoubleSupplier speed) {
    return runEnd(() -> setRollers(speed.getAsDouble()), this::stop)
        .withTimeout(.5)
        .withName("Eject Cube");
  }

  @Override
  public void periodic() {
    filteredCurrent = currentFilter.calculate(getCurrent());
  }
}
