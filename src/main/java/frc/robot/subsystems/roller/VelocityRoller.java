package frc.robot.subsystems.roller;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.hardware.interfaces.*;
import org.littletonrobotics.junction.Logger;

public class VelocityRoller extends Roller {

	private IRequest<Rotation2d> velocityRequest;

	public VelocityRoller(
		String logPath,
		ControllableMotor roller,
		InputSignal<Double> voltageSignal,
		InputSignal<Double> currentSignal,
		InputSignal<Rotation2d> positionSignal,
		IRequest<Double> voltageRequest,
		IRequest<Rotation2d> velocityRequest
	) {
		super(logPath, roller, voltageSignal, currentSignal, positionSignal, voltageRequest);
		this.velocityRequest = velocityRequest;
	}


	public void setTargetVelocity(Rotation2d targetVelocity) {
		velocityRequest.withSetPoint(targetVelocity);
		roller.applyRequest(velocityRequest);
	}

	public void log() {
		Logger.recordOutput(getLogPath() + "/VelocityRollerTargetVelocity", velocityRequest.getSetPoint());
	}

}
