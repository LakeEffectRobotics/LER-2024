package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.Tools;

/**
 *
 */
public class Gyro extends SubsystemBase {
	@SuppressWarnings("unused")
	private double offset_per_second = 0;
	private double[] sample_1 = {0, 1};
	private boolean first = true;
	@SuppressWarnings("unused")
	private double reset_time = 0;
	private double prev_sample;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    }
    
    public void calibrate() {
    	RobotMap.gyro.zeroYaw(); // TODO make it so it actually zeros instead of zeroing just yaw
    	sample_1[0] = Tools.getTimeSeconds();
    	sample_1[1] = getAngle();
    	
    }

    public void reset() {
        RobotMap.gyro.reset();
    }

    public Rotation2d getRotation2d() {
        return RobotMap.gyro.getRotation2d();
    }
    
    public void resetAngle() {
    	if (first) {
    		offset_per_second = (getAngle() - sample_1[1]) / (Tools.getTimeSeconds() - sample_1[0]);
    		first = false;
    	}
    	reset_time = Tools.getTimeSeconds();
    	prev_sample = -RobotMap.gyro.getAngle();
    }
    
    public double getAngle() {
        return -RobotMap.gyro.getAngle() - prev_sample;// - (Tools.getTimeSeconds() - reset_time) * offset_per_second; // it is negative to make counterclockwise motion increase the angle like normal math
    }

	public double getPitch() {
		return RobotMap.gyro.getRoll();
	}
    
    public double getAbsoluteAngle() {
    	return -RobotMap.gyro.getAngle();
    }
    
    public double[] getStraightOutput(double l, double r) {
    	final double ANGLE_TOLERANCE = 1;
    	double k_p = 0.01;
    	double l_out = l;
    	double r_out = r;
    	double current_angle = getAngle();
        	
    	if (Math.abs(current_angle) > ANGLE_TOLERANCE) {	// adjust values to compensate for turning drift	
    		double modifier = current_angle * k_p;	// make amount of correction proportional to angle
    		l_out += modifier;
	    	r_out -= modifier;
    	}
    	
    	l_out = l_out > 1 ? 1 : l_out;	// ensure values are within -1 to 1 range
    	l_out = l_out < -1 ? -1 : l_out;
    	r_out = r_out > 1 ? 1 : r_out;
    	r_out = r_out < -1 ? -1 : r_out;
    	
    	return new double[] {l_out, r_out};
    }
    
    public double[] getStraightOutput(double l, double r, double target) {
    	final double ANGLE_TOLERANCE = 1;
    	double k_p = 0.03;
    	double l_out = l;
    	double r_out = r;
    	double current_angle = getAbsoluteAngle() - target;
        	
    	if (Math.abs(current_angle) > ANGLE_TOLERANCE) {	// adjust values to compensate for turning drift	
    		double modifier = current_angle * k_p;	// make amount of correction proportional to angle
    		l_out += modifier;
	    	r_out -= modifier;
    	}
    	
    	l_out = l_out > 1 ? 1 : l_out;	// ensure values are within -1 to 1 range
    	l_out = l_out < -1 ? -1 : l_out;
    	r_out = r_out > 1 ? 1 : r_out;
    	r_out = r_out < -1 ? -1 : r_out;
    	
    	return new double[] {l_out, r_out};
    	
    }
    
	public double[] getArcadeGyroOutput(double x, double y) {
    	if (x == 0 && y == 0) {
        	return new double[] {0, 0};
    	}
    	
    	double joystick_speed;
    	double joystick_angle;
    	double current_angle;
    	double angle_difference;
    	final double MAX_ARCADE_SPEED = 0.8;
    	final double FORWARD_THRESHOLD = 40;
    	
    	double l_gyro_out = 0;
    	double r_gyro_out = 0;
    	
    	joystick_speed = Math.abs(x) > Math.abs(y) ? Math.abs(x) : Math.abs(y);	// use maximum axis to create speed
    	joystick_speed = Math.abs(joystick_speed) > Math.abs(MAX_ARCADE_SPEED) ? MAX_ARCADE_SPEED : joystick_speed; // ensure speed is not over the limit
    	
    	joystick_angle = -Math.toDegrees(Math.atan2(y, x)) - 90; // convert x y to angle within -180 to 180 range
    	if (joystick_angle < -180) {
    		joystick_angle = 360 + joystick_angle;
    	}
    	
    	current_angle = getAngle();
    	while (current_angle < 0) {	// convert gyro angle to angle within -180 to 180 range
    		current_angle += 360;
    	}
    	current_angle %= 360;
    	if (current_angle > 180) {
    		current_angle = -360 + current_angle;
    	}
    	
    	angle_difference = current_angle - joystick_angle;
    	if (angle_difference < -180) {
    		angle_difference = 360 + angle_difference;
    	}
    	else if (angle_difference > 180) {
    		angle_difference -= 360;
    	}
    	
    	if (angle_difference > FORWARD_THRESHOLD) {
    		l_gyro_out = joystick_speed;
    		r_gyro_out = -joystick_speed;
    	}
    	else if (angle_difference < -FORWARD_THRESHOLD) {
    		l_gyro_out = -joystick_speed;
    		r_gyro_out = joystick_speed;
    	}
    	else if (angle_difference < FORWARD_THRESHOLD && angle_difference > 0) { // rotate clockwise
    		l_gyro_out = joystick_speed;
    		r_gyro_out = (joystick_speed - angle_difference / FORWARD_THRESHOLD * joystick_speed * 2);
    	}
    	else if (angle_difference > -FORWARD_THRESHOLD && angle_difference < 0) { // rotate counterclockwise
    		l_gyro_out = (joystick_speed + angle_difference / FORWARD_THRESHOLD * joystick_speed * 2);
    		r_gyro_out = joystick_speed;
    	}
    	
    	return new double[] {l_gyro_out, r_gyro_out};
	}
}