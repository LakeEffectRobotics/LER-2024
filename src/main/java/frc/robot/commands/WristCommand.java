// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.SerialPort.WriteBufferMode;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristPosition;

public class WristCommand extends Command {

  Wrist wrist;

  Wrist.WristPosition pos;

  /** Creates a new WristCommand. */
  public WristCommand(Wrist wrist, Wrist.WristPosition pos) {
    addRequirements(wrist);
   this.wrist = wrist; 
   this.pos = pos;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("WRIST: command initialized");

    if(pos == Wrist.WristPosition.UP){ 
      wrist.moveWristUp();
    } else if (pos == WristPosition.INTAKE) {
      wrist.moveWristIntake();
    } else if (pos == WristPosition.TRAP) {
      wrist.moveWristTrap();
    } else if (pos == WristPosition.AMP) {
      wrist.moveWristAmp();
    } else if (pos == WristPosition.CLIMB) {
      wrist.moveWristClimb();
    }
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("WRIST: command ended");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return wrist.hasAchievedTargetAngle();
  }
}
