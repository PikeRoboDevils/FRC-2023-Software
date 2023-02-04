/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import com.revrobotics.CANSparkMax.IdleMode;

/**
 * The Constants class provides a convenient place to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 */
public final class Constants {
  public static class DrivetrainConstants {
    public static final int LEFT_LEADER_ID = 1;
    public static final int LEFT_FOLLOWER_ONE_ID = 3;
    public static final int LEFT_FOLLOWER_TWO_ID = 5;
    public static final int RIGHT_LEADER_ID = 2;
    public static final int RIGHT_FOLLOWER_ONE_ID = 4;
    public static final int RIGHT_FOLLOWER_TWO_ID = 6;

    public static final IdleMode IDLE_MODE = IdleMode.kCoast;
  }

  public static class ControlBoardConstants {
    public static final int LEFT_STICK = 0;
    public static final int RIGHT_STICK = 1;

    public static final int XBOX_PORT = 5;
  }
}
