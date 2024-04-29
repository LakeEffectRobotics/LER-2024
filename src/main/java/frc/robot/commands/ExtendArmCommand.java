package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm.Arm;
import frc.robot.subsystems.Arm.Arm.ArmExtension;

public class ExtendArmCommand extends Command { 
    Arm arm;
    ArmExtension extend;
    
    public ExtendArmCommand(Arm arm,Arm.ArmExtension extend) {
        this.arm = arm;
        this.extend = extend;

        
    }

    @Override
    public void initialize(){
        System.out.println("ARM - EXTENSION: command initialized");
        if(extend == ArmExtension.EXTEND){
            arm.extendArm();
        }else{
            arm.retractArm();
        }
    }

    @Override
    public void execute() {
       
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        System.out.println("ARM - EXTENSION: command ended");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return arm.inPosition();
    }
}