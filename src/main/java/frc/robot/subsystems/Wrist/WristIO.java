package frc.robot.subsystems.Wrist;

import com.revrobotics.CANSparkBase.IdleMode;

interface WristIO {
    public double getEncoderPosition();
    public void setTargetVolts(double volts);
    public void setIdleMode(IdleMode idleMode);
    public IdleMode getIdleMode();
    public void setPercentOut(double percent);
    public void periodic();
}
