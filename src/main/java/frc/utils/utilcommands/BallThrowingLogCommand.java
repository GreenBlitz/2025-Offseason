package frc.utils.utilcommands;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.constants.flywheel.Constants;
import frc.utils.math.FieldMath;
import frc.utils.time.TimeUtil;
import org.littletonrobotics.junction.Logger;

public class BallThrowingLogCommand extends Command {

	private String logPath;
	private int ballIndex;
	private double timeAtStartOfThrowSeconds;
	private Pose3d turretPose3dAtStartOfThrow;

	private double initialVelocityOnTheXAxis;
	private double initialVelocityOnTheYAxis;
	private double initialVelocityOnTheZAxis;

	private Pose3d robotPose3dAtStartOfThrow;

	private Pose3d finalPose;

	public BallThrowingLogCommand(
		String logPath,
		int ballIndex,
		Rotation2d hoodAngle,
		double timeAtStartOfThrowSeconds,
		Pose3d turretPose3dAtStartOfThrow,
		Rotation2d flywheelVelocity,
		ChassisSpeeds robotSpeeds,
		Pose2d robotPose2dAtStartOfThrow
	) {
		this.logPath = logPath;
		this.ballIndex = ballIndex;
		this.timeAtStartOfThrowSeconds = timeAtStartOfThrowSeconds;
		Transform3d turretFix = new Transform3d(
			new Pose3d(),
			new Pose3d(0, 0, 0, new Rotation3d(0, 0, Rotation2d.fromDegrees(-90).getRadians()))
		);
		this.turretPose3dAtStartOfThrow = turretPose3dAtStartOfThrow.plus(turretFix);

		Transform2d turretToRobot = new Transform2d(new Pose2d(), robotPose2dAtStartOfThrow);

		Translation2d turretRelativeRobotVelocity = FieldMath.getRelativeTranslation(
				new Pose2d(0, 0, turretPose3dAtStartOfThrow.toPose2d().plus(turretToRobot).getRotation()),
			new Translation2d(robotSpeeds.vxMetersPerSecond, robotSpeeds.vyMetersPerSecond)
		);

		this.initialVelocityOnTheXAxis = (120 * Constants.WHEEL_RADIUS_METERS) * hoodAngle.getCos() + turretRelativeRobotVelocity.getY();

		this.initialVelocityOnTheYAxis = turretRelativeRobotVelocity.getX();

		this.initialVelocityOnTheZAxis = (120 * Constants.WHEEL_RADIUS_METERS) * hoodAngle.getSin();

		this.robotPose3dAtStartOfThrow = new Pose3d(robotPose2dAtStartOfThrow);
	}

	@Override
	public void execute() {
		double timePassed = TimeUtil.getCurrentTimeSeconds() - timeAtStartOfThrowSeconds;
		double turretRelativeXPosition = initialVelocityOnTheXAxis * timePassed;
		double turretRelativeYPosition = initialVelocityOnTheYAxis * timePassed;
		double turretRelativeZPosition = initialVelocityOnTheZAxis * timePassed + (-10 * Math.pow(timePassed, 2)) / 2;

		Pose3d turretRelativeBallPose = new Pose3d(turretRelativeXPosition, turretRelativeYPosition, turretRelativeZPosition, new Rotation3d());

		Transform3d turretToRobot = new Transform3d(new Pose3d(), turretPose3dAtStartOfThrow);

		Transform3d ballToTurret = new Transform3d(new Pose3d(), turretRelativeBallPose);

		Pose3d fieldRelativeBallPose = robotPose3dAtStartOfThrow.plus(turretToRobot).plus(ballToTurret);

		if (fieldRelativeBallPose.getZ() <= 0.2) {
			if (finalPose == null && timePassed > 0.3) {
				finalPose = fieldRelativeBallPose;
			}
			Logger.recordOutput(logPath + "/Ball" + ballIndex, finalPose);
		} else {
			Logger.recordOutput(logPath + "/Ball" + ballIndex, fieldRelativeBallPose);
		}
	}

	@Override
	public void end(boolean interrupted) {
//		Logger.recordOutput(logPath + "/Ball" + ballIndex, new Pose3d(-1, -1, -1, new Rotation3d()));
	}

}
