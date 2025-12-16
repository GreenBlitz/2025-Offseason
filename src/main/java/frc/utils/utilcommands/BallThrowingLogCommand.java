package frc.utils.utilcommands;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotConstants;
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

	public BallThrowingLogCommand(
		String logPath,
		int ballIndex,
		Rotation2d hoodAngle,
		double timeAtStartOfThrowSeconds,
		Pose3d turretPose3dAtStartOfThrow,
		Rotation2d flywheelVelocity,
		ChassisSpeeds robotSpeeds
	) {
		this.logPath = logPath;
		this.ballIndex = ballIndex;
		this.timeAtStartOfThrowSeconds = timeAtStartOfThrowSeconds;
		this.turretPose3dAtStartOfThrow = turretPose3dAtStartOfThrow;

		Translation2d turretRelativeRobotVelocity = FieldMath.getRelativeTranslation(
			turretPose3dAtStartOfThrow.toPose2d(),
			new Translation2d(robotSpeeds.vxMetersPerSecond, robotSpeeds.vyMetersPerSecond)
		);

		this.initialVelocityOnTheXAxis = -(flywheelVelocity.getRotations() * Constants.WHEEL_RADIUS_METERS) * hoodAngle.getCos();
//			+ turretRelativeRobotVelocity.getX();
		this.initialVelocityOnTheYAxis = (flywheelVelocity.getRotations() * Constants.WHEEL_RADIUS_METERS) * hoodAngle.getSin();
//			+ turretRelativeRobotVelocity.getY();
	}

	@Override
	public void execute() {
		Logger.recordOutput("aaa", initialVelocityOnTheXAxis);
		double timePassed = TimeUtil.getCurrentTimeSeconds() - timeAtStartOfThrowSeconds;
		double turretRelativeXPosition = initialVelocityOnTheXAxis * timePassed;
		double turretRelativeYPosition = initialVelocityOnTheYAxis * timePassed
			+ (-0 * Math.pow(timePassed, 2)) / 2;

		Pose3d turretRelativeBallPose = new Pose3d(turretRelativeXPosition, turretRelativeYPosition, 0, new Rotation3d());

		Transform3d turretTransform = new Transform3d(new Pose3d(), turretPose3dAtStartOfThrow);

		Pose3d fieldRelativeBallPose = turretRelativeBallPose.plus(turretTransform);

		Logger.recordOutput(logPath + "/Ball" + ballIndex, fieldRelativeBallPose);
	}

	@Override
	public void end(boolean interrupted) {
//		Logger.recordOutput(logPath + "/Ball" + ballIndex, new Pose3d(-1, -1, -1, new Rotation3d()));
	}

}
