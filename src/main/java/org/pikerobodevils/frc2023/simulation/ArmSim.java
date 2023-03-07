/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.simulation;

import static org.pikerobodevils.frc2023.Constants.ArmConstants.*;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.pikerobodevils.frc2023.subsystems.Arm;

public class ArmSim {
  DCMotor motors = DCMotor.getNEO(2);
  DutyCycleEncoderSim encoderSim = new DutyCycleEncoderSim(ENCODER_ABS_DIO);

  EncoderSim quadEncoderSim = EncoderSim.createForChannel(ENCODER_QUAD_A);

  SingleJointedArmSim sim =
      new SingleJointedArmSim(
          motors,
          ARM_REDUCTION,
          MOI_KG_M_SQUARED,
          COM_DISTANCE,
          Units.degreesToRadians(-90),
          Units.degreesToRadians(75),
          true);

  private final Arm arm;

  public ArmSim(Arm arm) {
    this.arm = arm;
    encoderSim.setDistancePerRotation(RAD_PER_ENCODER_ROTATION);
  }

  public void update() {
    sim.setInputVoltage(arm.getVoltage());
    sim.update(.02);
    encoderSim.setDistance(sim.getAngleRads());
    quadEncoderSim.setDistance(sim.getAngleRads());
    quadEncoderSim.setRate(sim.getVelocityRadPerSec());
    SmartDashboard.putNumber("Current", sim.getCurrentDrawAmps());
  }
}
