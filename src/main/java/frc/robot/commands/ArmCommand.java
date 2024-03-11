package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;

public class ArmCommand extends Command { 
    Arm arm;
    double speed;
    
    public ArmCommand(Arm arm, double speed) {
        this.arm = arm;
        this.speed = speed;
    }

    @Override
    public void execute() {
        arm.setMotors(speed);
    }
}
