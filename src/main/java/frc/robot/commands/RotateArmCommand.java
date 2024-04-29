package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm.Arm;


public class RotateArmCommand extends Command { 
    Arm arm;
    Arm.ArmPosition pos;
    
    public RotateArmCommand(Arm arm,Arm.ArmPosition pos) {
        this.arm = arm;
        this.pos = pos;
    }
    @Override
    public void initialize(){ 
        System.out.println("ARM - ROTATION: rotation command initialized");
        
        System.out.println("rotate arm command initialize");
        switch (pos) {
            case INTAKE:
                    arm.rotateToIntakePosition();
                break;
            case TRAP:
                    arm.rotateToTrapPos();
                break;
            case AMP:
                    arm.rotateToAmpPos();
                break;
            case MIDDLE:
                    arm.rotateToMidPos(); 
                break;
            case CLIMB:
                    arm.rotateToClimbPos();
                break;
        
            default:
                break;
        }
        
    }

    @Override
    public void execute() {
       
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        System.out.println("ARM - ROTATION: command ended");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return arm.inPosition();
    }
}

