package frc.robot.subsystems;
//using UnityEngine;

import javax.swing.plaf.basic.BasicLookAndFeel;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAnalogSensor;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// public class PlayerController:MonoBehaviour{

// }
public class Arm extends SubsystemBase {

    DoubleSolenoid armSolenoid;

    ArmExtension armCurrentPosition;
    CANSparkMax armLeadController;
    SparkAnalogSensor pot;
    SparkPIDController pidController; 
    
    //TODO change these
    private static final long PISTON_TRAVEL_TIME = 50;

    private static final double kF = 0;
    private static final double kP = 0.3;
    private static final double kI = 0;

    private static final double kD = 0;
    private static final double MAX_OUTPUT = 0.75;
    private static final double MIN_OUTPUT = -0.25;

    private static final double MIN_ANGLE = 0.5;
    private static final double MAX_ANGLE = 110;

    private static final int ARM_DEADZONE = 10;

    // Function to convert from potentiometer volts to arm degrees above horizontal, obtained experimentally
    // Slope: degrees per volt
    // Constant: the degrees value at volts = 0
    private static final double VOLTS_TO_DEGREES_SLOPE = 31.9036;
    private static final double VOLTS_TO_DEGREES_CONSTANT = 0.925204;

    // Motor voltage required to hold arm up at horizontal
    // 0.05 is the experimentally determined motor percentage that does that, so convert % to volts:
    private static final double GRAVITY_COMPENSATION = 0.04 * 12;

    public static enum ArmPosition {
        AMP,
        TRAP,
        INTAKE,
        MIDDLE
    }

    private long armTimeout = 0;

    public boolean armExtended = false;

    // Target angle and volts
    // Angle is relative to horizontal, so volts accounts for arm angle
    private double targetAngle;
    private double targetVolts;

    private GenericEntry targetAngleShuffle;
    private GenericEntry targetPotShuffle;

    private GenericEntry currentAngleShuffle;
    private GenericEntry currentPotShuffle;

    private GenericEntry armMotorModeShuffle;

    private ShuffleboardTab tab = Shuffleboard.getTab("thats my favourite tab too");

    private GenericEntry armOutShuffle = tab
        .add("arm out?", "nuh D':")
        .withPosition(6, 1)
        .getEntry();

    public Arm(DoubleSolenoid armSolenoid, CANSparkMax armLeadController) {
        this.armSolenoid = armSolenoid;
        this.armLeadController = armLeadController;

        armCurrentPosition = ArmExtension.RETRACT;

        pot = armLeadController.getAnalog(SparkAnalogSensor.Mode.kAbsolute);

        pidController = armLeadController.getPIDController();
        pidController.setFeedbackDevice(pot);

        // set PId coefficients
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIAccum(0.05);
        pidController.setFF(kF);
        pidController.setOutputRange(MIN_OUTPUT, MAX_OUTPUT);

        // Initialize angle to where wrist is so it doesn't try to move on enable
        targetAngle = getCurrentAngle();
        targetVolts = convertAngleToVolts(targetAngle);

        // add ramp rate because christine did it
        armLeadController.setClosedLoopRampRate(1);


        targetAngleShuffle = tab
            .add("arm target angle", targetAngle)
            .withPosition(3, 2)
            .getEntry();

        targetPotShuffle = tab
            .add("arm target volts", targetVolts)
            .withPosition(4, 2)
            .getEntry();

        currentAngleShuffle = tab
            .add("arm angle", getCurrentAngle())
            .withPosition(3, 3)
            .getEntry();

        currentPotShuffle = tab
            .add("arm pot volts", pot.getPosition())
            .withPosition(4, 3)
            .getEntry();

        armMotorModeShuffle = tab
            .add("arm mode", getArmMotorMode())
            .withPosition(4, 4)
            .getEntry();
    }

    // Arm piston positions: up, down
    public enum ArmExtension {
        EXTEND(DoubleSolenoid.Value.kForward),
        RETRACT(DoubleSolenoid.Value.kReverse);
    
        private DoubleSolenoid.Value value;
    
        private ArmExtension(DoubleSolenoid.Value value) {
            this.value = value;
        }
    
        public DoubleSolenoid.Value getValue() {
            return value;
        }
    }

    public void extendArm() {
        armCurrentPosition = ArmExtension.EXTEND;

        armSolenoid.set(ArmExtension.EXTEND.value);
        armTimeout = System.currentTimeMillis()+PISTON_TRAVEL_TIME;
        armExtended = true;
        armOutShuffle.setString("OF COURSE!");
    }

    public void retractArm() {
        armCurrentPosition = ArmExtension.RETRACT;

        armSolenoid.set(ArmExtension.RETRACT.value);
        armTimeout = System.currentTimeMillis()+PISTON_TRAVEL_TIME;

        armExtended = false;
        armOutShuffle.setString("nuh D':");
    }

    public void toggleArm() {
        if(armExtended == true) {
            extendArm();
        } else if(armExtended == false) {
            retractArm();
        }
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
     * A friendly way for commands to make the subsystem move to the intake position
     */
    public void rotateToIntakePosition()
    {
        setTargetAngle(0.5);
    }

    public void rotateToAmpPos(){
        setTargetAngle(105);
    }

    public void rotateToTrapPos(){
        setTargetAngle(90);
    }

    public void rotateToMidPos() {
        setTargetAngle(45);
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

    public String getArmMotorMode() {
        if(armLeadController.getIdleMode() == IdleMode.kBrake) {
            return "brake mode (good)";
        } else {
            return "coast mode (fall)";
        }
    }

    /**
     * A helper function to let the command know when the Arm has finished its movement
     */
    public boolean inPosition(){
        return Math.abs(getTargetAngle()-getCurrentAngle())<ARM_DEADZONE && System.currentTimeMillis()>armTimeout;
    }

    public boolean aboveMiddle() {
        return getCurrentAngle()>15
        ;
    }

    @Override
    public void periodic() {
        currentAngleShuffle.setDouble(getCurrentAngle());
        currentPotShuffle.setDouble(pot.getPosition());

        targetAngleShuffle.setDouble(targetAngle);
        targetPotShuffle.setDouble(targetVolts);

        armMotorModeShuffle.setString(getArmMotorMode());


        if(getCurrentAngle() <= 5 && this.targetAngle <= 5) {
            armLeadController.set(0);
            // GetComponent.transform.position;
            // Vector3 direction = new vector3 horizontalinput, of,
            // verticalinput).normalized;

        } else {
            pidController.setReference(targetVolts, ControlType.kPosition, 0, getArbitraryFeedforward());
        } 
        
        // if(getCurrentAngle() <= 45) {
        //     armLeadController.setIdleMode(IdleMode.kBrake);
        // } else {
        //     armLeadController.setIdleMode(IdleMode.kBrake);
        // }
    }
    
}