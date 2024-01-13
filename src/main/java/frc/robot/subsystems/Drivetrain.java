package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    CANSparkMax leftLeadController;
    CANSparkMax rightLeadController;

    public Drivetrain(CANSparkMax leftLeadController, CANSparkMax rightLeadController) {
        this.leftLeadController = leftLeadController;
        this.rightLeadController = rightLeadController;
    }

    public void setOutput(double left, double right) {
        leftLeadController.set(-left);
        rightLeadController.set(right);
    }

    public void stop() {
        leftLeadController.set(0);
        rightLeadController.set(0);
    }
    
}
