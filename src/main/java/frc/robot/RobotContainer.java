// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.ArmCommand;
import frc.robot.commands.ClawCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.instant.ExtendArmCommand;
import frc.robot.commands.instant.RetractArmCommand;
import frc.robot.commands.instant.ShiftDownCommand;
import frc.robot.commands.instant.ShiftUpCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Drivetrain.Gear;

public class RobotContainer {

  public Drivetrain drivetrain = new Drivetrain(RobotMap.leftController1, RobotMap.rightController1, RobotMap.driveShitSolenoid);
  public Arm arm = new Arm(RobotMap.armPistonSolenoid, RobotMap.armController1);
  public Wrist wrist = new Wrist(RobotMap.wristController);
  public Claw claw = new Claw(RobotMap.clawController);

  public RobotContainer() {
    configureBindings();

    drivetrain.setDefaultCommand(new DriveCommand(drivetrain, OI.leftDriveSupplier, OI.rightDriveSupplier));
  }

  private void configureBindings() {
    //OI.shiftUpButton.onTrue(new ShiftUpCommand(drivetrain));
    //OI.shiftDownButton.onTrue(new ShiftDownCommand(drivetrain));

    // Not requiring drivetrain because we dont want to interupt DriveCommand
    OI.lowShiftButton.whileTrue(Commands.runOnce(() -> {
      drivetrain.disableAutoShifting();
      drivetrain.setGear(Gear.LOW);
    })).onFalse(Commands.runOnce(() -> { 
      drivetrain.enableAutoShifting();
    }));

    //OI.spinArmButton.whileTrue(new ArmCommand(arm, OI.spinArmSpeedSupplier.getAsDouble()));

    OI.spinOutClawButton.whileTrue(new ClawCommand(claw, OI.spinOutClawSpeedSupplier.getAsDouble()));
    OI.spinInClawButton.whileTrue(new ClawCommand(claw, -OI.spinInClawSpeedSupplier.getAsDouble()));

    OI.extendArmButton.onTrue(new ExtendArmCommand(arm));
    OI.retractArmButton.onTrue(new RetractArmCommand(arm));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
