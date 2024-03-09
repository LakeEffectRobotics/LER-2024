package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAnalogSensor;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Wrist extends SubsystemBase {

    CANSparkMax wristController;

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

    // TODO make constants for wrist positions


    public boolean isWristDeadAgain = false;

    private ShuffleboardTab tab = Shuffleboard.getTab("my favourite tab");
    private GenericEntry wristDeadShuffle = tab
        .add("wrist dead?", "not quite!")
        .withPosition(6, 0)
        .getEntry();

    public Wrist(CANSparkMax wristController) {
        this.wristController = wristController;

        pot = wristController.getAnalog(SparkAnalogSensor.Mode.kAbsolute);

        pidController = wristController.getPIDController();
        pidController.setFeedbackDevice(pot);

        // set PID coefficients
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIAccum(0.05);
        pidController.setFF(kF);
        pidController.setOutputRange(MIN_OUTPUT, MAX_OUTPUT);

        // Initialize angle to where wrist is so it doesn't try to move on enable
        targetAngle = getCurrentAngle();
        targetVolts = convertAngleToVolts(targetAngle);
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
     * @param angle desired angle above horizontal (degrees)
     * @return pot position at this angle. this accounts for current arm angle
     */
    private double convertAngleToVolts(double angle) {
        // I hate algebra
        return angle * (1 / VOLTS_TO_DEGREES_SLOPE) - VOLTS_TO_DEGREES_CONSTANT * (1 / VOLTS_TO_DEGREES_SLOPE);
    }


    /**
     * toggle wrist deadness; makes certain things go into wrist dead mode
     */
    public void wristDead() {
        isWristDeadAgain = !isWristDeadAgain;
        if (isWristDeadAgain) {
            wristController.setIdleMode(IdleMode.kCoast);
            wristDeadShuffle.setString("perhaps D:");
        } else {
            wristController.setIdleMode(IdleMode.kBrake);
            wristDeadShuffle.setString("not quite!");
        }
    }

    @Override
    public void periodic() {
        // if wrist is dead kill motor just in case
        if (isWristDeadAgain){
            wristController.set(0);
        }
    }
    
}
