/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import static org.pikerobodevils.frc2023.Constants.ControlBoardConstants.*;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class ControlBoard {
  private final CommandJoystick leftStick = new CommandJoystick(LEFT_STICK);
  private final CommandJoystick rightStick = new CommandJoystick(RIGHT_STICK);
  private final CommandXboxController xBox = new CommandXboxController(5);

  public double getSpeed() {
    double leftY = -xBox.getLeftY() * .75;
    return Math.signum(leftY) * Math.pow(MathUtil.applyDeadband(leftY, .04), 2);
  }

  public double getTurn() {
    double rightX = -xBox.getRightX() * .75;
    return Math.signum(rightX) * Math.pow(MathUtil.applyDeadband(rightX, .04), 2);
  }
}
