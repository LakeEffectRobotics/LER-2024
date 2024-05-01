package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;


public class Claw extends SubsystemBase {

    public static double CLAWINTAKESPEED = 0.5;
    public static double CLAWSHOOTSPEED = 25.0;
    private SparkLimitSwitch limit;

    private static Long rumbleEndTime = null;
    CANSparkMax clawController;

    SparkLimitSwitch clawLimitSwitch;

    MechanismLigament2d spinA;
    MechanismLigament2d spinB;

    double spinAngle = 0;

    // very large shuffleboard entry for limit switch pressed
    private ShuffleboardTab tab = Shuffleboard.getTab("my favourite tab");
    public GenericEntry limitSwitchShuffle = tab
        .add("limit switch!", false)
        .withSize(3, 3)
        .withPosition(7, 0)
        .getEntry();

    public Claw(CANSparkMax clawController, MechanismLigament2d parent) {
        this.clawController = clawController;
        limit = clawController.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        limit.enableLimitSwitch(false);

        spinA = new MechanismLigament2d("Intake A", 0.1, 0, 5, new Color8Bit(Color.kGreen));
        spinB = new MechanismLigament2d("Intake B", 0.1, 180, 5, new Color8Bit(Color.kGreen));

        parent.append(spinA);
        parent.append(spinB);
    }

    public void setOutput(double speed) {
        clawController.set(speed);
    }

    //public void setMode() {
        /*
        //TODO: determine speed for both cases
        switch (speed) {
            case INTAKE:
                    setOutput(0.0);
                break;
            case SHOOT:
                    setOutput(0.0);
                break;
            default:
                break;
        }
        */
    //}

    public void stop() {
        clawController.set(0);
    }

    @Override
    public void periodic() {

        // Periodic things
        
        /* operator controller rumble */
        if(hasNote()) {
            if(rumbleEndTime == null) { 
                rumbleEndTime = System.currentTimeMillis()+500;
                OI.xboxRumble(50.0);
            }
            if(System.currentTimeMillis() >= rumbleEndTime) {
                OI.xboxRumble(0.0);
            }
        } else {
            OI.xboxRumble(0.0);
            rumbleEndTime = null; 
        }
        //

        SmartDashboard.putNumber("Intake Speed", getSpeed());
        SmartDashboard.putBoolean("has note", hasNote());

        if(clawController.getAppliedOutput() != 0){
            spinAngle -= 15;
            spinA.setAngle(spinAngle);
            spinB.setAngle(spinAngle + 180);
        }
    }
    

    public double getSpeed(){
        return clawController.getEncoder().getVelocity();
    }

    public boolean hasNote() {
        return limit.isPressed();
       }
}
