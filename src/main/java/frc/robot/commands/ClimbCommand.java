package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Climber.Gear;

public class ClimbCommand extends Command {

    double speed;

Climber climber;
    public ClimbCommand(Climber climber, double speed) {
        this.climber = climber;
        this.speed = speed;
        
    }

    @Override
    public void initialize() {
        climber.setOutput(speed);
        if(speed >= 0) {
            climber.setGear(Gear.HIGH);
        }
        System.out.println("CLIMBER - Command initialized");
    }
    
    @Override
    public void execute() {
        climber.setOutput(speed);
    }

    @Override
    public void end(boolean isInterrupted) {
        if(speed >= 0) {
            climber.setGear(Gear.LOW);
        }
        System.out.println("CLIMBER - Command Done");
        climber.setOutput(0);
    }
}
