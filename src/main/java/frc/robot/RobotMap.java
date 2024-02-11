package frc.robot;

import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class RobotMap {

    /** Inner class to hold CAN ID constants */
    private class CAN {

        private static final int LEFT_CONTROLLER_1 = 3;
        private static final int LEFT_CONTROLLER_2 = 4;
        private static final int RIGHT_CONTROLLER_1 = 1;
        private static final int RIGHT_CONTROLLER_2 = 2;
    }

    private class PCM {
        private static final int DRIVE_SHIFT_UP = 0;
        private static final int DRIVE_SHIFT_DOWN = 1;
    }

    public static final CANSparkMax leftController1 = new CANSparkMax(CAN.LEFT_CONTROLLER_1, MotorType.kBrushless);
    public static final CANSparkMax rightController1 = new CANSparkMax(CAN.RIGHT_CONTROLLER_1, MotorType.kBrushless);
    public static final CANSparkMax rightController2 = new CANSparkMax(CAN.RIGHT_CONTROLLER_2, MotorType.kBrushless);
    public static final CANSparkMax leftController2 = new CANSparkMax(CAN.LEFT_CONTROLLER_2, MotorType.kBrushless);

    public static final RelativeEncoder leftDriveEncoder;
    public static final RelativeEncoder rightDriveEncoder;

    public static final DoubleSolenoid driveShitSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, PCM.DRIVE_SHIFT_DOWN, PCM.DRIVE_SHIFT_UP);

    public static final double CONVERSION_FACTOR = 1/4.0*4*Math.PI;
    // Static initializer will be run on first reference to RobotMap (stealing code from greg)
    static {
        
        leftController2.follow(leftController1);
        rightController2.follow(rightController1);

        leftController1.setInverted(false);
        rightController1.setInverted(true);
        
        leftDriveEncoder = leftController1.getAlternateEncoder(128 * 4);
        leftDriveEncoder.setPositionConversionFactor(CONVERSION_FACTOR);
        leftDriveEncoder.setInverted(true);
        leftDriveEncoder.setPosition(0);
        rightDriveEncoder = rightController1.getAlternateEncoder(64 * 4);
        rightDriveEncoder.setPositionConversionFactor(CONVERSION_FACTOR);
        rightDriveEncoder.setPosition(0);
        rightDriveEncoder.setInverted(false);      
    }
    
}
