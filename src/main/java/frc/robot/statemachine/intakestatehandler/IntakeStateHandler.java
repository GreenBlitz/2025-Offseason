package frc.robot.statemachine.intakestatehandler;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.hardware.digitalinput.DigitalInputInputsAutoLogged;
import frc.robot.hardware.digitalinput.IDigitalInput;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.roller.Roller;
import frc.utils.LoggedNetworkRotation2d;
import frc.utils.time.TimeUtil;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

import java.util.Set;

public class IntakeStateHandler {

	private final Arm fourBar;
	private final Roller rollers;
	private final IDigitalInput beamBreaker;
	private final String logPath;
	private final DigitalInputInputsAutoLogged beamBreakerInputs;
	private final LoggedNetworkNumber rollersCalibrationPower = new LoggedNetworkNumber("Tunable/IntakeRollerPower");
	private final LoggedNetworkRotation2d fourBarCalibrationPosition = new LoggedNetworkRotation2d("Tunable/FourBarPosition", new Rotation2d());

	private IntakeState currentState;

	public IntakeStateHandler(Arm fourBar, Roller rollers, IDigitalInput beamBreaker, String logPath) {
		this.fourBar = fourBar;
		this.rollers = rollers;
		this.beamBreaker = beamBreaker;
		this.beamBreakerInputs = new DigitalInputInputsAutoLogged();
		this.logPath = logPath + "/IntakeStateHandler";
		this.currentState = IntakeState.STAY_IN_PLACE;
	}

	public void periodic() {
		beamBreaker.updateInputs(beamBreakerInputs);
		Logger.processInputs(logPath, beamBreakerInputs);
	}

	public Command calibration() {
		return new ParallelCommandGroup(
			fourBar.getCommandsBuilder().setTargetPosition(() -> fourBarCalibrationPosition.get()),
			rollers.getCommandsBuilder().setPower(() -> rollersCalibrationPower.get())
		);
	}

	public Command stayInPlace() {
		return new ParallelCommandGroup(fourBar.getCommandsBuilder().stayInPlace(), rollers.getCommandsBuilder().stop());
	}

	public IntakeState oppositeIntakeState() {
		if (currentState.equals(IntakeState.CLOSED)) {
			return IntakeState.INTAKE;
		}
		return IntakeState.CLOSED;
	}

	public Command openOrCloseIntake() {
		Command command = new ParallelCommandGroup(
				new DeferredCommand(
						() -> new ConditionalCommand(
								setState(IntakeState.CLOSED),
								setState(IntakeState.INTAKE),
								() -> currentState == IntakeState.INTAKE
						),
						Set.of(fourBar, rollers)
				),
				new RunCommand(() -> Logger.recordOutput("bbbb", TimeUtil.getCurrentTimeSeconds()))
		).withInterruptBehavior(Command.InterruptionBehavior.kCancelSelf);
		
	}

	public Command intake(IntakeState state) {
		return new ParallelCommandGroup(
			fourBar.getCommandsBuilder().setTargetPosition(state.getFourBarPosition()),
			rollers.getCommandsBuilder().setPower(state.getIntakePower())
		);
	}

	public Command setState(IntakeState intakeState) {
		return new ParallelCommandGroup(switch (intakeState) {
			case CALIBRATION -> calibration();
			case STAY_IN_PLACE -> stayInPlace();
			case CLOSED, INTAKE -> intake(intakeState);
		},
			new InstantCommand(() -> Logger.recordOutput(logPath + "/CurrentState", intakeState.name())),
			new InstantCommand(() -> currentState = intakeState)
		);
	}

	public IntakeState getCurrentState() {
		return currentState;
	}

}
