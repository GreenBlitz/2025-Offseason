package frc.robot.statemachine.shooterstatehandler;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.constants.turret.TurretConstants;
import frc.utils.LoggedNetworkRotation2d;

public class ShooterConstants {

	public static final Rotation2d DEFAULT_FLYWHEEL_ROTATIONS_PER_SECOND = Rotation2d.fromRotations(10);

	public static final LoggedNetworkRotation2d turretCalibrationAngle = new LoggedNetworkRotation2d(
		"Tunable/TurretAngle",
		Rotation2d.fromDegrees(0.0)
	);
	public static final LoggedNetworkRotation2d hoodCalibrationAngle = new LoggedNetworkRotation2d(
		"Tunable/HoodAngle",
		Rotation2d.fromDegrees(0.0)
	);
	public static final LoggedNetworkRotation2d flywheelCalibrationRotations = new LoggedNetworkRotation2d(
		"Tunable/FlywheelRotations",
		Rotation2d.fromRotations(0.0)
	);
	public static final Rotation2d MAX_DISTANCE_FROM_MAX_OR_MIN_POSITION_NOT_TO_ROTATE = Rotation2d.fromDegrees(7);
	public static final Rotation2d SCREW_MAX_RANGE_EDGE = TurretCalculations
		.getRangeEdge(TurretConstants.MAX_POSITION, ShooterConstants.MAX_DISTANCE_FROM_MAX_OR_MIN_POSITION_NOT_TO_ROTATE.times(-1));
	public static final Rotation2d SCREW_MIN_RANGE_EDGE = TurretCalculations
		.getRangeEdge(TurretConstants.MIN_POSITION, ShooterConstants.MAX_DISTANCE_FROM_MAX_OR_MIN_POSITION_NOT_TO_ROTATE);

}
