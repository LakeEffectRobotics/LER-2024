// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.AmpCommandGroup;
import frc.robot.commands.ArmPrepareClimbGroup;
import frc.robot.commands.ClimbCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.ExtendArmCommand;
import frc.robot.commands.FastShoot;
import frc.robot.commands.IntakeCommandGroup;
import frc.robot.commands.TransportCommandGroup;
import frc.robot.commands.TrapCommandGroup;
import frc.robot.commands.autonomous.AutoAmpBlue;
import frc.robot.commands.autonomous.AutoAmpPickup;
import frc.robot.commands.autonomous.AutoAmpRed;
import frc.robot.commands.autonomous.AutoPickup;
import frc.robot.commands.autonomous.AutoPickupLong;
import frc.robot.commands.autonomous.AutoRotateCommand;
import frc.robot.commands.autonomous.DriveDuration;
import frc.robot.commands.autonomous.Intake;
import frc.robot.commands.instant.IntakeClawCommand;
import frc.robot.commands.instant.ShootClawCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Arm.ArmExtension;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Drivetrain.Gear;

public class RobotContainer {
  public final Compressor compressor = new Compressor(PneumaticsModuleType.REVPH); 

  public Drivetrain drivetrain = new Drivetrain(RobotMap.leftController1, RobotMap.rightController1, RobotMap.driveShitSolenoid);
  public Arm arm = new Arm(RobotMap.armPistonSolenoid, RobotMap.armController1);
  public Wrist wrist = new Wrist(RobotMap.wristController);
  public Claw claw = new Claw(RobotMap.clawController);
  public Climber climber = new Climber(RobotMap.climbController1,RobotMap.climbShiftSolenoid);
  public Gyro gyro = new Gyro();
  public Intake intake = new Intake();

  /* auto selection */
  public final String[] kAutos = {"0: none", "1: drive", "2: pickup", "3: spin", "4: amp! (blue)", "5: amp! (red)", "6: pickup (white line)", "7: amp + pickup (white line)"}; //list of autos
  public final String kDefaultAuto = kAutos[0];
  public static String m_autoSelected;
  public SendableChooser<String> m_chooser = new SendableChooser<>();

  public RobotContainer() {
    compressor.enableAnalog(90, 120);

    configureBindings();

    drivetrain.setDefaultCommand(new DriveCommand(drivetrain, OI.leftDriveSupplier, OI.rightDriveSupplier));
 
    
    /* auto selection */
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto); //set default
    for(String auto :kAutos) { //get lim_robotContainerst from robotcontainer
      m_chooser.addOption(auto, auto);
    }
    SmartDashboard.putData("AutoChoices", m_chooser);
  }

  /* operator controller toggle */
  

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

    OI.ClimbUpManualButton.whileTrue(new ClimbCommand(climber, climber.UP_SPEED));
    OI.ClimbDownManualButton.whileTrue(new ClimbCommand(climber, climber.DOWN_SPEED));

    // OI.ExtendArmButton.onTrue(new ExtendArmCommand(arm, ArmExtension.EXTEND));
    // OI.RetractArmButton.onTrue(new ExtendArmCommand(arm, ArmExtension.RETRACT));

    //OI.spinArmButton.whileTrue(new ArmCommand(arm, OI.spinArmSpeedSupplier.getAsDouble()));

    /* Operator Bindings */
    OI.spinOutClawButton.whileTrue(new ShootClawCommand(claw));
    OI.spinInClawButton.whileTrue(new IntakeClawCommand(claw, arm, OI.spinInClawSpeedSupplier.getAsDouble()));

    // OI.extendArmButton.onTrue(Commands.runOnce(() -> {
    //   wrist.setTargetAngle(100);
    // }));
    // OI.retractArmButton.onTrue(Commands.runOnce(() -> {
    //   wrist.setTargetAngle(5);
    // }));
    OI.intakePositionButton.onTrue(new IntakeCommandGroup(wrist, arm));
    OI.transportPositionButton.onTrue(new TransportCommandGroup(wrist, arm));
    OI.ampPositionButton.onTrue(new AmpCommandGroup(wrist, arm));
    // OI.trapPositionButton.onTrue(new TrapCommandGroup(wrist, arm));
    // OI.prepareClimbButton.whileTrue(new ClimbCommand(climber, climber.UP_SPEED));
    OI.climbButton.whileTrue(new ClimbCommand(climber, climber.DOWN_SPEED));

    OI.shootFastButton.whileTrue(new FastShoot(claw));
    // OI.armPrepareClimbButton.onTrue(new ArmPrepareClimbGroup(wrist, arm)); //switch between guitar and xbox

    OI.armUpButton.onTrue(Commands.runOnce(() -> arm.setTargetAngle(90)));
    OI.armDownButton.onTrue(Commands.runOnce(() -> arm.setTargetAngle(0)));
  }

  public Command getAutonomousCommand() {
    String auto = m_chooser.getSelected();
    System.out.println("SELECTED AUTO "+auto);
    if(auto == kAutos[0]) { //none
      return null;
    } else if (auto == kAutos[1]) { //drive
      System.out.println("DOING DRIVE AUTO");
      return new DriveDuration(drivetrain, 2500, 0, null, null);
    } else if (auto == kAutos[2]) {
      System.out.println("DOING PICKUP AUTO");
      return new AutoPickup(drivetrain, intake, arm, wrist, claw, 0);
    } else if(auto == kAutos[3]) {
      System.out.println("DOING SPIN AUTO");
      return new AutoRotateCommand(drivetrain, gyro, 0, 1800);
    } else if(auto == kAutos[4]) {
      System.out.println("DOING AMP BLUE AUTO");
      return new AutoAmpBlue(drivetrain, gyro, wrist, arm, claw);
    } else if(auto == kAutos[5]) {
      System.out.println("DOING AMP RED AUTO");
      return new AutoAmpRed(drivetrain, gyro, wrist, arm, claw);
    } else if(auto == kAutos[6]) {
      System.out.println("DOING WHITE LINE PICKUP AUTO");
      return new AutoPickupLong(drivetrain, intake, arm, wrist, claw, 0);
    } else if (auto== kAutos[7]) {
      System.out.println(" DOING AMP AND PICKUP FROM WHITE LINE AUTO");
      return new AutoAmpPickup(drivetrain, gyro, wrist, arm, claw);
    }else {
      System.out.println("DOING NOTHING :(");
      return null;
    }
  }
}
