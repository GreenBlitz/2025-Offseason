package frc.utils;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.constants.field.Field;
import frc.robot.subsystems.constants.turret.TurretConstants;
import frc.utils.math.FieldMath;
import frc.utils.math.ToleranceMath;

public class TurretCalculations {

	public static Pose2d getFieldRelativeTurretPosition(Pose2d robotPose, Rotation2d turretAngle) {
		Translation2d turretPositionRelativeToRobotRelativeToField = TurretConstants.TURRET_POSITION_RELATIVE_TO_ROBOT.toTranslation2d()
			.rotateBy(robotPose.getRotation());
		return new Pose2d(
			new Translation2d(
				robotPose.getX() + turretPositionRelativeToRobotRelativeToField.getX(),
				robotPose.getY() + turretPositionRelativeToRobotRelativeToField.getY()
			),
			Rotation2d.fromDegrees(robotPose.getRotation().getDegrees() + turretAngle.getDegrees())
		);
	}

	public static boolean isTurretMoveLegal(Rotation2d targetRobotRelative, Rotation2d position) {
		boolean isTargetInMaxRange = !(targetRobotRelative.getDegrees() > TurretConstants.SCREW_MAX_RANGE_EDGE.getDegrees()
			&& position.getDegrees() < TurretConstants.SCREW_MIN_RANGE_EDGE.getDegrees());

		boolean isTargetInMinRange = !(targetRobotRelative.getDegrees() < TurretConstants.SCREW_MIN_RANGE_EDGE.getDegrees()
			&& position.getDegrees() > TurretConstants.SCREW_MAX_RANGE_EDGE.getDegrees());

		boolean isTargetBehindSoftwareLimits = ToleranceMath.isInRange(
			targetRobotRelative.getDegrees(),
			TurretConstants.BACKWARDS_SOFTWARE_LIMIT.getDegrees(),
			TurretConstants.FORWARD_SOFTWARE_LIMIT.getDegrees()
		);

		return isTargetInMaxRange && isTargetInMinRange && isTargetBehindSoftwareLimits;
	}

	public static Rotation2d getRobotRelativeLookAtHubAngleForTurret(Pose2d robotPose, Rotation2d turretPosition) {
		Translation2d fieldRelativeTurretPose = getFieldRelativeTurretPosition(robotPose, turretPosition).getTranslation();
		Rotation2d targetAngle = Rotation2d.fromDegrees(
			FieldMath.getRelativeTranslation(fieldRelativeTurretPose, Field.getHubMiddle()).getAngle().getDegrees()
				- robotPose.getRotation().getDegrees()
		);
		return Rotation2d.fromRadians(
			MathUtil.inputModulus(targetAngle.getRadians(), TurretConstants.MIN_POSITION.getRadians(), TurretConstants.MAX_POSITION.getRadians())
		);
	}

	public static Rotation2d getRangeEdge(Rotation2d angle, Rotation2d tolerance) {
		return Rotation2d.fromRadians(
			MathUtil.inputModulus(
				angle.getRadians() + tolerance.getRadians(),
				TurretConstants.MIN_POSITION.getRadians(),
				TurretConstants.MAX_POSITION.getRadians()
			)
		);
	}

}
