package frc.utils;

import edu.wpi.first.wpilibj.DriverStation;
import frc.utils.driverstation.GameSpecificMessageResponse;
import frc.utils.driverstation.DriverStationUtil;
import frc.utils.time.TimeUtil;
import frc.utils.alerts.Alert;

public class HubUtil {

	private static DriverStation.Alliance autoWinnerAlliance = getAutoWinningAlliance();
	private static DriverStation.Alliance autoLosingAlliance = getAutoLosingAlliance();

	private static DriverStation.Alliance getAutoWinningAlliance() {
		String gameData = DriverStation.getGameSpecificMessage();
		if (gameData.isEmpty() && !DriverStation.isAutonomous()) {
			return alertWarningForEmptyAlliance("Unknown auto winner alliance");
		}
		DriverStation.Alliance alliance = switch (GameSpecificMessageResponse.fromChar(gameData.charAt(0))) {
			case BLUE -> DriverStation.Alliance.Blue;
			case RED -> DriverStation.Alliance.Red;
			case DEFAULT -> null;
		};
		if (alliance == null && !DriverStation.isAutonomous()) {
			return alertWarningForEmptyAlliance("Didn't get auto winning alliance");
		}
		return alliance;
	}

	private static DriverStation.Alliance getAutoLosingAlliance() {
		if (autoWinnerAlliance == null) {
			return null;
		}
		return switch (autoWinnerAlliance) {
			case Red -> DriverStation.Alliance.Blue;
			case Blue -> DriverStation.Alliance.Red;
		};
	}

	public static DriverStation.Alliance alertWarningForEmptyAlliance(String name) {
		new Alert(Alert.AlertType.WARNING, name).report();
		return null;
	}

	public static void refreshAlliances() {
		if (autoWinnerAlliance == null && DriverStationUtil.isTeleop()) {
			autoWinnerAlliance = getAutoWinningAlliance();
			autoLosingAlliance = getAutoLosingAlliance();
		}
	}

	public static DriverStation.Alliance getAutoWinnerAlliance() {
		return autoWinnerAlliance;
	}

	public static DriverStation.Alliance getAutoLoserAlliance() {
		return autoLosingAlliance;
	}

	public static boolean isAutoWinnerShift() {
		return getShiftsPassed() % 2 != 0;
	}

	public static int getShiftsPassed() {
		if (
			TimeUtil.getTimeSinceTeleopInitSeconds() >= GamePeriodUtils.GAME_DURATION_SECONDS
				|| TimeUtil.getTimeSinceTeleopInitSeconds() >= GamePeriodUtils.TELEOP_DURATION_SECONDS
		) {
			return (GamePeriodUtils.TELEOP_DURATION_SECONDS - GamePeriodUtils.TRANSITION_SHIFT_DURATION_SECONDS)
				/ GamePeriodUtils.ALLIANCE_SHIFT_DURATION_SECONDS;
		} else {
			return (int) (TimeUtil.getTimeSinceTeleopInitSeconds() - GamePeriodUtils.TRANSITION_SHIFT_DURATION_SECONDS)
				/ GamePeriodUtils.ALLIANCE_SHIFT_DURATION_SECONDS;
		}
	}

	public static DriverStation.Alliance getActiveHub() {
		if (DriverStation.isAutonomous()) {
			return DriverStationUtil.getAlliance();
		} else if (!DriverStationUtil.isTeleop() || GamePeriodUtils.hasGameEnded()) {
			return DriverStationUtil.DEFAULT_ALLIANCE;
		} else if (GamePeriodUtils.isTransitionShift() || GamePeriodUtils.hasEndGameStarted()) {
			return DriverStationUtil.getAlliance();
		}

		return isAutoWinnerShift() ? autoWinnerAlliance : autoLosingAlliance;
	}

	public static boolean isOurHubActive() {
		if (getActiveHub() == null) {
			return false;
		}
		return getActiveHub().equals(DriverStationUtil.getAlliance());
	}

	public static double timeUntilCurrentShiftEndsSeconds() {
		if (
			TimeUtil.getTimeSinceTeleopInitSeconds() >= GamePeriodUtils.GAME_DURATION_SECONDS || TimeUtil.getTimeSinceTeleopInitSeconds() == -1
		) {
			return 0;
		}
		if (GamePeriodUtils.isTransitionShift()) {
			return GamePeriodUtils.getTimeUntilTransitionShiftEnds();
		} else if (GamePeriodUtils.hasEndGameStarted()) {
			return GamePeriodUtils.getTimeUntilGameEnds();
		}
		return GamePeriodUtils.getTimeUntilShiftEnds();
	}

	public static double getTimeLeftUntilActive() {
		if (isOurHubActive()) {
			return 0;
		}
		return timeUntilCurrentShiftEndsSeconds();
	}

	public static double getTimeLeftUntilInactive() {
		if (!isOurHubActive() || GamePeriodUtils.hasGameEnded()) {
			return 0;
		}
		return timeUntilCurrentShiftEndsSeconds();
	}

	public static boolean isRobotAllianceAutoWinner() {
		return DriverStationUtil.getAlliance() == autoWinnerAlliance;
	}

}
