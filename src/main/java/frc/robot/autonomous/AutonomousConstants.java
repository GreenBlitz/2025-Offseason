package frc.robot.autonomous;

import com.pathplanner.lib.path.PathConstraints;

public class AutonomousConstants {

	public static final String LOG_PATH_PREFIX = "Autonomous";

	public static final double PATHFINDING_DEADBAND_METERS = 0.5;

	public static final PathConstraints PATH_CONSTRAINTS = new PathConstraints(1, 1, 2, 2);

}
