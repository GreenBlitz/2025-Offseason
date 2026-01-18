package frc.robot.hardware.phoenix6.request;

import com.ctre.phoenix6.controls.ControlRequest;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.hardware.interfaces.VelocityPositionRequest;

import java.util.function.Consumer;

public class Phoenix6VelocityPositionRequest extends Phoenix6FeedForwardRequest implements VelocityPositionRequest {

	private Consumer<Rotation2d> setVelocity;
	private Rotation2d velocity;
	private Consumer<Rotation2d> setPosition;
	private Rotation2d position;

	public Phoenix6VelocityPositionRequest(
		Consumer<Rotation2d> setVelocity,
		Rotation2d position,
		ControlRequest controlRequest,
		Consumer<Double> setFeedForward,
		double defaultArbitraryFeedForward,
		Rotation2d velocity

	) {
		super(position, controlRequest, setVelocity, setFeedForward, defaultArbitraryFeedForward);
		this.setVelocity = setVelocity;
		this.velocity = velocity;
	}

	public void withPosition(Rotation2d position) {
		this.position = position;
		setPosition.accept(position);
	}

	public Rotation2d getPosition() {
		return position;
	}


	@Override
	public VelocityPositionRequest setVelocity(Rotation2d velocity) {
		this.velocity = velocity;
		setVelocity.accept(velocity);
		return this;
	}

	@Override
	public Rotation2d getVelocity() {
		return velocity;
	}

}
