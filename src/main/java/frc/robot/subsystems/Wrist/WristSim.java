package frc.robot.subsystems.Wrist;

import static frc.robot.subsystems.Wrist.Wrist.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class WristSim implements WristIO {

    private Wrist instance;

    private SingleJointedArmSim physics = new SingleJointedArmSim(DCMotor.getNEO(1), 80, SingleJointedArmSim.estimateMOI(0.28, 7), 0.28, Math.toRadians(MIN_ANGLE), Math.toRadians(MAX_ANGLE), true, Math.toRadians(MAX_ANGLE));

    Double percentOut = null;
    Double targetPosition = null;

    IdleMode idleMode;

    long lastTime = System.currentTimeMillis();

    public WristSim(CANSparkMax wristController, Wrist instance){
        this.instance = instance;
    }

    @Override
    public double getEncoderPosition() {
        return instance.convertAngleToVolts(Math.toDegrees(physics.getAngleRads()));
    }

    @Override
    public void setTargetVolts(double volts) {
        targetPosition = volts;
        percentOut = null;
    }

    @Override
    public void setIdleMode(IdleMode idleMode) {
        this.idleMode = idleMode;
    }

    @Override
    public IdleMode getIdleMode() {
        return idleMode;
    }

    @Override
    public void setPercentOut(double percent) {
        targetPosition = null;
        percentOut = percent;
    }
    
    @Override
    public void periodic(){
        double volts = 0;

        if(targetPosition != null){
            // Simulating just P
            volts = ((targetPosition - getEncoderPosition()) * kP)* 12;
        } else if (percentOut != null){
            volts = 12 * percentOut;
        }

        physics.setInputVoltage(volts);

        physics.update((System.currentTimeMillis() - lastTime)/1000.0);
        lastTime = System.currentTimeMillis();

        SmartDashboard.putNumber("Wrist Current", physics.getCurrentDrawAmps());
    }
}
