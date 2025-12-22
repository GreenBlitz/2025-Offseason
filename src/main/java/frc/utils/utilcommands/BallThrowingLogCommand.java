package frc.utils.utilcommands;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotConstants;
import frc.robot.statemachine.StateMachineConstants;
import frc.robot.subsystems.constants.flywheel.Constants;
import frc.utils.math.FieldMath;
import frc.utils.time.TimeUtil;
import org.littletonrobotics.junction.Logger;

public class BallThrowingLogCommand extends Command {

	private String logPath;
	private int ballIndex;
	private double startTimeSeconds;

	private double initialVelocityOnTheXAxisTurretRelative;
	private double initialVelocityOnTheYAxisTurretRelative;
	private double initialVelocityOnTheZAxisTurretRelative;

	private Pose3d initialTurretPose;
	private Pose3d initialRobotPose;

	private Pose3d finalPose;

	public BallThrowingLogCommand(
		String logPath,
		int ballIndex,
		Rotation2d hoodAngle,
		Pose3d initialTurretPose,
		Rotation2d flywheelVelocity,
		ChassisSpeeds robotSpeeds,
		Pose2d initialRobotPose
	) {
		this.logPath = logPath;
		this.ballIndex = ballIndex;

		Transform3d turretFix = new Transform3d(
			new Pose3d(),
			new Pose3d(0, 0, 0, new Rotation3d(0, 0, Rotation2d.fromDegrees(-90).getRadians()))
		);
		this.initialTurretPose = initialTurretPose.plus(turretFix);
		Transform2d turretToRobot = new Transform2d(new Pose2d(), initialRobotPose);
		Translation2d turretRelativeRobotVelocity = FieldMath.getRelativeTranslation(
			new Pose2d(0, 0, initialTurretPose.toPose2d().plus(turretToRobot).getRotation()),
			new Translation2d(robotSpeeds.vxMetersPerSecond, robotSpeeds.vyMetersPerSecond)
		);

		this.initialVelocityOnTheXAxisTurretRelative = (flywheelVelocity.getRadians() * Constants.WHEEL_RADIUS_METERS) * hoodAngle.getCos()
			+ turretRelativeRobotVelocity.getY();
		this.initialVelocityOnTheYAxisTurretRelative = turretRelativeRobotVelocity.getX();
		this.initialVelocityOnTheZAxisTurretRelative = (flywheelVelocity.getRadians() * Constants.WHEEL_RADIUS_METERS) * hoodAngle.getSin();

		this.initialRobotPose = new Pose3d(initialRobotPose);
	}

	@Override
	public void initialize() {
		this.startTimeSeconds = TimeUtil.getCurrentTimeSeconds();
	}

	@Override
	public void execute() {
		double timePassed = TimeUtil.getCurrentTimeSeconds() - startTimeSeconds;
		Pose3d turretRelativeBallPose = getTurretRelativeBallPose(timePassed);

		Transform3d turretToRobot = new Transform3d(new Pose3d(), initialTurretPose);
		Transform3d ballToTurret = new Transform3d(new Pose3d(), turretRelativeBallPose);
		Pose3d fieldRelativeBallPose = initialRobotPose.plus(turretToRobot).plus(ballToTurret);

		if (fieldRelativeBallPose.getZ() <= StateMachineConstants.DISTANCE_FROM_FLOOR_TO_STOP_BALL_SIMULATION_METERS) {
			if (finalPose == null && timePassed > StateMachineConstants.MINIMAL_TIME_AFTER_THROW_TO_STOP_BALL_SIMULATION_SECONDS) {
				finalPose = fieldRelativeBallPose;
			}
			Logger.recordOutput(logPath + "/Ball" + ballIndex + 1, finalPose);
		} else {
			Logger.recordOutput(logPath + "/Ball" + ballIndex + 1, fieldRelativeBallPose);
		}
	}

	private Pose3d getTurretRelativeBallPose(double timePassed) {
		double turretRelativeXPosition = initialVelocityOnTheXAxisTurretRelative * timePassed;
		double turretRelativeYPosition = initialVelocityOnTheYAxisTurretRelative * timePassed;
		double turretRelativeZPosition = initialVelocityOnTheZAxisTurretRelative * timePassed
			+ (-RobotConstants.GRAVITATIONAL_ACCELERATION_METERS_PER_SECOND_SQUARED_ISRAEL * Math.pow(timePassed, 2)) / 2;

		Pose3d turretRelativeBallPose = new Pose3d(turretRelativeXPosition, turretRelativeYPosition, turretRelativeZPosition, new Rotation3d());
		return turretRelativeBallPose;
	}

}
