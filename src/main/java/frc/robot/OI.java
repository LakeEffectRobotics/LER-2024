package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

    /** Driver station ports controllers are connected to */
    private static class PORT {

        private static final int LEFT_STICK = 0;
        private static final int RIGHT_STICK = 1;
        private static final int OPERATOR_CONTROLLER = 2;

    }

    /** Driver Button Map */
    private static class DRIVER_MAP {

    }

    /** Operator Button Map */
    private static class OPERATOR_MAP {

    }

    private static final Joystick leftJoystick = new Joystick(PORT.LEFT_STICK);
    private static final Joystick rightJoystick = new Joystick(PORT.RIGHT_STICK);

    public static DoubleSupplier leftDriveSupplier = () -> {
        double raw = leftJoystick.getY();
        // todo process raw output
        return raw;
    };

    public static DoubleSupplier rightDriveSupplier = () -> {
        double raw = rightJoystick.getY();
        // todo process raw output
        return raw;
    };
    
}
