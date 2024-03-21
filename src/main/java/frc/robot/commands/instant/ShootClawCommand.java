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


//THIS iS NOT BEING USED
public class ShootClawCommand extends Command {

  Claw claw;
  double speed;

  public ShootClawCommand(Claw claw) {
    addRequirements(claw); 
    this.claw = claw;
    claw.setOutput(0);

  }


  // Called when the command is initially scheduled.
  /* 
  @Override
  public void initialize() {
    claw.setMode(ClawSpeeds.SHOOT);
  }
  */

  @Override
  public void execute() {
    speed = Claw.CLAWSHOOTSPEED;
    claw.setOutput(speed);
  }


  @Override 
  public void end(boolean isInterrupted) {
    claw.setOutput(0);
  }
}
