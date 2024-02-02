package frc.robot.commands.instant;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain.Gear;

public class ShiftUpCommand extends InstantCommand {

    private Drivetrain drivetrain;

    public ShiftUpCommand(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
        drivetrain.setGear(Gear.HIGH);
    }
    
}
