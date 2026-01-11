package frc.robot.statemachine.shooterstatehandler;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.interpolation.InverseInterpolator;
import frc.utils.InterpolationMap;
import frc.utils.LoggedNetworkRotation2d;

import java.util.Map;

public class ShooterConstants {

	public static final Rotation2d DEFAULT_FLYWHEEL_ROTATIONS_PER_SECOND = Rotation2d.fromRotations(25);

	public static final InterpolationMap<Double, Rotation2d> HOOD_INTERPOLATION_MAP = new InterpolationMap<Double, Rotation2d>(
		InverseInterpolator.forDouble(),
		InterpolationMap.interpolatorForRotation2d(),
		Map.of(
			1.07,
			Rotation2d.fromDegrees(43),
			3.03,
			Rotation2d.fromDegrees(30),
			4.48,
			Rotation2d.fromDegrees(30),
			5.48,
			Rotation2d.fromDegrees(30)
		)
	);

	public static final InterpolationMap<Double, Rotation2d> FLYWHEEL_INTERPOLATION_MAP = new InterpolationMap<Double, Rotation2d>(
		InverseInterpolator.forDouble(),
		InterpolationMap.interpolatorForRotation2d(),
		Map.of(
			1.07,
			Rotation2d.fromRotations(70),
			3.03,
			Rotation2d.fromRotations(76),
			4.48,
			Rotation2d.fromRotations(86),
			5.48,
			Rotation2d.fromRotations(105)
		)
	);

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

}
