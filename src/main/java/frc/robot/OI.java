package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class OI {

    /** Driver station ports controllers are connected to */
    private static class PORT {

        private static final int LEFT_STICK = 0;
        private static final int RIGHT_STICK = 1;
        private static final int OPERATOR_CONTROLLER = 2;

    }

    /** Driver Button Map */
    private static class DRIVER_MAP {
        private static final int SHIFT_UP_BUTTON = 3;
        private static final int SHIFT_DOWN_BUTTON = 2;
        private static final int LOW_SHIFT_BUTTON = 1;
    }

    private static final XboxController xboxController = new XboxController(PORT.OPERATOR_CONTROLLER);
    /**
     * The threshold that must be met before an xbox is trigger is considered "pressed". Used to bind command so triggers
     */
    private static final double XBOX_TRIGGER_THRESHOLD = 0.2;

    /** Operator Button Map */
    private static class OPERATOR_MAP {
        private static final int SPIN_ARM_TRIGGER = XboxController.Axis.kLeftTrigger.value;

        private static final int EXTEND_ARM_BUTTON = XboxController.Button.kY.value;
        private static final int RETRACT_ARM_BUTTON = XboxController.Button.kA.value;

    }

    private static final Joystick leftJoystick = new Joystick(PORT.LEFT_STICK);
    private static final Joystick rightJoystick = new Joystick(PORT.RIGHT_STICK);

    public static final JoystickButton shiftUpButton = new JoystickButton(rightJoystick, DRIVER_MAP.SHIFT_UP_BUTTON);
    public static final JoystickButton shiftDownButton = new JoystickButton(rightJoystick, DRIVER_MAP.SHIFT_DOWN_BUTTON);

    public static final JoystickButton lowShiftButton = new JoystickButton(leftJoystick, DRIVER_MAP.LOW_SHIFT_BUTTON);

    public static final Trigger spinArmButton = new Trigger(() -> xboxController.getRawAxis(OPERATOR_MAP.SPIN_ARM_TRIGGER) >= XBOX_TRIGGER_THRESHOLD);

    public static final Trigger extendArmButton = new JoystickButton(xboxController, OPERATOR_MAP.EXTEND_ARM_BUTTON);
    public static final Trigger retractArmButton = new JoystickButton(xboxController, OPERATOR_MAP.RETRACT_ARM_BUTTON);


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

    /**
     * Operator-supplied intake spin speed
     */
    public static DoubleSupplier spinArmSpeedSupplier = () -> {
        return Math.pow(xboxController.getRawAxis(OPERATOR_MAP.SPIN_ARM_TRIGGER), 2) * 0.6;
    };
    
}
