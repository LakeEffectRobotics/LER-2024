package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class DriveForDuration extends Command {

    Drivetrain drivetrain;
    private Long timeout;

    public DriveForDuration(Drivetrain drivetrain) {
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
    }
    @Override
    public void initialize() {
        timeout = System.currentTimeMillis()+2500;
    }

    @Override
    public void execute() {
        drivetrain.setOutput(-0.25, -0.25);
    }

    @Override
    public void end(boolean isInterrupted){
        drivetrain.setOutput(0, 0);
    }

    public boolean isFinished() {
        return System.currentTimeMillis()>=timeout;
    }
}
