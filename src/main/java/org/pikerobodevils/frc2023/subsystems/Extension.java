/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import static org.pikerobodevils.frc2023.Constants.ExtensionConstants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Extension extends SubsystemBase {
  private final DoubleSolenoid upper =
      new DoubleSolenoid(PneumaticsModuleType.REVPH, UPPER_FORWARD, UPPER_REVERSE);
  private final DoubleSolenoid lower =
      new DoubleSolenoid(PneumaticsModuleType.REVPH, LOWER_FORWARD, LOWER_REVERSE);

  public Extension() {
    retract();
  }

  public enum State {
    Extended,
    Retracted;
  }

  private State state = State.Retracted;

  public void extend() {
    this.state = State.Extended;
    upper.set(DoubleSolenoid.Value.kForward);
    lower.set(DoubleSolenoid.Value.kForward);
  }

  public void retract() {
    this.state = State.Retracted;
    upper.set(DoubleSolenoid.Value.kReverse);
    lower.set(DoubleSolenoid.Value.kReverse);
  }

  public State getState() {
    return state;
  }

  public CommandBase extendCommand() {
    return Commands.either(
        runOnce(this::extend).andThen(Commands.waitSeconds(.5)),
        runOnce(this::extend),
        () -> getState() != State.Extended);
  }

  public CommandBase retractCommand() {
    return Commands.either(
        runOnce(this::retract).andThen(Commands.waitSeconds(.5)),
        runOnce(this::retract),
        () -> getState() != State.Retracted);
  }
}
