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
    private static double PREPARECLIMBROTATION = 3.24; //TODO: set this
    private static double CLIMBROTATION = 3.08; //TODO: also set this
  

    // SPEEDS ARE POSITIVE, DIRECTION SET LATER
    private final double DOWN_SPEED = 0.5;
    private final double UP_SPEED = 0.25;
    private final double DEADZONE = 0.01;

    private Double setpoint = null;

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

        setpoint = pot.getPosition();
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
        setpoint = PREPARECLIMBROTATION;
        // leadClimbController.setInverted(false);
        // leadClimbController.getPIDController().setOutputRange(0.0, 0.1);
        // leadClimbController.getPIDController().setReference(PREPARECLIMBROTATION, ControlType.kPosition,0);
    } 

    public void climb() {
        System.out.println("Going up");
        setGear(Gear.HIGH);
        setpoint = CLIMBROTATION;
        // leadClimbController.setInverted(true);
        // leadClimbController.getPIDController().setOutputRange(0.0, 0.1);
        // leadClimbController.getPIDController().setReference(CLIMBROTATION, ControlType.kPosition,0);

        setGear(Gear.LOW);

    }

    @Override
    public void periodic() {
        double output = 0;

        
        if(setpoint == null) {
            leadClimbController.set(0);
        } 
        else {
            double error = pot.getPosition() - setpoint;
            if(error > 0 && Math.abs(error) > DEADZONE){
                leadClimbController.set(-DOWN_SPEED);
            } else if (error < 0 && Math.abs(error) > DEADZONE){
                leadClimbController.set(UP_SPEED);
            } else {
                leadClimbController.set(0);
            }
        } 

        // Periodic things
        SmartDashboard.putNumber("Climber Pot", pot.getPosition());
    }
}
