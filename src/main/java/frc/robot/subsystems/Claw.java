package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.SparkMaxLimitSwitch;


import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;


public class Claw extends SubsystemBase {

    public static double CLAWINTAKESPEED = 0.5;
    public static double CLAWSHOOTSPEED = -25.0;
    private SparkLimitSwitch limit;

    private static Long rumbleEndTime = null;
    CANSparkMax clawController;

    SparkLimitSwitch clawLimitSwitch;

    // very large shuffleboard entry for limit switch pressed
    private ShuffleboardTab tab = Shuffleboard.getTab("my favourite tab");
    public GenericEntry limitSwitchShuffle = tab
        .add("limit switch!", false)
        .withSize(3, 3)
        .withPosition(7, 0)
        .getEntry();

    public Claw(CANSparkMax clawController) {
        this.clawController = clawController;
        limit = clawController.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        limit.enableLimitSwitch(false);
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
        
        if(hasNote()) {
            if(rumbleEndTime == null) {
                rumbleEndTime = System.currentTimeMillis()+1000;
                OI.xboxRumble(50.0);
            }
            if(System.currentTimeMillis() >= rumbleEndTime) {
                OI.xboxRumble(0.0);
            }
        } else {
            rumbleEndTime = null;
        }

        SmartDashboard.putNumber("Intake Speed", getSpeed());
        SmartDashboard.putBoolean("has note", hasNote());
    }
    

    public double getSpeed(){
        return clawController.getEncoder().getVelocity();
    }

    public boolean hasNote() {
        return limit.isPressed();
       }
}
