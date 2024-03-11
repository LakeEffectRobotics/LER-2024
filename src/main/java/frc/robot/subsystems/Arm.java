package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAnalogSensor;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {

    DoubleSolenoid armSolenoid;

    ArmPosition armCurrentPosition;
    CANSparkMax armLeadController;
    SparkAnalogSensor pot;
    SparkPIDController pidController; 
    
    //TODO change these
    
    private static final double kF = 0;
    private static final double kP = 0.4;
    private static final double kI = 0;

    private static final double kD = 0.5;
    private static final double MAX_OUTPUT = 0.5;
    private static final double MIN_OUTPUT = -0.2;

    private static final double MIN_ANGLE = -70;
    private static final double MAX_ANGLE = 122;

    // Function to convert from potentiometer volts to arm degrees above horizontal, obtained experimentally
    // Slope: degrees per volt
    // Constant: the degrees value at volts = 0
    private static final double VOLTS_TO_DEGREES_SLOPE = 70.5821;
    private static final double VOLTS_TO_DEGREES_CONSTANT = -106.185;

    // Motor voltage required to hold arm up at horizontal
    // 0.05 is the experimentally determined motor percentage that does that, so convert % to volts:
    private static final double GRAVITY_COMPENSATION = 0.04 * 12;

    // Target angle and volts
    // Angle is relative to horizontal, so volts accounts for arm angle
    private double targetAngle;
    private double targetVolts;

    private GenericEntry targetAngleShuffle;
    private GenericEntry targetPotShuffle;

    private GenericEntry currentAngleShuffle;
    private GenericEntry currentPotShuffle;

    // TODO make constants for wrist positions

    private ShuffleboardTab tab = Shuffleboard.getTab("thats my favourite tab too");

    private GenericEntry armOutShuffle = tab
        .add("arm out?", "nuh D':")
        .withPosition(6, 1)
        .getEntry();

    public Arm(DoubleSolenoid armSolenoid, CANSparkMax armLeadController) {
        this.armSolenoid = armSolenoid;
        this.armLeadController = armLeadController;

        armCurrentPosition = ArmPosition.RETRACT;

        pot = armLeadController.getAnalog(SparkAnalogSensor.Mode.kAbsolute);

        pidController = armLeadController.getPIDController();
        pidController.setFeedbackDevice(pot);

        // set PId coefficients
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIAccum(0.05);
        pidController.setFF(kF);
        pidController.setOutputRange(MIN_ANGLE, MAX_ANGLE);

        // Initialize angle to where wrist is so it doesn't try to move on enable
        targetAngle = getCurrentAngle();
        targetVolts = convertAngleToVolts(targetAngle);

        // add ramp rate because christine did it
        armLeadController.setClosedLoopRampRate(1);


        targetAngleShuffle = tab
            .add("wrist target angle", targetAngle)
            .withPosition(3, 0)
            .getEntry();

        targetPotShuffle = tab
            .add("wrist target volts", targetVolts)
            .withPosition(4, 0)
            .getEntry();

        currentAngleShuffle = tab
            .add("wrist angle", getCurrentAngle())
            .withPosition(3, 1)
            .getEntry();

        currentPotShuffle = tab
            .add("wrist pot volts", pot.getPosition())
            .withPosition(4, 1)
            .getEntry();
    }

    // Arm piston positions: up, down
    public enum ArmPosition {
        EXTEND(DoubleSolenoid.Value.kForward),
        RETRACT(DoubleSolenoid.Value.kReverse);
    
        private DoubleSolenoid.Value value;
    
        private ArmPosition(DoubleSolenoid.Value value) {
            this.value = value;
        }
    
        public DoubleSolenoid.Value getValue() {
            return value;
        }
    }

    public void extendArm() {
        armCurrentPosition = ArmPosition.EXTEND;

        armSolenoid.set(ArmPosition.EXTEND.value);

        armOutShuffle.setString("YEAH!");
    }

    public void retractArm() {
        armCurrentPosition = ArmPosition.RETRACT;

        armSolenoid.set(ArmPosition.RETRACT.value);

        armOutShuffle.setString("nuh D':");
    }

    public double getCurrentAngle() {
        double potVoltage = pot.getPosition();
        return potVoltage * VOLTS_TO_DEGREES_SLOPE + VOLTS_TO_DEGREES_CONSTANT;
    }

    public double getTargetAngle() {
        return targetAngle;
    }

    /**
     * 
     * @param angle desired degrees above horizontal
     */
    public void setTargetAngle(double angle) { // abc1239+10=21 road work ahead, i sure hope it does. David was here.......
        if (angle <= MIN_ANGLE) {
            this.targetAngle = MIN_ANGLE;
        } else if (angle >= MAX_ANGLE) {
            this.targetAngle = MAX_ANGLE;
        } else {
            this.targetAngle = angle;
        }

        this.targetVolts = convertAngleToVolts(this.targetAngle);
    }

    /**
     * 
     * @param angle desired angle above horizontal (degrees)
     * @return pot position at this angle. this accounts for current arm angle
     */
    private double convertAngleToVolts(double angle) {
        // I hate algebra
        return angle * (1 / VOLTS_TO_DEGREES_SLOPE) - VOLTS_TO_DEGREES_CONSTANT * (1 / VOLTS_TO_DEGREES_SLOPE);
    }

    /**
     * 
     * @return  motor volts needed to "cancel out" gravity: (Gravity compensation constant) * cos(current angle)
     */
    private double getArbitraryFeedforward() {
        return GRAVITY_COMPENSATION * Math.cos(Math.toRadians(getCurrentAngle()));
    }

    public void setMotors(double speed) {
        armLeadController.set(speed);
    }

    @Override
    public void periodic() {
        currentAngleShuffle.setDouble(getCurrentAngle());
        currentPotShuffle.setDouble(pot.getPosition());

        targetAngleShuffle.setDouble(targetAngle);
        targetPotShuffle.setDouble(targetVolts);
    }
    
}
