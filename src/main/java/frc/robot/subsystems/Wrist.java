package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.ControlType;
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
    private static final double kP = 0.6;
    private static final double kI = 0;

    private static final double kD = 0;
    private static final double MAX_OUTPUT = 0.15;
    private static final double MIN_OUTPUT = -0.85;

    private static final double MIN_ANGLE = 3;
    private static final double MAX_ANGLE = 130;

    // Function to convert from potentiometer volts to arm degrees above horizontal, obtained experimentally
    // Slope: degrees per volt
    // Constant: the degrees value at volts = 0
    private static final double VOLTS_TO_DEGREES_SLOPE = -69.2228;
    private static final double VOLTS_TO_DEGREES_CONSTANT = 220.528;

    // Motor voltage required to hold arm up at horizontal
    // 0.05 is the experimentally determined motor percentage that does that, so convert % to volts:
    private static final double GRAVITY_COMPENSATION = 0.04 * 12;

    public static  enum WristPosition{
        UP,
        DOWN
    }

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

    private GenericEntry targetAngleShuffle;
    private GenericEntry targetPotShuffle;
    
    private GenericEntry currentAngleShuffle;
    private GenericEntry currentPotShuffle;

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

        wristController.setClosedLoopRampRate(0.4);

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

    public double getCurrentAngle() {
        double potVoltage = pot.getPosition();
        return potVoltage * VOLTS_TO_DEGREES_SLOPE + VOLTS_TO_DEGREES_CONSTANT;
    }

    public double getTargetAngle() {
        return targetAngle;
    }

         /**
     * This is how we will know if the command isFinished.
     * 
     * @return true if we have achieved the desired angle
     */
    public boolean hasAchievedTargetAngle(){
        // Todo:  is the target angle close enough to the current angle?

        return false;  // obviously this needs to be real
    }
     /**
      *  just a note from the wrist class,  0 degrees is supposed to be horizontal.
      *  Todo:  If this is code lifed from last year, need to make sure that +'ve still makes it go in the direction we want
      * @param angle in degrees  A postive angle is going to move the wrist that many degrees above horizontal
      */
    public void setTargetAngle(double angle){
        if (angle <= MIN_ANGLE) {
            this.targetAngle = MIN_ANGLE;
        } else if (angle >= MAX_ANGLE) {
            this.targetAngle = MAX_ANGLE;
        } else {
            this.targetAngle = angle;
        }

        this.targetVolts = convertAngleToVolts(this.targetAngle);
        pidController.setReference(this.targetVolts, ControlType.kPosition);
    }
    
    public void moveWristUp(){
        // use setTarget angle to move the wrist up
        // setTargetAngle(25);   //start with 25 so we don't break anything
    }

    public void moveWristDown(){
        // use setTarget angle to move the wrist up
        // setTargetAngle(-10);   //start with 25 so we don't break anything
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

    public void setMotors(double speed) {
        wristController.set(speed);
    }

    @Override
    public void periodic() {
        // if wrist is dead kill motor just in case
        if (isWristDeadAgain){
            wristController.set(0);
        }

        currentAngleShuffle.setDouble(getCurrentAngle());
        currentPotShuffle.setDouble(pot.getPosition());

        targetAngleShuffle.setDouble(targetAngle);
        targetPotShuffle.setDouble(targetVolts);

        if(getCurrentAngle() <= 9 && this.targetAngle <= 9) {
            wristController.set(0);
        }
    }
    
}
