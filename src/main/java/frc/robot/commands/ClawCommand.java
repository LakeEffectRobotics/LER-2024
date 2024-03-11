package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Claw;

public class ClawCommand extends Command {

    Claw claw;
    double speed;

    public ClawCommand(Claw claw, double speed) {
        this.claw = claw;
        this.speed = speed;
    }

    @Override
    public void execute() {
        claw.setOutput(speed);
    }
    
}
