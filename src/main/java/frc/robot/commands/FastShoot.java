// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Claw;

public class FastShoot extends Command {
  Claw claw;

  /** Creates a new FastShoot. */
  public FastShoot(Claw claw) {
    addRequirements(claw);
    this.claw = claw;
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    claw.setOutput(-1.0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    claw.setOutput(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
