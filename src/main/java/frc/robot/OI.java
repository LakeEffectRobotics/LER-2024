package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

    /** Driver station ports controllers are connected to */
    private static class PORT {

        private static final int LEFT_STICK = 0;
       

    }

  

    /** Driver (Joystick) */
    private static final Joystick leftJoystick = new Joystick(PORT.LEFT_STICK);
   
    public static DoubleSupplier leftDriveSupplier = () -> {
        double raw = leftJoystick.getY();

        //return raw;
        return processDriveInput(raw);
    };

  
  
    private static double processDriveInput(double raw) {
        // TODO: Configure input processing to suit your liking
        if (Math.abs(raw) < 0.1) {
            raw = 0;
        }
        
        // Signum function is -1 for x < 0, 1 for x > 0
        raw = Math.pow(raw, 2) * Math.signum(raw);
        // raw *= 0.8;
        return -raw;
    }
    
   
}
