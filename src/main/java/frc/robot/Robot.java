// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Arm;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();

    /* auto selection */
    m_robotContainer.m_chooser.setDefaultOption("Default Auto", m_robotContainer.kDefaultAuto); //set default
    for(String auto : m_robotContainer.kAutos) { //get list from robotcontainer
      m_robotContainer.m_chooser.addOption(auto, auto);
    }
    SmartDashboard.putData("AutoChoices", m_robotContainer.m_chooser);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    
    SmartDashboard.putNumber("Left Encoder", m_robotContainer.drivetrain.leftDriveEncoder.getPosition());
    SmartDashboard.putNumber("Right Encoder", m_robotContainer.drivetrain.rightDriveEncoder.getPosition());
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {} 

  @Override
  public void disabledExit() {}

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
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();

  }

  @Override
  public void testPeriodic() {

    m_robotContainer.arm.setTargetAngle(90);
  }

  @Override
  public void testExit() {}
}
