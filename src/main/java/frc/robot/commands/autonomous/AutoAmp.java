// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import javax.naming.directory.DirContext;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AmpCommandGroup;
import frc.robot.commands.IntakeCommandGroup;
import frc.robot.commands.ShootForAuto;
import frc.robot.commands.TransportCommandGroup;
import frc.robot.commands.WristCommand;
import frc.robot.commands.instant.IntakeClawCommand;
import frc.robot.commands.instant.ShootClawCommand;
import frc.robot.subsystems.Arm.Arm;
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
      new ParallelCommandGroup(
      new WristCommand(wrist, WristPosition.UP), // start wrist up (safety)
      new DriveDuration(drivetrain, 777, 0, null, null) // drive to amp
      ),
      
      new AutoRotateCommand(drivetrain, gyro, 0, -90), //rotate to face amp
      new DriveDuration(drivetrain, 350, 0, -drivetrain.AUTO_SPEED_LEFT, -drivetrain.AUTO_SPEED_RIGHT), //drive to amp
      new DriveDuration(drivetrain, 100, 0, -0.25, null), // align to amp
      new AmpCommandGroup(wrist, arm), //move arm to amp position
      new ShootForAuto(claw, 500), // shoot 
      new ParallelCommandGroup(
      new TransportCommandGroup(wrist, arm), //move arm to transport
      new DriveDuration(drivetrain, 200, 0, 0.25, 0.25) // back away from amp
      ),
      new AutoRotateCommand(drivetrain, gyro, 0, 90), //face middle line 
      new DriveDuration(drivetrain, 1500, 0, null, null) //drive to middle line
      );
      //new IntakeCommandGroup(wrist, arm),
      //new IntakeClawCommand(claw, arm, 0)
  }
}
