/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.subsystems;

import static org.pikerobodevils.frc2023.Constants.ExtensionConstants.*;
import static org.pikerobodevils.lib.Commands1.conditionally;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.pikerobodevils.frc2023.Constants;

public class Extension extends SubsystemBase {
  private final DoubleSolenoid upper =
      new DoubleSolenoid(Constants.PM_TYPE, UPPER_FORWARD, UPPER_REVERSE);
  private final DoubleSolenoid lower =
      new DoubleSolenoid(Constants.PM_TYPE, LOWER_FORWARD, LOWER_REVERSE);

  private State state = State.Retracted;

  public Extension() {
    setStateCommand(State.Retracted);
  }

  public void extend() {
    setState(State.Retracted);
  }

  public void retract() {
    setState(State.Retracted);
  }

  public void setState(State state) {
    this.state = state;
    upper.set(state.solenoidValue);
    lower.set(state.solenoidValue);
  }

  public State getState() {
    return state;
  }

  public CommandBase extendCommand() {
    return setStateCommand(State.Extended);
  }

  public CommandBase retractCommand() {
    return setStateCommand(State.Retracted);
  }

  public CommandBase setStateCommand(State state) {
    return conditionally(
        () -> state == getState(),
        runOnce(() -> setState(state)).andThen(Commands.waitSeconds(.5)));
  }

  public enum State {
    Extended(DoubleSolenoid.Value.kForward),
    Retracted(DoubleSolenoid.Value.kReverse);

    public final DoubleSolenoid.Value solenoidValue;

    State(DoubleSolenoid.Value solenoidValue) {
      this.solenoidValue = solenoidValue;
    }
  }
}
