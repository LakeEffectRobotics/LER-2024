package frc.robot.subsystems.Arm;

import static frc.robot.subsystems.Arm.Arm.*;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import frc.robot.subsystems.Arm.Arm.ArmExtension;

class ArmSim implements ArmIO {

    SingleJointedArmSim physics = new SingleJointedArmSim(DCMotor.getNEO(2), 200, 7.585, 1.5, 0, Math.PI/2, true, 0);

    Arm instance;

    Double percentOut = null;
    Double targetPosition = null;

    long lastTime = System.currentTimeMillis();

    public ArmSim(Arm instance){
        this.instance = instance;
    }

    private double getAngleDeg(){
        return Math.toDegrees(physics.getAngleRads());
    }

    @Override
    public double getPotVolts() {
        return instance.convertAngleToVolts(getAngleDeg());
    }
    @Override
    public void setPiston(ArmExtension value) {
        
    }
    @Override
    public void setPercent(double percent) {
        percentOut = percent;
        targetPosition = null;
    }
    @Override
    public void setPosition(double targetVolts) {
        targetPosition = targetVolts;
        percentOut = null;
    }
    @Override
    public IdleMode getIdleMode() {
        return IdleMode.kBrake;
    }
    
    @Override
    public void periodic() {
        double volts = 0;

        if(targetPosition != null){
            // Simulating just P
            volts = ((targetPosition - getPotVolts()) * kP + instance.getArbitraryFeedforward())* 12;
        } else if (percentOut != null){
            volts = 12 * percentOut;
        }

        physics.setInputVoltage(volts);

        physics.update((System.currentTimeMillis() - lastTime)/1000.0);
        lastTime = System.currentTimeMillis();
    }
}
