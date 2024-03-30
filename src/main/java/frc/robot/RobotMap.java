package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.SPI;

public class RobotMap {

    /** Inner class to hold CAN ID constants */
    private class CAN {
        /** Drivetrain */
        private static final int LEFT_CONTROLLER_1 = 3;
        private static final int LEFT_CONTROLLER_2 = 4;
        private static final int RIGHT_CONTROLLER_1 = 1;
        private static final int RIGHT_CONTROLLER_2 = 2;

        /** Intake */
        
        // Climber
        private static final int CLIMB_CONTROLLER_1 = 5;
        private static final int CLIMB_CONTROLLER_2 = 6;
        private static final int CLIMB_CONTROLLER_3 = 7;

        private static final int ARM_CONTROLLER_1 = 8;
        private static final int ARM_CONTROLLER_2 = 9;

        private static final int CLAW_CONTROLLER = 10;
        private static final int WRIST_CONTROLLER = 11;

    }

    private class PCM {
        private static final int DRIVE_SHIFT_UP = 15;
        private static final int DRIVE_SHIFT_DOWN = 14;

        private static final int ARM_PISTON_UP = 13;
        private static final int ARM_PISTON_DOWN = 12;

        private static final int CLIMB_PISTON_UP = 10; // Todo:  have to find out the real number
        private static final int CLIMB_PISTON_DOWN = 11; // Todo:  find the real number
    }

    /** Drivetrain */
    public static final CANSparkMax leftController1 = new CANSparkMax(CAN.LEFT_CONTROLLER_1, MotorType.kBrushless);
    public static final CANSparkMax rightController1 = new CANSparkMax(CAN.RIGHT_CONTROLLER_1, MotorType.kBrushless);
    public static final CANSparkMax rightController2 = new CANSparkMax(CAN.RIGHT_CONTROLLER_2, MotorType.kBrushless);
    public static final CANSparkMax leftController2 = new CANSparkMax(CAN.LEFT_CONTROLLER_2, MotorType.kBrushless);
    public static final DoubleSolenoid driveShitSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, PCM.DRIVE_SHIFT_DOWN, PCM.DRIVE_SHIFT_UP);

    /** Intake */
        
    // Climber
    public static final CANSparkMax climbController1 = new CANSparkMax(CAN.CLIMB_CONTROLLER_1, MotorType.kBrushless);
    public static final CANSparkMax climbController2 = new CANSparkMax(CAN.CLIMB_CONTROLLER_2, MotorType.kBrushless);
    public static final CANSparkMax climbController3 = new CANSparkMax(CAN.CLIMB_CONTROLLER_3, MotorType.kBrushless);
    public static final DoubleSolenoid climbShiftSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, PCM.CLIMB_PISTON_DOWN, PCM.CLIMB_PISTON_UP);  // 50% prob this is the right order for down and up

    // Arm
    public static final CANSparkMax armController1 = new CANSparkMax(CAN.ARM_CONTROLLER_1, MotorType.kBrushless);
    public static final CANSparkMax armController2 = new CANSparkMax(CAN.ARM_CONTROLLER_2, MotorType.kBrushless);
    public static final DoubleSolenoid armPistonSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, PCM.ARM_PISTON_UP, PCM.ARM_PISTON_DOWN);

    public static final CANSparkMax clawController = new CANSparkMax(CAN.CLAW_CONTROLLER, MotorType.kBrushless);
    public static final CANSparkMax wristController = new CANSparkMax(CAN.WRIST_CONTROLLER, MotorType.kBrushless);

    // Gyro
    public static final AHRS gyro = new AHRS(SPI.Port.kMXP);

    // Static initializer will be run on first reference to RobotMap (stealing code from greg)
    static {
        
        /** Drivetrain */
        leftController2.follow(leftController1);
        rightController2.follow(rightController1);

        leftController1.setInverted(false);
        rightController1.setInverted(false);   

        /** Intake */
        
        // Climber 
        climbController2.follow(climbController1);
        climbController3.follow(climbController1);

        climbController1.setIdleMode(IdleMode.kBrake);
        climbController2.setIdleMode(IdleMode.kBrake);
        climbController3.setIdleMode(IdleMode.kBrake);

        armController2.follow(armController1);

        armController1.setInverted(false);
        armController2.setInverted(false);

        armController1.setIdleMode(IdleMode.kBrake);
        armController2.setIdleMode(IdleMode.kBrake);


        wristController.setIdleMode(IdleMode.kCoast);
        wristController.setInverted(true);        
    }
  
    public static void burnFlash(){
        leftController1.burnFlash();
        leftController2.burnFlash();
        rightController1.burnFlash();
        rightController2.burnFlash();

        climbController1.burnFlash();
        climbController2.burnFlash();
        climbController3.burnFlash();

        armController1.burnFlash();
        armController2.burnFlash();

        clawController.burnFlash();
        wristController.burnFlash();
    }
    
}
