package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAnalogSensor;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.SparkAnalogSensor.Mode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private static double PREPARECLIMBROTATION = 22.0; //TODO: set this
    private static double CLIMBROTATION = 5.0; //TODO: also set this
  

    CANSparkMax leadClimbController;
    SparkAnalogSensor pot;

    DoubleSolenoid shiftSolenoid;

    Gear currentGear;

    public Climber(CANSparkMax leadClimbController, DoubleSolenoid shiftSolenoid) {
        this.leadClimbController = leadClimbController;
        this.shiftSolenoid = shiftSolenoid;
        
        leadClimbController.getEncoder().setPosition(0); 
        pot = leadClimbController.getAnalog(Mode.kAbsolute);

        SparkPIDController climbController = leadClimbController.getPIDController();
        climbController.setFeedbackDevice(pot);
        // climbController.setP(0.1,0);

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

    public void setOutput(double speed) {// pid controller should be used instead
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
        System.out.println("Hooks out");
        setGear(Gear.HIGH);
        leadClimbController.setInverted(false);
        leadClimbController.getPIDController().setOutputRange(0.0, 0.1);
        leadClimbController.getPIDController().setReference(PREPARECLIMBROTATION, ControlType.kPosition,0);
    } 

    public void climb() {
        System.out.println("Going up");
        setGear(Gear.HIGH);
        leadClimbController.setInverted(true);
        leadClimbController.getPIDController().setOutputRange(0.0, 0.1);
        leadClimbController.getPIDController().setReference(CLIMBROTATION, ControlType.kPosition,0);

        setGear(Gear.LOW);

    }

    @Override
    public void periodic() {
        // Periodic things
        SmartDashboard.putNumber("Climber Pot", pot.getPosition());
    }
}
