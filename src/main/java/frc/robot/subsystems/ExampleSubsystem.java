package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ExampleSubsystem extends SubsystemBase {

    CANSparkMax motorLeadController;

    /**
     * Encoder to inches conversion factor
     * 
     * <pre>
     * Stage 3: 36:48
     * Encoder stage: 12:36
     * Wheel Diameter: 4"
     * </pre>
     */

    public double maxSpeed;

    private RelativeEncoder motorEncoder;

    public ExampleSubsystem(CANSparkMax motorController) {
        this.motorLeadController = motorController;

        motorEncoder.setPosition(0);
    }

    public void setOutput(double left) {
        maxSpeed = SmartDashboard.getNumber("drivetrain limiter", 0.0);
        motorLeadController.set(left);

    }

    public void stop() {
        motorLeadController.set(0);

    }

    @Override
    public void periodic() {
        if (maxSpeed < 1) {
            maxSpeed = maxSpeed + 0.01;
        }
    }
}