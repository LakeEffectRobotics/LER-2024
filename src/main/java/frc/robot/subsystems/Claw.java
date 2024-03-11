package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.SparkMaxLimitSwitch;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Claw extends SubsystemBase {

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

    }

    public void setOutput(double speed) {
        clawController.set(speed);
    }

    public void stop() {
        clawController.set(0);
    }

    @Override
    public void periodic() {
        // Periodic things
    }
    
}
