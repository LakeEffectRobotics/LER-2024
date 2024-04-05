// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;

public class AutoRotateCommand extends Command {

  Drivetrain drivetrain;
  Gyro gyro;
  private double wait;
  private double rotateAngle;
  private double rotateTargetAngle;

  private static final int ROTATION_DEADZONE = 5;

  /** Creates a new DriveDuration. */
  public AutoRotateCommand(Drivetrain drivetrain, Gyro gyro, double waitTime, double rotateAngle) {
      addRequirements(drivetrain, gyro);
      this.drivetrain = drivetrain;
      this.gyro = gyro;
      this.wait = System.currentTimeMillis()+waitTime;
      this.rotateAngle = rotateAngle;
      this.rotateTargetAngle = 0;
  }



  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
     rotateTargetAngle = gyro.getAngle()+rotateAngle;
     System.out.println("ROTATE - current angle: "+gyro.getCurrentCommand());
     System.out.println("ROTATE - target angle: "+rotateTargetAngle);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(System.currentTimeMillis()>wait) {
      if(rotateAngle > 0) { //positive
        drivetrain.setOutput(-drivetrain.AUTO_SPEED_ROTATE_LEFT, drivetrain.AUTO_SPEED_ROTATE_RIGHT);
      } else if(rotateTargetAngle < 0) { //negative
        drivetrain.setOutput(drivetrain.AUTO_SPEED_ROTATE_LEFT, -drivetrain.AUTO_SPEED_ROTATE_RIGHT);
      }

    if(isFinished()) {
      end(true);
    }
  }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean isInterupted) {
    drivetrain.setOutput(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(gyro.getAngle()-rotateTargetAngle) <= ROTATION_DEADZONE;
  }
}
