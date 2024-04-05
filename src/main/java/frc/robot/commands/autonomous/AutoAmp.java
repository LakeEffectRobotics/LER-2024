// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AmpCommandGroup;
import frc.robot.commands.WristCommand;
import frc.robot.commands.instant.ShootClawCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristPosition;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoAmp extends SequentialCommandGroup {
  /** Creates a new IntakeCommandGroup. */
  public AutoAmp(Drivetrain drivetrain, Gyro gyro, Wrist wrist, Arm arm, Claw claw ) {
    addCommands(
      new WristCommand(wrist, WristPosition.UP),
      new DriveDuration(drivetrain, 300, 0),
      new AutoRotateCommand(drivetrain, gyro, 0, -90),
      new DriveDuration(drivetrain, 300, 0),
      new ParallelCommandGroup(
      new AmpCommandGroup(wrist, arm),
      new ShootClawCommand(claw)
      )
    );
  }
}
