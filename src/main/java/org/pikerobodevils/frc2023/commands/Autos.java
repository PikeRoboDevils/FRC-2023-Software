/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.commands;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import org.pikerobodevils.frc2023.subsystems.Drivetrain;

public final class Autos {
  Drivetrain drivetrain;
  Superstructure superstructure;

  public Autos(Drivetrain drivetrain, Superstructure superstructure) {
    this.drivetrain = drivetrain;
    this.superstructure = superstructure;
  }

  public CommandBase driveBackAuto() {
    return drivetrain.setLeftRightVoltageCommand(-3, -3).withTimeout(3.5);
  }

  public CommandBase scoreMidCubeDriveBack() {
    return scoreMidCube().andThen(driveBackAuto());
  }

  public CommandBase scoreHighCube() {
    return superstructure
        .scoreHighPosition()
        .andThen(drivetrain.setLeftRightVoltageCommand(3, 3).withTimeout(.4))
        .andThen(superstructure.score())
        .andThen(drivetrain.setLeftRightVoltageCommand(-3, -3).withTimeout(.4))
        .andThen(superstructure.stowCommand());
  }

  public CommandBase scoreHighCubeDriveBack() {
    return scoreHighCube().andThen(driveBackAuto());
  }

  public CommandBase scoreMidCube() {
    return superstructure
        .scoreMidPosition()
        .andThen(superstructure.score())
        .andThen(superstructure.stowCommand());
  }

  // Pitch: + --> backwards
  public CommandBase autoBalanceBackwards() {
    Debouncer pitchDebouncer = new Debouncer(0.25, Debouncer.DebounceType.kRising);
    Debouncer rateDebouncer = new Debouncer(0.05, Debouncer.DebounceType.kRising);
    return Commands.runOnce(
            () -> {
              pitchDebouncer.calculate(false);
              rateDebouncer.calculate(false);
            })
        .andThen(
            drivetrain
                .setLeftRightVoltageCommand(-3, -3)
                .until(() -> pitchDebouncer.calculate(drivetrain.getPitch() < -10)))
        .andThen(
            drivetrain
                .setLeftRightVoltageCommand(-1., -1.)
                .withTimeout(1)
                .andThen(
                    drivetrain
                        .setLeftRightVoltageCommand(-.75, -.75)
                        .until(
                            () -> {
                              System.out.println(drivetrain.getPitchRate());
                              return rateDebouncer.calculate(drivetrain.getPitchRate() > 15);
                            })))
        .andThen(drivetrain.bangBangBalance());
  }

  public CommandBase autoBalanceForwards() {
    Debouncer pitchDebouncer = new Debouncer(0.75, Debouncer.DebounceType.kRising);
    Debouncer rateDebouncer = new Debouncer(0.05, Debouncer.DebounceType.kRising);
    return Commands.runOnce(
            () -> {
              pitchDebouncer.calculate(false);
              rateDebouncer.calculate(false);
            })
        .andThen(
            drivetrain
                .setLeftRightVoltageCommand(3, 3)
                .until(() -> pitchDebouncer.calculate(drivetrain.getPitch() > 14)))
        .andThen(
            drivetrain
                .setLeftRightVoltageCommand(1., 1.)
                .withTimeout(1)
                .andThen(
                    drivetrain
                        .setLeftRightVoltageCommand(.75, .75)
                        .until(
                            () -> {
                              System.out.println(drivetrain.getPitchRate());
                              return rateDebouncer.calculate(drivetrain.getPitchRate() < -15);
                            })))
        .andThen(drivetrain.bangBangBalance());
  }
}
