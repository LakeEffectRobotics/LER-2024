package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {

    DoubleSolenoid armSolenoid;

    private ShuffleboardTab tab = Shuffleboard.getTab("my favourite tab");

    ArmPosition armCurrentPosition;

    private GenericEntry armUpShuffle = tab
        .add("arm up?", "nuh D':")
        .withPosition(6, 1)
        .getEntry();

    public Arm(DoubleSolenoid armSolenoid) {
        this.armSolenoid = armSolenoid;

        armCurrentPosition = ArmPosition.RETRACT;
    }

    // Arm piston positions: up, down
    public enum ArmPosition {
        EXTEND(DoubleSolenoid.Value.kForward),
        RETRACT(DoubleSolenoid.Value.kReverse);
    
        private DoubleSolenoid.Value value;
    
        private ArmPosition(DoubleSolenoid.Value value) {
            this.value = value;
        }
    
        public DoubleSolenoid.Value getValue() {
            return value;
        }
    }

    public void extendArm() {
        armCurrentPosition = ArmPosition.EXTEND;

        armSolenoid.set(ArmPosition.EXTEND.value);

        armUpShuffle.setString("YEAH!");
    }

    public void retractArm() {
        armCurrentPosition = ArmPosition.RETRACT;

        armSolenoid.set(ArmPosition.RETRACT.value);

        armUpShuffle.setString("nuh D':");
    }

    @Override
    public void periodic() {
        // Periodic things
    }
    
}
