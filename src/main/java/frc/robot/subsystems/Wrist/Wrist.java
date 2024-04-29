package frc.robot.subsystems.Wrist;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class Wrist extends SubsystemBase {

    private WristIO io;

    //TODO change these
    
    static final double kF = 0;
    static final double kP = 0.06;
    static final double kI = 0;

    static final double kD = 0;
    static final double MAX_OUTPUT = 0.85;
    static final double MIN_OUTPUT = -0.4;

    static final double MIN_ANGLE = 3;
    static final double MAX_ANGLE = 130;

    private static final double WRIST_DEADZONE = 7;
    // Function to convert from potentiometer volts to arm degrees above horizontal, obtained experimentally
    // Slope: degrees per volt
    // Constant: the degrees value at volts = 0
    private static final double VOLTS_TO_DEGREES_SLOPE = 5.9999;
    private static final double VOLTS_TO_DEGREES_CONSTANT = 119.999;
    // Motor voltage required to hold arm up at horizontal
    // 0.05 is the experimentally determined motor percentage that does that, so convert % to volts:
    private static final double GRAVITY_COMPENSATION = 0.04 * 12;

    public static  enum WristPosition{
        UP,
        INTAKE,
        AMP,
        TRAP,
        CLIMB
    }

    private WristPosition commandedPosition = WristPosition.INTAKE;

    // Target angle and volts
    // Angle is relative to horizontal, so volts accounts for arm angle
    private double targetAngle;
    private double targetVolts;

    // TODO make constants for wrist positions


    public boolean isWristDeadAgain = false;

    private ShuffleboardTab tab = Shuffleboard.getTab("thats my favourite tab too");
    private GenericEntry wristDeadShuffle = tab
        .add("wrist dead?", "not quite!")
        .withPosition(6, 0)
        .getEntry();

    private GenericEntry targetAngleShuffle;
    private GenericEntry targetPotShuffle;
    
    private GenericEntry currentAngleShuffle;
    private GenericEntry currentPotShuffle;

    private GenericEntry wristMotorModeShuffle;

    public Wrist(CANSparkMax wristController) {
        
        io = Robot.isReal() ? new WristReal(wristController, this) : new WristSim(wristController, this);

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
            .add("wrist pot volts", wristController.getEncoder().getPosition())
            .withPosition(4, 1)
            .getEntry();

        wristMotorModeShuffle = tab
            .add("wrist mode", getWristMotorMode())
            .withPosition(4, 2)
            .getEntry();
    }

    public double getCurrentAngle() {
        // double potVoltage = pot.getPosition();
        double motorEncoderPosition = io.getEncoderPosition();
        return motorEncoderPosition * VOLTS_TO_DEGREES_SLOPE + VOLTS_TO_DEGREES_CONSTANT;
        // return potVoltage * VOLTS_TO_DEGREES_SLOPE + VOLTS_TO_DEGREES_CONSTANT;
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
        return Math.abs(getCurrentAngle()-getTargetAngle())<WRIST_DEADZONE;
    }
     /**
      *  just a note from the wrist class,  0 degrees is supposed to be horizontal.
      *  Todo:  If this is code lifed from last year, need to make sure that +'ve still makes it go in the direction we want
      * @param angle in degrees  A postive angle is going to move the wrist that many degrees above horizontal
      */
    private void setTargetAngle(double angle){
        if (angle <= MIN_ANGLE) {
            this.targetAngle = MIN_ANGLE;
        } else if (angle >= MAX_ANGLE) {
            this.targetAngle = MAX_ANGLE;
        } else {
            this.targetAngle = angle;
        }

        this.targetVolts = convertAngleToVolts(this.targetAngle);
        io.setTargetVolts(this.targetVolts);
    }
    
    public void moveWristUp(){ //transport
        commandedPosition = WristPosition.UP;
        io.setIdleMode(IdleMode.kBrake);
        setTargetAngle(90);
    }
    public void moveWristIntake(){
        commandedPosition = WristPosition.INTAKE;
        io.setIdleMode(IdleMode.kCoast);
        setTargetAngle(0.4);
    }
    
    public void moveWristAmp(){
        commandedPosition = WristPosition.AMP;
        setTargetAngle(-2.0);
    } 
    public void moveWristTrap(){
        commandedPosition = WristPosition.TRAP;
        setTargetAngle(120);
    }
    public void moveWristClimb(){
        commandedPosition = WristPosition.CLIMB;
        setTargetAngle(130);
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
     * toggle wrist deadness; makes certain things go into wrist dead mode
     */
    public void wristDead() {
        isWristDeadAgain = !isWristDeadAgain;
        if (isWristDeadAgain) {
            io.setIdleMode(IdleMode.kCoast);
            wristDeadShuffle.setString("perhaps D:");
        } else {
            io.setIdleMode(IdleMode.kBrake);
            wristDeadShuffle.setString("not quite!");
        }
    }

    public void setMotors(double speed) {
        io.setPercentOut(speed);
    }

    public String getWristMotorMode() {
        if(io.getIdleMode() == IdleMode.kBrake) {
            return "brake";
        } else {
            return "not break";
        }
    }

    @Override
    public void periodic() {

        io.periodic();

        // if wrist is dead kill motor just in case
        if (isWristDeadAgain){
            io.setPercentOut(0);
        }

        SmartDashboard.putString("Commanded Position", commandedPosition.name());

        currentAngleShuffle.setDouble(getCurrentAngle());
        currentPotShuffle.setDouble(io.getEncoderPosition());

        targetAngleShuffle.setDouble(targetAngle);
        targetPotShuffle.setDouble(targetVolts);

        wristMotorModeShuffle.setString(getWristMotorMode());

        if(getCurrentAngle() <= 25 && this.targetAngle <= 9 && commandedPosition == WristPosition.INTAKE) {
            io.setPercentOut(0);
            io.setIdleMode(IdleMode.kCoast);
        } else {
            //wristController.setIdleMode(IdleMode.kBrake);
        }
    }
    
}
