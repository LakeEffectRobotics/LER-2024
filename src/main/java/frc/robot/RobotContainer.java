// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.ExampleSubsystem;

public class RobotContainer {

  public ExampleSubsystem drivetrain = new ExampleSubsystem(RobotMap.leftController1);
  
  public RobotContainer() {
  
  

    drivetrain.setDefaultCommand(new DriveCommand(drivetrain, OI.leftDriveSupplier));
 
  
  }



 
}
