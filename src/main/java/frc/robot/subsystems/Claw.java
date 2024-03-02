package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Claw extends SubsystemBase {

    CANSparkMax clawController;

    public Claw(CANSparkMax clawController) {
        this.clawController = clawController;
    }

    public void setOutput(double speed) {
        clawController.set(speed);
    }

    public void stop() {
        clawController.set(0);
    }

    @Override
    public void periodic() {
        // Periodic things
    }
    
}
