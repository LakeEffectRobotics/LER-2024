// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Arm.ArmExtension;
import frc.robot.subsystems.Arm.ArmPosition;
import frc.robot.subsystems.Wrist.WristPosition;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AmpCommandGroup extends SequentialCommandGroup {
  /** Creates a new IntakeCommandGroup. */
  public AmpCommandGroup(Wrist wrist, Arm arm) {
    ParallelCommandGroup extendRotate = new ParallelCommandGroup( new ExtendArmCommand(arm, ArmExtension.RETRACT),
                                                                  new RotateArmCommand(arm,ArmPosition.AMP));

    Command positionClaw = new WaitUntilCommand(arm :: aboveMiddle).andThen(new WristCommand(wrist, WristPosition.AMP));


    addCommands(
      new WristCommand(wrist, WristPosition.UP),
      extendRotate.alongWith(positionClaw)
    );
  }
}
