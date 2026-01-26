package frc.robot.subsystems.constants.train;

import com.ctre.phoenix6.configs.Slot0Configs;
import frc.robot.RobotConstants;

public class TrainConstant {

	public static final String LOG_PATH = RobotConstants.SUBSYSTEM_LOGPATH_PREFIX + "/Train";
	public static final String FUNNEL_INPUT_NAME = "FunnelSensor";
	public static final double GEAR_RATIO = 25.0 / 6.0;
	public static final int CURRENT_LIMIT = 20;
	public static final double MOMENT_OF_INERTIA = 0.001;

	public static final Slot0Configs REAL_SLOTS_CONFIG = new Slot0Configs();
	public static final Slot0Configs SIMULATION_SLOTS_CONFIG = new Slot0Configs();

	static {
		REAL_SLOTS_CONFIG.kP = 0;
		REAL_SLOTS_CONFIG.kI = 0;
		REAL_SLOTS_CONFIG.kD = 0;
		REAL_SLOTS_CONFIG.kG = 0;
		REAL_SLOTS_CONFIG.kS = 0;
		REAL_SLOTS_CONFIG.kV = 0.52;
		REAL_SLOTS_CONFIG.kA = 0;

		SIMULATION_SLOTS_CONFIG.kP = 0;
		SIMULATION_SLOTS_CONFIG.kI = 0;
		SIMULATION_SLOTS_CONFIG.kD = 0;
		SIMULATION_SLOTS_CONFIG.kG = 0;
		SIMULATION_SLOTS_CONFIG.kS = 0;
		SIMULATION_SLOTS_CONFIG.kV = 0.52;
		SIMULATION_SLOTS_CONFIG.kA = 0;
	}
	public static final boolean IS_INVERTED = false;

}
