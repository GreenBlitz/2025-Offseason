package frc.robot.autonomous;

import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.AngleUnit;
import frc.robot.Robot;
import frc.utils.auto.PathHelper;
import frc.utils.auto.PathPlannerAutoWrapper;
import frc.utils.auto.PathPlannerUtil;
import frc.utils.math.FieldMath;

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

    public static List<Supplier<PathPlannerAutoWrapper>> getFirstAutos(Robot robot, Supplier<Translation2d> closestBallCluster) {
        return List.of(
            () -> new PathPlannerAutoWrapper(PathPlannerUtil.createPathDuringRuntime(robot.getPoseEstimator().getEstimatedPose(),new Pose2d(closestBallCluster.get(), f,new PathConstraints(4.200,2.000,540.0,12.0)))
        );
    }

}
