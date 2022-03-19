// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.ShooterSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class shooterPID extends PIDCommand {
  /** Creates a new shooterPID. */
  public shooterPID(ShooterSubsystem shooterSubsystem, DoubleSupplier shooterRPM) {
    super(
        // The controller that the command will use
        new PIDController(ShooterConstants.shooterkP, ShooterConstants.shooterkI, ShooterConstants.shooterkD),
        // This should return the measurement
        () -> shooterSubsystem.getShooterRPM(),
        // This should return the setpoint (can also be a constant)
        () -> shooterRPM.getAsDouble(),
        // This uses the output
        output -> {
          // sets the min and max output to 1 and -1
          shooterSubsystem.setShooterSpeed(MathUtil.clamp(output, -1, 1));

          SmartDashboard.putNumber("sspeed", output);

        });
    addRequirements(shooterSubsystem);
    getController().enableContinuousInput(-135, 135);
    getController().setTolerance(50);
  }

  @Override
  public void initialize() {
    System.out.println("shooterPID started!");
  }

  @Override
  public void end(boolean interrupted) {
    System.out.println("shooterPID ended!");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
