package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class RobotMap {

    /** Inner class to hold CAN ID constants */
    private class CAN {
        /** Drivetrain */
        private static final int LEFT_CONTROLLER_1 = 3;
       
    }


    /** Sample Motor */
    public static final CANSparkMax leftController1 = new CANSparkMax(CAN.LEFT_CONTROLLER_1, MotorType.kBrushless);
   


  
    public static void burnFlash(){
        leftController1.burnFlash();
       
    }
    
}
