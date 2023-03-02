/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.frc2023;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import io.github.oblarg.oblog.Logger;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  // VXC8701
  private Compressor compressor = new Compressor(PneumaticsModuleType.REVPH);

  @Override
  public void robotInit() {
    compressor.enableAnalog(100, 120);
    //compressor.disable();
    DriverStation.silenceJoystickConnectionWarning(true);

    m_robotContainer = new RobotContainer();
    if (isReal()) {
      // DataLogManager.start();
    } else if (Constants.LOG_IN_SIM) {
      DataLogManager.start(
          Filesystem.getOperatingDirectory().toPath().resolve("sim_logs").toString());
    }
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Pressure", compressor.getPressure());
    CommandScheduler.getInstance().run();
    Logger.updateEntries();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {
    m_robotContainer.simulationPeriodic();
  }
}
