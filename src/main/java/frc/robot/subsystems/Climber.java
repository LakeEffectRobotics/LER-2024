package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

    CANSparkMax leadClimbController;

    DoubleSolenoid shiftSolenoid;

    Gear currentGear;

    public Climber(CANSparkMax leadClimbController, DoubleSolenoid shiftSolenoid) {
        this.leadClimbController = leadClimbController;
        this.shiftSolenoid = shiftSolenoid;
    }

    public enum Gear {
        HIGH(DoubleSolenoid.Value.kForward),
        LOW(DoubleSolenoid.Value.kReverse),
        OFF(DoubleSolenoid.Value.kOff);

        private DoubleSolenoid.Value value;

        private Gear(DoubleSolenoid.Value value) {
            this.value = value;
        }

        public DoubleSolenoid.Value getValue() {
            return value;
        }
    }

    public void setOutput(double speed) {
        leadClimbController.set(speed);
    }

    public void stop() {
        leadClimbController.set(0);
    }

    public void setGear(Gear gear) {
        currentGear = gear;
        shiftSolenoid.set(gear.value);
    }

    public Gear getGear() {
        return currentGear;
    }

    @Override
    public void periodic() {
        // Periodic things
    }
}
