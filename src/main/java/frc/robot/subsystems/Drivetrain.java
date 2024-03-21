package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    CANSparkMax leftLeadController;
    CANSparkMax rightLeadController;

    DoubleSolenoid shiftSolenoid;

    Gear currentGear;

    boolean autoShifting;

    /**
     * Encoder to inches conversion factor
     * <pre>
     * Stage 3: 36:48
     * Encoder stage: 12:36
     * Wheel Diameter: 4"
     * </pre>
     */
    public static final double CONVERSION_FACTOR = 1/4.0 * 4 * Math.PI;
    /** Grayhill encoder on left side of drivetrain */
    public final RelativeEncoder leftDriveEncoder;
    /** Grayhill encoder on right side of drivetrain */
    public final RelativeEncoder rightDriveEncoder;

    public double maxSpeed;

    public Drivetrain(CANSparkMax leftLeadController, CANSparkMax rightLeadController, DoubleSolenoid shiftSolenoid) {
        this.leftLeadController = leftLeadController;
        this.rightLeadController = rightLeadController;
        this.shiftSolenoid = shiftSolenoid;

        leftDriveEncoder = leftLeadController.getAlternateEncoder(128 * 4);
        leftDriveEncoder.setPositionConversionFactor(CONVERSION_FACTOR);
        leftDriveEncoder.setInverted(true);
        leftDriveEncoder.setPosition(0);
        rightDriveEncoder = rightLeadController.getAlternateEncoder(64 * 4);
        rightDriveEncoder.setPositionConversionFactor(CONVERSION_FACTOR);
        rightDriveEncoder.setPosition(0);
        rightDriveEncoder.setInverted(false);   

        autoShifting = true;
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

    public void setOutput(double left, double right) {
        leftLeadController.set(left * maxSpeed);
        rightLeadController.set(-(right * maxSpeed)); 
    }

    public void stop() {
        leftLeadController.set(0);
        rightLeadController.set(0);
    }
 
    public void setGear(Gear gear) {
        currentGear = gear;
        shiftSolenoid.set(gear.value);
    }

    public Gear getGear() {
        return currentGear;
    }

    public void disableAutoShifting() {
        autoShifting = false;
    }

    public void enableAutoShifting() {
        autoShifting = true;
    }

    /**
     * Pulls speed to figure out when to shift gear
     * MUST BE RUN IN COMMAND excute()
     */
    public void shiftGears() {

        if(!autoShifting) return;

        double UPSHIFT_SPEED = 1400;
        double DOWNSHIFT_SPEED = 1000;

        double currentSpeed = (leftDriveEncoder.getVelocity() + rightDriveEncoder.getVelocity()) / 2;

        SmartDashboard.putNumber("Avg Speed", currentSpeed);

        SmartDashboard.putString("Gear", String.valueOf(currentGear));
    
        if(currentSpeed >= UPSHIFT_SPEED && currentGear == Gear.LOW) {
            currentGear = Gear.HIGH;
            shiftSolenoid.set(Gear.HIGH.value);
            maxSpeed = 0.6;
        }

        if(currentSpeed <= DOWNSHIFT_SPEED) {
            currentGear = Gear.LOW;
            shiftSolenoid.set(Gear.LOW.value);
        }
    }

    @Override
    public void periodic() {
        if(maxSpeed < 1) {
            maxSpeed = maxSpeed + 0.01;
        }

        SmartDashboard.putNumber("Max Speed", maxSpeed);
    }
}