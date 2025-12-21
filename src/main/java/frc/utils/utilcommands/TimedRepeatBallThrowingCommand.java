package frc.utils.utilcommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.utils.time.TimeUtil;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

import java.util.function.Supplier;

public class TimedRepeatBallThrowingCommand extends Command {

	private final String logPath;
	private final Supplier<Rotation2d> hoodAngle;
	private final Supplier<Pose3d> turretPose3dAtStartOfThrow;
	private final Supplier<Rotation2d> flywheelVelocity;
	private final Supplier<ChassisSpeeds> robotSpeeds;
	private final Supplier<Pose2d> robotPose2dAtStartOfThrow;
	private final double timeInterval;
	private final LoggedNetworkNumber numberOfBallsInFunnel;
	private int ballsThrown;
	private double timeAtStartOfThrow;

	public TimedRepeatBallThrowingCommand(
		String logPath,
		Supplier<Rotation2d> hoodAngle,
		Supplier<Pose3d> turretPose3dAtStartOfThrow,
		Supplier<Rotation2d> flywheelVelocity,
		Supplier<ChassisSpeeds> robotSpeeds,
		Supplier<Pose2d> robotPose2dAtStartOfThrow,
		double timeInterval,
		LoggedNetworkNumber numberOfBallsInFunnel
	) {
		this.logPath = logPath;
		this.hoodAngle = hoodAngle;
		this.turretPose3dAtStartOfThrow = turretPose3dAtStartOfThrow;
		this.flywheelVelocity = flywheelVelocity;
		this.robotSpeeds = robotSpeeds;
		this.robotPose2dAtStartOfThrow = robotPose2dAtStartOfThrow;
		this.timeInterval = timeInterval;
		this.numberOfBallsInFunnel = numberOfBallsInFunnel;
		this.ballsThrown = 0;
		this.timeAtStartOfThrow = TimeUtil.getCurrentTimeSeconds();
	}

	@Override
	public void execute() {
		if (TimeUtil.getCurrentTimeSeconds() > timeAtStartOfThrow + timeInterval) {
			new BallThrowingLogCommand(
				logPath,
				ballsThrown + 1,
				hoodAngle.get(),
				TimeUtil.getCurrentTimeSeconds(),
				turretPose3dAtStartOfThrow.get(),
				flywheelVelocity.get(),
				robotSpeeds.get(),
				robotPose2dAtStartOfThrow.get()
			).schedule();
			timeAtStartOfThrow = TimeUtil.getCurrentTimeSeconds();
			ballsThrown++;
		}
	}

	@Override
	public boolean isFinished() {
		return ballsThrown >= numberOfBallsInFunnel.get();
	}

	@Override
	public void end(boolean interrupted) {
		numberOfBallsInFunnel.set(0);
	}

}
