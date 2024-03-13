package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private static double PREPARECLIMBROTATION = 0.0; //TODO: set this
    private static double CLIMBROTATION = 0.0; //TODO: also set this

    CANSparkMax leadClimbController;

    DoubleSolenoid shiftSolenoid;

    Gear currentGear;

    public Climber(CANSparkMax leadClimbController, DoubleSolenoid shiftSolenoid) {
        this.leadClimbController = leadClimbController;
        this.shiftSolenoid = shiftSolenoid;
        
        leadClimbController.getEncoder().setPosition(0); 
        setGear(Gear.LOW);
        setOutput(0);
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



    public void prepareClimb() {
        setGear(Gear.HIGH);
        leadClimbController.getEncoder().setPosition(PREPARECLIMBROTATION);
    } 

    public void climb() {
        setGear(Gear.HIGH);
        leadClimbController.getEncoder().setPosition(CLIMBROTATION);
        setGear(Gear.LOW);

    }

    @Override
    public void periodic() {
        // Periodic things
    }
}
