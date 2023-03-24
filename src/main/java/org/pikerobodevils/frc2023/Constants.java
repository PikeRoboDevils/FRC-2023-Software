/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.RobotBase;

/**
 * The Constants class provides a convenient place to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 */
public final class Constants {
  public static final PneumaticsModuleType PM_TYPE =
      RobotBase.isReal() ? PneumaticsModuleType.REVPH : PneumaticsModuleType.CTREPCM;
  public static final boolean LOG_IN_SIM = false;
  public static final String SIM_LOG_DIR =
      Filesystem.getOperatingDirectory().toPath().resolve("sim_logs").toString();
  public static final String RIO_LOG_DIR = Filesystem.getOperatingDirectory().toPath().toString();

  public static class DrivetrainConstants {
    public static final int LEFT_LEADER_ID = 1;
    public static final int LEFT_FOLLOWER_ONE_ID = 3;
    public static final int LEFT_FOLLOWER_TWO_ID = 5;
    public static final int RIGHT_LEADER_ID = 2;
    public static final int RIGHT_FOLLOWER_ONE_ID = 4;
    public static final int RIGHT_FOLLOWER_TWO_ID = 6;

    public static final IdleMode IDLE_MODE = IdleMode.kCoast;

    public static final int CURRENT_LIMIT = 50;
  }

  public static class AutoBalanceConstants {
    public static final double BALANCED_THRESHOLD = 5; // degrees

    public static final double BANG_BANG_VOLTS = 0.5; // V
  }

  public static class ControlBoardConstants {
    public static final int DRIVER_PORT = 0;
    public static final int OPERATOR_PORT = 1;
  }

  public static class ArmConstants {
    public static final int LEFT_CONTROLLER_ID = 7;
    public static final int RIGHT_CONTROLLER_ID = 8;

    public static final int ENCODER_ABS_DIO = 4;
    public static final int ENCODER_QUAD_A = 5;
    public static final int ENCODER_QUAD_B = 6;

    public static final double QUAD_COUNTS_PER_REV = 2048;

    // Encoder pulley teeth / arm pulley teeth;
    public static final double ARM_TO_ENCODER_RATIO = 1;

    public static final double RAD_PER_ENCODER_ROTATION = 2 * Math.PI * ARM_TO_ENCODER_RATIO;
    public static final double RAD_PER_QUAD_TICK = RAD_PER_ENCODER_ROTATION / QUAD_COUNTS_PER_REV;

    public static final double OFFSET_DEGREES = 74;
    public static final double ENCODER_OFFSET =
        MathUtil.inputModulus(
            Units.degreesToRadians(OFFSET_DEGREES) / RAD_PER_ENCODER_ROTATION, 0, 1);

    public static final double KP = 5.3731; // 5.2103;
    public static final double KI = 0;
    public static final double KD = 0; // 1.6179;

    public static final double KS = 0.19677;
    public static final double KG = .5;
    public static final double KV = 2.114;
    public static final double KA = 0.12894;

    /**
     * SIM GAINS public static final double KP = 5; public static final double KI = 0; public static
     * final double KD = 1;
     *
     * <p>public static final double KS = 0; public static final double KG = 1.3; public static
     * final double KV = 2.22; public static final double KA = 0.04;
     */
    public static final double MAX_VELO = Math.PI;
    // Rad / s / s
    public static final double MAX_ACCEL = Math.PI * 3;
    public static final TrapezoidProfile.Constraints CONSTRAINTS =
        new TrapezoidProfile.Constraints(MAX_VELO, MAX_ACCEL);

    // The reduction between the motors and the arm.
    public static final double ARM_REDUCTION = 5 * 5 * (64.0 / 14);

    public static final double COM_DISTANCE = Units.inchesToMeters(25);
    public static final double MASS = Units.lbsToKilograms(15);

    public static final double MOI_KG_M_SQUARED = MASS * (Math.pow(COM_DISTANCE, 2));
  }

  public static class IntakeConstants {
    public static final int FORWARD_CHANNEL = 0;
    public static final int REVERSE_CHANNEL = 1;

    public static final int LEFT_ID = 9;
    public static final int RIGHT_ID = 10;

    public static final int CURRENT_LIMIT = 20; // Amps

    public static final double INTAKE_CUBE_SPEED = -.75;

    public static final double HOLD_CUBE_SPEED = -0.05;

    public static final double SHOOT_CUBE_SPEED = .75;
    public static final double INTAKE_STALL_DETECTION = 15; // Amps
  }

  public static class ExtensionConstants {
    public static final int UPPER_FORWARD = 2;
    public static final int UPPER_REVERSE = 3;
    public static final int LOWER_FORWARD = 4;
    public static final int LOWER_REVERSE = 5;
  }
}
