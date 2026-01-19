package frc.robot.statemachine.funnelstatehandler;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.hardware.digitalinput.DigitalInputInputsAutoLogged;
import frc.robot.hardware.digitalinput.IDigitalInput;
import frc.robot.statemachine.StateMachineConstants;
import frc.robot.subsystems.roller.Roller;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

public class FunnelStateHandler {

	private final Roller omni;

	private final IDigitalInput sensor;
	private final DigitalInputInputsAutoLogged sensorInputsAutoLogged;

	private final Roller train;

	private final String logPath;

	private final LoggedNetworkNumber omniCalibrationVoltage;

	private final LoggedNetworkNumber trainCalibrationVoltage;

	protected FunnelState currentState;

	public FunnelStateHandler(Roller omni, Roller train, String logPath, IDigitalInput sensor) {
		this.omni = omni;
		this.train = train;
		this.sensor = sensor;
		this.logPath = logPath + "/FunnelStateHandler";
		this.currentState = FunnelState.STOP;
		this.omniCalibrationVoltage = new LoggedNetworkNumber("Tunable/OmniPower", 0);
		this.trainCalibrationVoltage = new LoggedNetworkNumber("Tunable/TrainPower", 0);
		this.sensorInputsAutoLogged = new DigitalInputInputsAutoLogged();
		Logger.recordOutput(logPath + "/CurrentState", currentState.name());
		sensor.updateInputs(sensorInputsAutoLogged);
	}

	public Command setState(FunnelState state) {
		Command command = switch (state) {
			case DRIVE -> drive();
			case SHOOT -> shoot();
			case SHOOT_WHILE_INTAKE -> shootWhileIntake();
			case INTAKE -> intake();
			case STOP -> stop();
			case CALIBRATION -> calibration();
		};
		return new ParallelCommandGroup(
			new InstantCommand(() -> Logger.recordOutput(logPath + "/CurrentState", state.name())),
			new InstantCommand(() -> currentState = state),
			command
		);
	}

	public boolean isBallAtSensor() {
		return sensorInputsAutoLogged.debouncedValue;
	}

	private Command drive() {
		return new ParallelCommandGroup(omni.getCommandsBuilder().stop(), train.getCommandsBuilder().stop());
	}

	private Command shoot() {
		return new ParallelCommandGroup(
			omni.getCommandsBuilder().setVoltage(FunnelState.SHOOT.getOmniVoltage()),
			new SequentialCommandGroup(
				new WaitCommand(StateMachineConstants.TIME_FOR_OMNI_TO_ACCELERATE_SECONDS),
				train.getCommandsBuilder().setVoltage(FunnelState.SHOOT.getTrainVoltage())
			)
		);
	}

	private Command shootWhileIntake() {
		return new ParallelCommandGroup(
			omni.getCommandsBuilder().setVoltage(FunnelState.SHOOT_WHILE_INTAKE.getOmniVoltage()),
			new SequentialCommandGroup(
				new WaitCommand(StateMachineConstants.TIME_FOR_OMNI_TO_ACCELERATE_SECONDS),
				train.getCommandsBuilder().setVoltage(FunnelState.SHOOT_WHILE_INTAKE.getTrainVoltage())
			)
		);
	}

	private Command intake() {
		return new SequentialCommandGroup(
			new ParallelCommandGroup(
				train.getCommandsBuilder().setVoltage(FunnelState.INTAKE.getTrainVoltage()),
				omni.getCommandsBuilder().setVoltage(FunnelState.INTAKE.getOmniVoltage())
			).until(() -> this.isBallAtSensor()),
			new ParallelCommandGroup(omni.getCommandsBuilder().stop(), train.getCommandsBuilder().stop())
		);
	}

	private Command stop() {
		return new ParallelCommandGroup(omni.getCommandsBuilder().stop(), train.getCommandsBuilder().stop());
	}

	private Command calibration() {
		return new ParallelCommandGroup(
			omni.getCommandsBuilder().setVoltage(() -> omniCalibrationVoltage.get()),
			train.getCommandsBuilder().setVoltage(() -> trainCalibrationVoltage.get())
		);
	}

	public void periodic() {
		sensor.updateInputs(sensorInputsAutoLogged);
		Logger.processInputs(logPath, sensorInputsAutoLogged);
	}

}
