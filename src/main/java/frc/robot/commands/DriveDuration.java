// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class DriveDuration extends Command {

  Drivetrain drivetrain;
  private double duration;
  private double endTime;
  /** Creates a new DriveDuration. */
  public DriveDuration(Drivetrain drivetrain, double duration) {
      addRequirements(drivetrain);
      this.drivetrain = drivetrain;
      this.duration = duration;
      
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double endTime = System.currentTimeMillis()+duration;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.setOutput(0.25, 0.25);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.setOutput(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return System.currentTimeMillis()<=endTime;
  }
}
