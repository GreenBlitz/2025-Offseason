package frc.robot.autonomous;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.utils.auto.PathHelper;
import frc.utils.auto.PathPlannerAutoWrapper;

import java.util.List;
import java.util.function.Supplier;

public class AutosBuilder {

	public static List<Supplier<PathPlannerAutoWrapper>> getAllTestAutos() {
		return List.of(() -> new PathPlannerAutoWrapper("Rotate"), () -> new PathPlannerAutoWrapper("Straight 2m"));
	}

	public static List<Supplier<PathPlannerAutoWrapper>> getAllStartTest() {
		return List.of(
			() -> new PathPlannerAutoWrapper(
				new SequentialCommandGroup(
					PathFollowingCommandsBuilder.followPath(PathHelper.PATH_PLANNER_PATHS.get("LookAtMiddle1")),
					PathFollowingCommandsBuilder.followPath(PathHelper.PATH_PLANNER_PATHS.get("FromReefToFeeder2")),
					PathFollowingCommandsBuilder.followPath(PathHelper.PATH_PLANNER_PATHS.get("FromFeederToReef3"))
				),
				Pose2d.kZero,
				"Start Of Autonomous"
			)
		);
	}

}
