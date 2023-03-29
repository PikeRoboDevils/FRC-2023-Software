/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import static org.pikerobodevils.frc2023.Constants.IntakeConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.util.function.DoubleSupplier;
import org.pikerobodevils.frc2023.Constants;

public class Intake extends SubsystemBase implements Loggable {
  DoubleSolenoid intakeCylinders =
      new DoubleSolenoid(Constants.PM_TYPE, FORWARD_CHANNEL, REVERSE_CHANNEL);

  // left
  private final CANSparkMax main =
      new CANSparkMax(LEFT_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
  // right
  private final CANSparkMax follower =
      new CANSparkMax(RIGHT_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

  LinearFilter currentFilter = LinearFilter.movingAverage(10);
  private double filteredCurrent;

  public Intake() {
    main.restoreFactoryDefaults();
    main.setInverted(true);
    // main.setSmartCurrentLimit(CURRENT_LIMIT);
    main.burnFlash();
    follower.restoreFactoryDefaults();
    follower.setSmartCurrentLimit(CURRENT_LIMIT);
    Timer.delay(.1);
    follower.follow(main, true);
    Timer.delay(0.1);
    follower.burnFlash();
    Timer.delay(.1);
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
                .until(() -> debounce.calculate(getFilteredCurrent() > INTAKE_STALL_DETECTION)))
        // Reduce motor power to holding power
        .finallyDo(
            (interrupted) -> {
              System.out.println("ended");
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
