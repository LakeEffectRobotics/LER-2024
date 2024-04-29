// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Arm.Arm;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Arm.Arm.ArmExtension;
import frc.robot.subsystems.Arm.Arm.ArmPosition;
import frc.robot.subsystems.Wrist.WristPosition;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class IntakeCommandGroup extends SequentialCommandGroup {
  /** Creates a new IntakeCommandGroup. */
  public IntakeCommandGroup(Wrist wrist, Arm arm) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    //commands to add
    /** 1. make sure wrist is up
     *  2. Lower arm
     *  3. extend arm
     *  4. wrist down.
     */
    addCommands(new WristCommand(wrist, WristPosition.UP),
                                new RotateArmCommand(arm,ArmPosition.INTAKE),
                                new ExtendArmCommand(arm, ArmExtension.EXTEND),
                                new WristCommand(wrist, WristPosition.INTAKE)
    );
  }
}
