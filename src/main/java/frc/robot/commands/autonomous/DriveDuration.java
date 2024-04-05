// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class DriveDuration extends Command {

  Drivetrain drivetrain;
  private double duration;
  private double endTime;
  private double wait;
  private double waitTime;

  /** Creates a new DriveDuration. */
  public DriveDuration(Drivetrain drivetrain, double duration, double waitTime) {
      addRequirements(drivetrain);
      this.drivetrain = drivetrain;
      this.duration = duration;
      this.waitTime = waitTime;
      this.wait = System.currentTimeMillis()+waitTime;


      
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("AUTO DRIVE - initialized");
     this.endTime = System.currentTimeMillis()+waitTime+duration;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(System.currentTimeMillis()>wait) {
    drivetrain.setOutput(drivetrain.AUTO_SPEED_LEFT, drivetrain.AUTO_SPEED_RIGHT); 
    if(isFinished()) {
      end(true);
    }
  }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean isInterupted) {
    System.out.println("AUTO DRIVE - ended");
    drivetrain.setOutput(0, 0);
  }
  
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return System.currentTimeMillis()>endTime;
  }
}
