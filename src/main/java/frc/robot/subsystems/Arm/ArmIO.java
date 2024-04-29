package frc.robot.subsystems.Arm;

import com.revrobotics.CANSparkBase.IdleMode;

import frc.robot.subsystems.Arm.Arm.ArmExtension;

interface ArmIO {
    public double getPotVolts();
    public void setPiston(ArmExtension value);
    public void setPercent(double percent);
    public void setPosition(double targetVolts);
    public IdleMode getIdleMode();
    public void periodic();
}
