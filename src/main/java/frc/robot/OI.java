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
        private static final int SPIN_OUT_INTAKE_TRIGGER = XboxController.Axis.kLeftTrigger.value;
        private static final int SPIN_IN_INTAKE_TRIGGER = XboxController.Axis.kRightTrigger.value;
        
        private static final int INTAKE_POSITION_BUTTON = XboxController.Button.kA.value;
        private static final int TRANSPORT_POSITION_BUTTON = XboxController.Button.kB.value;
        private static final int AMP_POSITION_BUTTON = XboxController.Button.kX.value;
        private static final int TRAP_POSITION_BUTTON = XboxController.Button.kY.value;
    }


    /** Driver (Joystick) */
    private static final Joystick leftJoystick = new Joystick(PORT.LEFT_STICK);
    private static final Joystick rightJoystick = new Joystick(PORT.RIGHT_STICK);

    public static final JoystickButton shiftUpButton = new JoystickButton(rightJoystick, DRIVER_MAP.SHIFT_UP_BUTTON);
    public static final JoystickButton shiftDownButton = new JoystickButton(rightJoystick, DRIVER_MAP.SHIFT_DOWN_BUTTON);
    public static final JoystickButton lowShiftButton = new JoystickButton(leftJoystick, DRIVER_MAP.LOW_SHIFT_BUTTON);

    /** Operator (Xbox Controller) */
    public static final Trigger spinOutClawButton = new Trigger(() -> xboxController.getRawAxis(OPERATOR_MAP.SPIN_OUT_INTAKE_TRIGGER) >= XBOX_TRIGGER_THRESHOLD);
    public static final Trigger spinInClawButton = new Trigger(() -> xboxController.getRawAxis(OPERATOR_MAP.SPIN_IN_INTAKE_TRIGGER) >= XBOX_TRIGGER_THRESHOLD);

    public static final JoystickButton intakePositionButton = new JoystickButton(xboxController, OPERATOR_MAP.INTAKE_POSITION_BUTTON);
    public static final JoystickButton transportPositionButton = new JoystickButton(xboxController, OPERATOR_MAP.TRANSPORT_POSITION_BUTTON);
    public static final JoystickButton ampPositionButton = new JoystickButton(xboxController, OPERATOR_MAP.AMP_POSITION_BUTTON);
    public static final JoystickButton trapPositionButton = new JoystickButton(xboxController, OPERATOR_MAP.TRAP_POSITION_BUTTON);



    //public static final Trigger extendArmButton = new JoystickButton(xboxController, OPERATOR_MAP.EXTEND_ARM_BUTTON);
    //public static final Trigger retractArmButton = new JoystickButton(xboxController, OPERATOR_MAP.RETRACT_ARM_BUTTON);


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
    public static DoubleSupplier spinOutClawSpeedSupplier = () -> {
        return Math.pow(xboxController.getRawAxis(OPERATOR_MAP.SPIN_OUT_INTAKE_TRIGGER), 2);
    };

    public static DoubleSupplier spinInClawSpeedSupplier = () -> {
        return Math.pow(xboxController.getRawAxis(OPERATOR_MAP.SPIN_IN_INTAKE_TRIGGER), 2);
    };
    
}
