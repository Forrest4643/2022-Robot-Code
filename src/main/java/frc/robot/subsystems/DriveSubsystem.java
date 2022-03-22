// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveSubsystem extends SubsystemBase {

  // defining motor names
  private final CANSparkMax leftFront = new CANSparkMax(DriveConstants.leftFrontID, MotorType.kBrushless);
  private final CANSparkMax leftRear = new CANSparkMax(DriveConstants.leftRearID, MotorType.kBrushless);
  private final CANSparkMax rightFront = new CANSparkMax(DriveConstants.rightFrontID, MotorType.kBrushless);
  private final CANSparkMax rightRear = new CANSparkMax(DriveConstants.rightRearID, MotorType.kBrushless);

  // setting speed controller groups
  private final MotorControllerGroup leftDrive = new MotorControllerGroup(leftFront, leftRear);
  private final MotorControllerGroup rightDrive = new MotorControllerGroup(rightFront, rightRear);

  private RelativeEncoder leftFrontEncoder = leftFront.getEncoder();
  private RelativeEncoder leftRearEncoder = leftRear.getEncoder();
  private RelativeEncoder rightFrontEncoder = rightFront.getEncoder();
  private RelativeEncoder rightRearEncoder = rightRear.getEncoder();

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(leftDrive, rightDrive);

  SlewRateLimiter driveSlew = new SlewRateLimiter(DriveConstants.turnSlew);
  SlewRateLimiter turnSlew = new SlewRateLimiter(DriveConstants.steerSlew);

  public double getDriveDistanceFT() {
    // returns the average position of all drive encoders.
    double driveForwardRAW = ((leftFrontEncoder.getPosition() + leftRearEncoder.getPosition()) / 2)
        + ((rightFrontEncoder.getPosition() + rightRearEncoder.getPosition()) / 2) / 2;

    return (driveForwardRAW * DriveConstants.driveTickToIN) / 12;
  }

  public void resetDriveEncoders() {
    leftFrontEncoder.setPosition(0);
    leftRearEncoder.setPosition(0);
    rightFrontEncoder.setPosition(0);
    rightRearEncoder.setPosition(0);
  }

  /** Creates a new ExampleSubsystem. */
  public DriveSubsystem() {
    leftDrive.setInverted(false);
    rightDrive.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("DriveDistanceFT", getDriveDistanceFT());
  }

  public void setDrive(double Speed, double turnRate) {

    // inputs to a power for a nice response curve

    double SqrSpeed = Math.pow(MathUtil.applyDeadband(Math.abs(Speed), DriveConstants.stickDB), DriveConstants.speedPow);
    double SqrTurn = Math.pow(MathUtil.applyDeadband(Math.abs(turnRate), DriveConstants.stickDB), DriveConstants.turnPow);

    if (Speed < 0) {
      SqrSpeed = SqrSpeed * -1;
    }

    if (turnRate < 0) {
      SqrTurn = SqrTurn * -1;
    }

    m_robotDrive.arcadeDrive(driveSlew.calculate(SqrSpeed), driveSlew.calculate(SqrTurn));

    SmartDashboard.putNumber("sqrturn", SqrTurn);
    SmartDashboard.putNumber("sqrspeed", SqrSpeed);
  }
}
