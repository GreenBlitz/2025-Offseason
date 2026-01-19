package frc.robot.statemachine.funnelstatehandler;

public enum FunnelState {

	STOP,
	INTAKE(3, 1),
	SHOOT(3, 1),
	SHOOT_WHILE_INTAKE(3, 1),
	DRIVE,
	CALIBRATION;

	private final double omniVoltage;
	private final double trainVoltage;

	FunnelState(double omniVoltage, double trainVoltage) {
		this.omniVoltage = omniVoltage;
		this.trainVoltage = trainVoltage;
	}

	FunnelState() {
		this.omniVoltage = 0;
		this.trainVoltage = 0;
	}

	public double getOmniVoltage() {
		return omniVoltage;
	}

	public double getTrainVoltage() {
		return trainVoltage;
	}

}
