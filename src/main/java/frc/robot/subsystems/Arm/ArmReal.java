package frc.robot.subsystems.Arm;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAnalogSensor;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.subsystems.Arm.Arm.ArmExtension;

import static frc.robot.subsystems.Arm.Arm.*;

class ArmReal implements ArmIO {

    DoubleSolenoid armSolenoid;

    CANSparkMax armLeadController;
    SparkAnalogSensor pot;
    SparkPIDController pidController; 
    Arm instance;

    public ArmReal(DoubleSolenoid armSolenoid, CANSparkMax armLeadController, Arm instance){
        this.armSolenoid = armSolenoid;
        this.armLeadController = armLeadController;
        this.instance = instance;

        pot = armLeadController.getAnalog(SparkAnalogSensor.Mode.kAbsolute);

        pidController = armLeadController.getPIDController();
        pidController.setFeedbackDevice(pot);

        // set PId coefficients
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIAccum(0.05);
        pidController.setFF(kF);
        pidController.setOutputRange(MIN_OUTPUT, MAX_OUTPUT);
    }

    @Override
    public double getPotVolts(){
        return pot.getPosition();
    }

    @Override
    public void setPiston(ArmExtension value){
        armSolenoid.set(value.getValue());
    }

    @Override
    public void setPercent(double percent){
        armLeadController.set(percent);
    }

    @Override
    public void setPosition(double targetVolts){
        pidController.setReference(targetVolts, ControlType.kPosition, 0, instance.getArbitraryFeedforward());
    }

    @Override
    public IdleMode getIdleMode() {
        return armLeadController.getIdleMode();
    }

    @Override
    public void periodic() {

    }
}
