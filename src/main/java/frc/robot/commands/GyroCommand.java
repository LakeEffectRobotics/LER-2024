package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Gyro;

public class GyroCommand extends Command {
    private boolean finished = false;

    Gyro gyro;

    public GyroCommand(Gyro gyro) {
        addRequirements(gyro);
        this.gyro = gyro;
    }

    @Override
    public void initialize() {
        System.out.println("this works");
    }

    @Override
    public void execute() {
        double current_angle = gyro.getAngle();
    	while (current_angle < 0) {	// convert gyro angle to angle within -180 to 180 range
    		current_angle += 360;
    	}
    	current_angle %= 360;
    	if (current_angle > 180) {
    		current_angle = -360 + current_angle;
    	}
        SmartDashboard.putNumber("Gyro", current_angle);
    }   

    @Override
    public boolean isFinished() {
        return finished;
    }

    public void end() {
    }   

    public void interrupted() {
    }

}