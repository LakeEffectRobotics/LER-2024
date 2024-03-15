// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.instant;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.OI;
import frc.robot.subsystems.Claw;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html


public class IntakeClawCommand extends Command{

  Claw claw;
  double speed;

  public IntakeClawCommand(Claw claw, double triggerpos) {
    addRequirements(claw); 
    this.claw = claw;
    claw.setOutput(0);
  }

  @Override
  public void execute() {
    //double triggerPosition = OI.spinInClawSpeedSupplier.getAsDouble();
    //System.out.println(triggerPosition);
      speed = Claw.CLAWINTAKESPEED;
      claw.setOutput(speed);



  }

  @Override
  public void end(boolean isInterrupted){
    claw.setOutput(0);
  }

}
