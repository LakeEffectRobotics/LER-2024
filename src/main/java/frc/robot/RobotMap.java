package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class RobotMap {

    /** Inner class to hold CAN ID constants */
    private class CAN {

        private static final int LEFT_CONTROLLER_1 = 2;
        private static final int LEFT_CONTROLLER_2 = 3;
        private static final int RIGHT_CONTROLLER_1 = 4;
        private static final int RIGHT_CONTROLLER_2 = 5;
    }

    public static final CANSparkMax leftController1 = new CANSparkMax(CAN.LEFT_CONTROLLER_1, MotorType.kBrushless);
    public static final CANSparkMax rightController1 = new CANSparkMax(CAN.RIGHT_CONTROLLER_1, MotorType.kBrushless);
    public static final CANSparkMax rightController2 = new CANSparkMax(CAN.RIGHT_CONTROLLER_2, MotorType.kBrushless);
    public static final CANSparkMax leftController2 = new CANSparkMax(CAN.LEFT_CONTROLLER_2, MotorType.kBrushless);


    // Static initializer will be run on first reference to RobotMap (stealing code from greg)
    static {
        leftController2.follow(leftController1);
        rightController2.follow(rightController1);
    }
    
}
