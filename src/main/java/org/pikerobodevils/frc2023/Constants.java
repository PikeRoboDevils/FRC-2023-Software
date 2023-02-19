/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 */
public final class Constants {
  public static final boolean LOG_IN_SIM = false;

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
    public static final int DRIVER_PORT = 0;
    public static final int OPERATOR_PORT = 1;
  }

  public static class ArmConstants {
    public static final int LEFT_CONTROLLER_ID = 7;
    public static final int RIGHT_CONTROLLER_ID = 8;

    public static final int ENCODER_QUAD_A = 5;
    public static final int ENCODER_QUAD_B = 6;
    public static final int ENCODER_ABS_DIO = 7;

    public static final double QUAD_COUNTS_PER_REV = 4096;

    // Encoder pulley teeth / arm pulley teeth;
    public static final double ARM_TO_ENCODER_RATIO = 1;

    public static final double RAD_PER_ENCODER_ROTATION = 2 * Math.PI * ARM_TO_ENCODER_RATIO;
    public static final double RAD_PER_QUAD_TICK = RAD_PER_ENCODER_ROTATION / QUAD_COUNTS_PER_REV;

    public static final double OFFSET_DEGREES = 0;
    public static final double ENCODER_OFFSET = Units.degreesToRadians(OFFSET_DEGREES);

    public static final double KP = 5;
    public static final double KI = 0;
    public static final double KD = 1;

    public static final double KS = 0;
    public static final double KG = 1.3;
    public static final double KV = 2.22;
    public static final double KA = 0.04;

    public static final double MAX_VELO = Math.PI * 2;
    // Rad / s / s
    public static final double MAX_ACCEL = Math.PI * 8;
    public static final TrapezoidProfile.Constraints CONSTRAINTS =
        new TrapezoidProfile.Constraints(MAX_VELO, MAX_ACCEL);

    // The reduction between the motors and the arm.
    public static final double ARM_REDUCTION = 5 * 5 * (64.0 / 14);

    public static final double COM_DISTANCE = Units.inchesToMeters(25);
    public static final double MASS = Units.lbsToKilograms(15);

    public static final double MOI_KG_M_SQUARED = MASS * (Math.pow(COM_DISTANCE, 2));
  }
}
