package frc.robot.subsystems.Wrist;

import static frc.robot.subsystems.Wrist.Wrist.*;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;

class WristReal implements WristIO {
    CANSparkMax wristController;

    //SparkAnalogSensor pot;

    SparkPIDController pidController; 

    Wrist instance;

    public WristReal(CANSparkMax wristController, Wrist instance){
        this.instance = instance;
        this.wristController = wristController;

        // pot = wristController.getAnalog(SparkAnalogSensor.Mode.kAbsolute);
        // wristController.setInverted(true);

        pidController = wristController.getPIDController();
        pidController.setFeedbackDevice(wristController.getEncoder());

        // set PID coefficients
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIAccum(0.05);
        pidController.setFF(kF);
        pidController.setOutputRange(MIN_OUTPUT, MAX_OUTPUT);
    }

    @Override
    public double getEncoderPosition() {
        return wristController.getEncoder().getPosition();
    }

    @Override
    public void setTargetVolts(double volts){
        pidController.setReference(volts, ControlType.kPosition);
    }

    @Override
    public void setIdleMode(IdleMode idleMode){
        wristController.setIdleMode(IdleMode.kBrake);
    }

    @Override
    public IdleMode getIdleMode(){
        return wristController.getIdleMode();
    }

    @Override
    public void setPercentOut(double percent){
        wristController.set(percent);
    }

    @Override
    public void periodic(){
    }
}
