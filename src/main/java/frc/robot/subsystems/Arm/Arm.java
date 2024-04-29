package frc.robot.subsystems.Arm;
//using UnityEngine;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAnalogSensor;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

// public class PlayerController:MonoBehaviour{

// }
public class Arm extends SubsystemBase {

    ArmIO io;

    ArmExtension armCurrentPosition;

    //TODO change these
    static final long PISTON_TRAVEL_TIME = 50;

    static final double kF = 0;
    static final double kP = 0.3;
    static final double kI = 0;

    static final double kD = 0;
    static final double MAX_OUTPUT = 0.75;
    static final double MIN_OUTPUT = -0.25;

    static final double MIN_ANGLE = 0.5;
    static final double MAX_ANGLE = 110;

    static final int ARM_DEADZONE = 10;

    // Function to convert from potentiometer volts to arm degrees above horizontal, obtained experimentally
    // Slope: degrees per volt
    // Constant: the degrees value at volts = 0

    ///////////// y_{1}\sim mx_{1}+b /////////////
    private static final double VOLTS_TO_DEGREES_SLOPE = 34.078;
    private static final double VOLTS_TO_DEGREES_CONSTANT = -0.988;

    // Motor voltage required to hold arm up at horizontal
    // 0.05 is the experimentally determined motor percentage that does that, so convert % to volts:
    private static final double GRAVITY_COMPENSATION = 0.04 * 12;

    public static enum ArmPosition {
        AMP,
        TRAP,
        INTAKE,
        MIDDLE,
        CLIMB
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

    public MechanismLigament2d drawing;

    private ShuffleboardTab tab = Shuffleboard.getTab("thats my favourite tab too");

    private GenericEntry armOutShuffle = tab
        .add("arm out?", "nuh D':")
        .withPosition(6, 1)
        .getEntry();

    public Arm(DoubleSolenoid armSolenoid, CANSparkMax armLeadController) {
        
        io = Robot.isReal() ? new ArmReal(armSolenoid, armLeadController, this) : new ArmSim(this);

        armCurrentPosition = ArmExtension.RETRACT;

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
            .add("arm pot volts", io.getPotVolts())
            .withPosition(4, 3)
            .getEntry();

        armMotorModeShuffle = tab
            .add("arm mode", getArmMotorMode())
            .withPosition(4, 4)
            .getEntry();

        drawing = new MechanismLigament2d("Arm", 1.5, 0);
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

        io.setPiston(ArmExtension.EXTEND);
        armTimeout = System.currentTimeMillis()+PISTON_TRAVEL_TIME;
        armExtended = true;
        armOutShuffle.setString("OF COURSE!");
    }

    public void retractArm() {
        armCurrentPosition = ArmExtension.RETRACT;

        io.setPiston(ArmExtension.EXTEND);
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
        double potVoltage = io.getPotVolts();
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
        
        System.out.println("TARGET TO " + angle);
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

    public void rotateToClimbPos() {
        setTargetAngle(80);
    }

    /**
     * 
     * @param angle desired angle above horizontal (degrees)
     * @return pot position at this angle. this accounts for current arm angle
     */
    double convertAngleToVolts(double angle) {
        // I hate algebra
        return angle * (1 / VOLTS_TO_DEGREES_SLOPE) - VOLTS_TO_DEGREES_CONSTANT * (1 / VOLTS_TO_DEGREES_SLOPE);
    }

    /**
     * 
     * @return  motor volts needed to "cancel out" gravity: (Gravity compensation constant) * cos(current angle)
     */
    double getArbitraryFeedforward() {
        return GRAVITY_COMPENSATION * Math.cos(Math.toRadians(getCurrentAngle()));
    }

    public void setMotors(double speed) {
        io.setPercent(speed);
    }

    public String getArmMotorMode() {
        if(io.getIdleMode() == IdleMode.kBrake) {
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

        io.periodic();

        currentAngleShuffle.setDouble(getCurrentAngle());
        currentPotShuffle.setDouble(io.getPotVolts());

        targetAngleShuffle.setDouble(targetAngle);
        targetPotShuffle.setDouble(targetVolts);

        armMotorModeShuffle.setString(getArmMotorMode());


        drawing.setAngle(getCurrentAngle());
        drawing.setLength(armExtended ? 1.5 : 1.25);

        if(getCurrentAngle() <= 5 && this.targetAngle <= 5) {
            io.setPercent(0);
            // GetComponent.transform.position;
            // Vector3 direction = new vector3 horizontalinput, of,
            // verticalinput).normalized;

        } else {
            io.setPosition(targetVolts);
        } 
        
        // if(getCurrentAngle() <= 45) {
        //     armLeadController.setIdleMode(IdleMode.kBrake);
        // } else {
        //     armLeadController.setIdleMode(IdleMode.kBrake);
        // }
    }
    
}