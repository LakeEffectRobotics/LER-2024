// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.instant.ShiftDownCommand;
import frc.robot.commands.instant.ShiftUpCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Drivetrain.Gear;

public class RobotContainer {

  public Drivetrain drivetrain = new Drivetrain(RobotMap.leftController1, RobotMap.rightController1, RobotMap.driveShitSolenoid);
  public Gyro gyro = new Gyro();

  public RobotContainer() {
    configureBindings();

    drivetrain.setDefaultCommand(new DriveCommand(drivetrain, OI.leftDriveSupplier, OI.rightDriveSupplier));
  }

  private void configureBindings() {

    /**
     * Commented to disable manual shifting because auto shifting doesn't care about your buttons
     */
    //OI.shiftUpButton.onTrue(new ShiftUpCommand(drivetrain));
    //OI.shiftDownButton.onTrue(new ShiftDownCommand(drivetrain));

    // Not requiring drivetrain because we dont want to interupt DriveCommand
    OI.lowShiftButton.whileTrue(Commands.runOnce(() -> {
      drivetrain.disableAutoShifting();
      drivetrain.setGear(Gear.LOW);
    })).onFalse(Commands.runOnce(() -> {
      drivetrain.enableAutoShifting();
    }));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
