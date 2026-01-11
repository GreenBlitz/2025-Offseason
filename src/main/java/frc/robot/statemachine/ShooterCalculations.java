package frc.robot.statemachine;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.SimulationManager;
import frc.robot.subsystems.constants.turret.TurretConstants;

public class ShooterCalculations {

	public static Pose2d getTurretPose(Pose2d robotPose) {
		return new Pose2d(
			robotPose.getX() + robotPose.getRotation().getCos() * TurretConstants.TURRET_DISTANCE_FROM_ROBOT_ON_X_AXIS,
			robotPose.getY() + robotPose.getRotation().getSin() * TurretConstants.TURRET_DISTANCE_FROM_ROBOT_ON_X_AXIS,
			robotPose.getRotation()
		);
	}

}
