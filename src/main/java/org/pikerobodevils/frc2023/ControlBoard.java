/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import static org.pikerobodevils.frc2023.Constants.ControlBoardConstants.*;

import edu.wpi.first.wpilibj2.command.button.CommandJoystick;

public class ControlBoard {
  private final CommandJoystick leftStick = new CommandJoystick(LEFT_STICK);
  private final CommandJoystick rightStick = new CommandJoystick(RIGHT_STICK);

  public double getSpeed() {
    return -leftStick.getY();
  }

  public double getTurn() {
    return -rightStick.getX();
  }
}
