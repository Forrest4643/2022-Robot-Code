// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.HoodConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HoodPIDSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.ShooterPIDSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class AutoAim extends CommandBase {
  private DriveSubsystem m_driveSubsystem;
  private VisionSubsystem m_visionSubsystem;
  private ShooterPIDSubsystem m_shooterPIDSubsystem;
  private HoodPIDSubsystem m_hoodPIDSubsystem;

  private PIDController driveSteer = new PIDController(DriveConstants.drivekP, DriveConstants.drivekI,
      DriveConstants.drivekD);

  /** Creates a new driveAim. */
  public AutoAim(HoodPIDSubsystem m_hoodPIDSubsystem, VisionSubsystem m_visionSubsystem,
      ShooterPIDSubsystem m_shooterPIDSubsystem, DriveSubsystem m_driveSubsystem, IndexerSubsystem m_indexerSubsystem) {
    this.m_driveSubsystem = m_driveSubsystem;
    this.m_visionSubsystem = m_visionSubsystem;
    this.m_shooterPIDSubsystem = m_shooterPIDSubsystem;
    this.m_hoodPIDSubsystem = m_hoodPIDSubsystem;

    addRequirements(m_hoodPIDSubsystem, m_hoodPIDSubsystem, m_shooterPIDSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    System.out.println("AutoAim Started!");
    
    m_shooterPIDSubsystem.enable();
    m_hoodPIDSubsystem.enable();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    double targetDistance = m_visionSubsystem.getTargetDistanceFT();
    double targetYaw = m_visionSubsystem.getTargetYaw();

    // aiming hood
    m_hoodPIDSubsystem.setSetpoint(MathUtil.clamp(
        HoodConstants.quadAimC +
            (HoodConstants.quadAimB * targetDistance) +
            (Math.pow(targetDistance, 2) * HoodConstants.quadAimA),

        65, 80));

    // setting shooter RPM

    m_shooterPIDSubsystem.setSetpoint(ShooterConstants.quadAimD +
        (ShooterConstants.quadAimC * targetDistance) +
        (Math.pow((targetDistance), 2) * ShooterConstants.quadAimB) +
        (Math.pow(targetDistance, 3) * ShooterConstants.quadAimA));

    double turnRate = driveSteer.calculate(targetYaw);
    m_driveSubsystem.setDrive(0, MathUtil.clamp(turnRate, -.5, .5));

    SmartDashboard.putNumber("driveSteer", turnRate);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shooterPIDSubsystem.disable();
    m_hoodPIDSubsystem.disable();
    m_driveSubsystem.setDrive(0, 0);
    System.out.println("AutoAim Ended!");
  }

  public boolean readyFire() {
    return m_shooterPIDSubsystem.getController().atSetpoint()
        && m_hoodPIDSubsystem.getController().atSetpoint() && driveSteer.atSetpoint();

  }
}
