// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {

  private final DriveSubsystem DriveSubsystem = new DriveSubsystem();
  private final IntakeSubsystem IntakeSubsystem = new IntakeSubsystem();
  private final PneumaticsSubsystem PneumaticsSubsystem = new PneumaticsSubsystem();
  // private final TurretSubsystem turretSubsystem = new TurretSubsystem();
  private final IndexerSubsystem IndexerSubsystem = new IndexerSubsystem();
  private final ShooterPIDSubsystem shooterSubsystem = new ShooterPIDSubsystem();
  private final HoodPIDSubsystem hoodSubsystem = new HoodPIDSubsystem();
  private final VisionSubsystem VisionSubsystem = new VisionSubsystem();
  private final XboxController driveController = new XboxController(0);
  private final XboxController operateController = new XboxController(1);

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    DriveSubsystem.setDefaultCommand(

        new StickDrive(() -> driveController.getRawAxis(2) - driveController.getRawAxis(3),
            () -> -driveController.getRawAxis(0), DriveSubsystem));

    IntakeSubsystem.setDefaultCommand(new AutoIndex(IntakeSubsystem, IndexerSubsystem, PneumaticsSubsystem));

  }

  private void configureButtonBindings() {

    new JoystickButton(operateController, 6).whenPressed(new IndexOne(IndexerSubsystem));

    new JoystickButton(operateController, 1).whenPressed(new InstantCommand(PneumaticsSubsystem::frontIntakeOpen))
        .whenReleased(new InstantCommand(PneumaticsSubsystem::frontIntakeClosed));

    new JoystickButton(operateController, 3).whenPressed(new InstantCommand(PneumaticsSubsystem::rearIntakeOpen))
        .whenReleased(new InstantCommand(PneumaticsSubsystem::rearIntakeClosed));

    // new JoystickButton(driveController, 1).whileActiveOnce(new
    // AutoAim(DriveSubsystem, VisionSubsystem, shooterSubsystem, hoodSubsystem));\
    
    new JoystickButton(driveController, 1).whenPressed(new InstantCommand(shooterSubsystem::enable))
        .whenReleased(new InstantCommand(shooterSubsystem::disable));

  }

}
