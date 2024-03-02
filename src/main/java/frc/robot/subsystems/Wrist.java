package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAnalogSensor;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Wrist extends SubsystemBase {

    CANSparkMax wristController;

    SparkAnalogSensor pot;

    SparkPIDController pidController;   

    public Wrist(CANSparkMax wristController) {
        this.wristController = wristController;

        pot = wristController.getAnalog(SparkAnalogSensor.Mode.kAbsolute);

        pidController = wristController.getPIDController();
        pidController.setFeedbackDevice(pot);
    }
    
}
