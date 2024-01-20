package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class DriveCommand extends Command {

    Drivetrain drivetrain;

    DoubleSupplier leftSupplier;
    DoubleSupplier rightSupplier;
    
    public DriveCommand(Drivetrain drivetrain, DoubleSupplier leftSupplier, DoubleSupplier rightSupplier) {
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
        this.leftSupplier = leftSupplier;
        this.rightSupplier = rightSupplier;
    }

    @Override
    public void initialize() {
        drivetrain.stop();
    }

    @Override
    public void execute() {
        drivetrain.setOutput(leftSupplier.getAsDouble(), rightSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
