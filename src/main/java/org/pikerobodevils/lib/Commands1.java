/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.lib;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.BooleanSupplier;

public class Commands1 {
  public static CommandBase conditionally(BooleanSupplier condition, Command onTrue) {
    return Commands.either(onTrue, Commands.none(), condition);
  }
}
