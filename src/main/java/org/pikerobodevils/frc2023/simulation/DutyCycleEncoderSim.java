/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023.simulation;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;

/** Class to control a simulated duty cycle encoder. */
public class DutyCycleEncoderSim {
  private final SimDouble m_simPosition;
  private final SimDouble m_simDistancePerRotation;

  /**
   * Constructs from an DutyCycleEncoder object.
   *
   * @param encoder DutyCycleEncoder to simulate
   */
  public DutyCycleEncoderSim(DutyCycleEncoder encoder) {
    this(encoder.getSourceChannel());
  }

  /**
   * Constructs from a digital input channel.
   *
   * @param channel digital input channel.
   */
  public DutyCycleEncoderSim(int channel) {
    SimDeviceSim wrappedSimDevice = new SimDeviceSim("DutyCycle:DutyCycleEncoder", channel);
    m_simPosition = wrappedSimDevice.getDouble("position");
    m_simDistancePerRotation = wrappedSimDevice.getDouble("distance_per_rot");
  }

  /**
   * Set the position in turns.
   *
   * @param turns The position.
   */
  public void set(double turns) {
    m_simPosition.set(turns);
  }

  /**
   * Set the position.
   *
   * @param distance The position.
   */
  public void setDistance(double distance) {
    m_simPosition.set(distance / m_simDistancePerRotation.get());
  }

  public void setDistancePerRotation(double distancePerRotation) {
    if (m_simDistancePerRotation != null) {
      m_simDistancePerRotation.set(distancePerRotation);
    }
  }
}
