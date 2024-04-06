// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import javax.swing.text.html.parser.Entity;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Claw;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html


public class ShootForAuto extends Command {

  Claw claw;
  double speed;

  double benchSpeed = 0;
  double time;
  double endtime;


  public ShootForAuto(Claw claw, double time) {
    addRequirements(claw); 
    this.claw = claw;
    this.time = time;
    this.endtime = 0;
  }

  @Override
  public void initialize(){
    endtime = System.currentTimeMillis()+time;
    
    benchSpeed = -1;
  }

  @Override
  public void execute() {
      speed = Claw.CLAWSHOOTSPEED;
      claw.setOutput(speed);
      benchSpeed = Math.max(benchSpeed, claw.getSpeed());
      SmartDashboard.putNumber("Bench", benchSpeed);

      }

  @Override
  public void end(boolean isInterrupted){
    claw.setOutput(0);
  }

  @Override
  public boolean isFinished() {
    return System.currentTimeMillis()>=endtime;
  }
}
