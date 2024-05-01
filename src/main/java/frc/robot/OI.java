package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
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
        private static final int ARM_PREPARE_CLIMB_BUTTON = 4;
        
        private static final int CLIMB_UP_MANUAL_BUTTON = 3;
        private static final int CLIMB_DOWN_MANUAL_BUTTON = 2;

        private static final int ARM_EXTENSION_BUTTONS = 5;
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
        private static final int PREPARE_CLIMB_BUTTON = XboxController.Button.kLeftBumper.value;
        private static final int CLIMB_BUTTTON = XboxController.Button.kRightBumper.value;
        private static final int SHOOTFASTBUTTON = XboxController.Button.kStart.value;


        // XBoxController DPAD Buttons
        private static final int ARM_UP_BUTTON = 0;
        private static final int ARM_DOWN_BUTTON = 180;
        private static final int DOWN_SELECTION_BUTTON = 90;
        private static final int LEFT_SELECTION_BUTTON = 270;
    }


    /** Driver (Joystick) */
    private static final Joystick leftJoystick = new Joystick(PORT.LEFT_STICK);
    private static final Joystick rightJoystick = new Joystick(PORT.RIGHT_STICK);

    public static final JoystickButton shiftUpButton = new JoystickButton(rightJoystick, DRIVER_MAP.SHIFT_UP_BUTTON);
    public static final JoystickButton shiftDownButton = new JoystickButton(rightJoystick, DRIVER_MAP.SHIFT_DOWN_BUTTON);
    public static final JoystickButton lowShiftButton = new JoystickButton(leftJoystick, DRIVER_MAP.LOW_SHIFT_BUTTON);
    
    public static final JoystickButton armPrepareClimbButton = new JoystickButton(leftJoystick, DRIVER_MAP.ARM_PREPARE_CLIMB_BUTTON); 
    public static final JoystickButton trapPositionButton = new JoystickButton(rightJoystick, DRIVER_MAP.ARM_PREPARE_CLIMB_BUTTON);
    public static final JoystickButton ClimbUpManualButton = new JoystickButton(leftJoystick, DRIVER_MAP.CLIMB_UP_MANUAL_BUTTON);
    public static final JoystickButton ClimbDownManualButton = new JoystickButton(leftJoystick, DRIVER_MAP.CLIMB_DOWN_MANUAL_BUTTON);

    public static final JoystickButton ExtendArmButton = new JoystickButton(leftJoystick, DRIVER_MAP.ARM_EXTENSION_BUTTONS);
    public static final JoystickButton RetractArmButton = new JoystickButton(rightJoystick, DRIVER_MAP.ARM_EXTENSION_BUTTONS);

    /** Operator (Xbox Controller) */
    public static final Trigger spinOutClawButton = new Trigger(() -> xboxController.getRawAxis(OPERATOR_MAP.SPIN_OUT_INTAKE_TRIGGER) >= XBOX_TRIGGER_THRESHOLD);
    public static final Trigger spinInClawButton = new Trigger(() -> xboxController.getRawAxis(OPERATOR_MAP.SPIN_IN_INTAKE_TRIGGER) >= XBOX_TRIGGER_THRESHOLD);

    public static final Trigger armUpButton = new JoystickButton(rightJoystick, 1);
    public static final Trigger armDownButton = new JoystickButton(rightJoystick, 2);

    public static final JoystickButton intakePositionButton = new JoystickButton(xboxController, OPERATOR_MAP.INTAKE_POSITION_BUTTON);
    public static final JoystickButton transportPositionButton = new JoystickButton(xboxController, OPERATOR_MAP.TRANSPORT_POSITION_BUTTON);
    public static final JoystickButton ampPositionButton = new JoystickButton(xboxController, OPERATOR_MAP.AMP_POSITION_BUTTON);
    public static final JoystickButton prepareClimbButton = new JoystickButton(xboxController, OPERATOR_MAP.PREPARE_CLIMB_BUTTON);
    public static final JoystickButton climbButton = new JoystickButton(xboxController,OPERATOR_MAP.CLIMB_BUTTTON);

    public static final JoystickButton shootFastButton = new JoystickButton(xboxController, OPERATOR_MAP.SHOOTFASTBUTTON);


    public static DoubleSupplier leftDriveSupplier = () -> {
        double raw = leftJoystick.getY();

        //return raw;
        return processDriveInput(raw);
    };

    public static DoubleSupplier rightDriveSupplier = () -> {
        double raw = rightJoystick.getY();
        
        //return raw;
        return processDriveInput(raw);
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
    
    public static void xboxRumble(double value) {
        xboxController.setRumble(RumbleType.kBothRumble, value);
    }

    // public static void toggleGuitar() { //in case guitar stops working for some reason
    //     if(PORT.OPERATOR_CONTROLLER == 2) {

    //         System.out.println("USING XBOX CONTROLLE1!!!!!11!1!!!!11!!1!!11!!1!");
    //         PORT.OPERATOR_CONTROLLER = 3;
    //     } else {
    //         System.out.println("USING ");
    //         PORT.OPERATOR_CONTROLLER = 2;
    //     }
    // }
}
