/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import static org.pikerobodevils.frc2023.Constants.ControlBoardConstants.*;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class ControlBoard {
  public final CommandXboxController driver = new CommandXboxController(DRIVER_PORT);

  public final CommandXboxController operator = new CommandXboxController(OPERATOR_PORT);

  public double getSpeed() {
    double leftY = -driver.getLeftY();
    return Math.signum(leftY) * Math.pow(MathUtil.applyDeadband(leftY, .04), 2);
  }

  public double getTurn() {
    double rightX = -driver.getRightX();
    return Math.signum(rightX) * Math.pow(MathUtil.applyDeadband(rightX, .04), 2);
  }
}
