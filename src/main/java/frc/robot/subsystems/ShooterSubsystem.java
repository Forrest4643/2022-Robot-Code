// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
  private final CANSparkMax leftMotor = new CANSparkMax(ShooterConstants.leftMotorID, MotorType.kBrushless);
  private final CANSparkMax rightMotor = new CANSparkMax(ShooterConstants.rightMotorID, MotorType.kBrushless);

  private final RelativeEncoder leftEncoder = leftMotor.getEncoder();
  private final RelativeEncoder rightEncoder = rightMotor.getEncoder();

  
  /** Creates a new ShooterSubsystem. */
  public ShooterSubsystem() {
    leftMotor.setInverted(true);
    rightMotor.setInverted(false);

    rightEncoder.setVelocityConversionFactor(1);
    leftEncoder.setVelocityConversionFactor(1);
    }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("leftShooterRPM", leftEncoder.getVelocity());
    SmartDashboard.putNumber("rightShooterRPM", rightEncoder.getVelocity());
    SmartDashboard.putNumber("shooterRPM", getShooterRPM());
  }

  public void idleShooter() {
    leftMotor.set(0);
    rightMotor.set(0);
  }

  public void setShooterSpeed(double speed) {
    leftMotor.set(speed);
    rightMotor.set(speed);
  }

  public double getShooterRPM() {
    return (leftEncoder.getVelocity() + rightEncoder.getVelocity()) / 2;
  }
}