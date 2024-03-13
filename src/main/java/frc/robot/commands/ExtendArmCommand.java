package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmExtension;

public class ExtendArmCommand extends Command { 
    Arm arm;
    ArmExtension extend;
    
    public ExtendArmCommand(Arm arm,Arm.ArmExtension extend) {
        this.arm = arm;
        this.extend = extend;

        
    }

    @Override
    public void initialize(){
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
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return arm.inPosition();
    }
}