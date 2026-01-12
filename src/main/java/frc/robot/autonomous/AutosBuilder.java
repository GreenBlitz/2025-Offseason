package frc.robot.autonomous;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.statemachine.RobotState;
import frc.utils.auto.PathHelper;
import frc.utils.auto.PathPlannerAutoWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AutosBuilder {

	public static List<Supplier<PathPlannerAutoWrapper>> getAllTestAutos() {
		return List.of(
			() -> new PathPlannerAutoWrapper("Rotate"),
			() -> new PathPlannerAutoWrapper("Rotate 2m"),
			() -> new PathPlannerAutoWrapper("Straight 2m")
		);
	}

	public static List<Supplier<PathPlannerAutoWrapper>> getAllPreBuiltAutos(Robot robot) {
		List<Supplier<PathPlannerAutoWrapper>> autos = new ArrayList<>();

		autos.add(() -> auto1(robot));

		return autos;
	}

	private static PathPlannerAutoWrapper auto1(Robot robot) {
		return new PathPlannerAutoWrapper(
			new SequentialCommandGroup(
				AutoBuilder.pathfindThenFollowPath(PathHelper.PATH_PLANNER_PATHS.get("a1-start-to-note1"), AutonomousConstants.PATH_CONSTRAINTS),
				new ParallelDeadlineGroup(
					AutoBuilder.followPath(PathHelper.PATH_PLANNER_PATHS.get("a1-note1-to-note2")),
					robot.getRobotCommander().getSuperstructure().setState(RobotState.DRIVE).asProxy()
				),
				new ParallelDeadlineGroup(
					AutoBuilder.followPath(PathHelper.PATH_PLANNER_PATHS.get("a1-note2-to-end")),
					robot.getRobotCommander().getSuperstructure().setState(RobotState.DRIVE).asProxy()
				)
			),
			new Pose2d(),
			"autonomous1"
		);
	}

}
