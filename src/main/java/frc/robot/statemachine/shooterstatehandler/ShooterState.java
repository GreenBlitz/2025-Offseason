package frc.robot.statemachine.shooterstatehandler;

import edu.wpi.first.math.geometry.Rotation2d;

public enum ShooterState {

	STAY_IN_PLACE,
	IDLE,
	CLOSE_SHOOT(Rotation2d.fromDegrees(40), Rotation2d.fromRotations(70)),
	CLOSE_MIDDLE_SHOOT(Rotation2d.fromDegrees(27), Rotation2d.fromRotations(76)),
	FAR_MIDDLE_SHOOT(Rotation2d.fromDegrees(30), Rotation2d.fromRotations(86)),
	FAR_SHOOT(Rotation2d.fromDegrees(27), Rotation2d.fromRotations(105)),
	CALIBRATION;
	
	Rotation2d hoodAngle;
	Rotation2d flywheelSpeed;
	
	ShooterState(Rotation2d hoodAngle, Rotation2d flywheelSpeed){
		this.hoodAngle = hoodAngle;
		this.flywheelSpeed = flywheelSpeed;
	}
	
	ShooterState(){
	}
	
	public Rotation2d getHoodAngle() {
		return hoodAngle;
	}
	
	public Rotation2d getFlywheelSpeed() {
		return flywheelSpeed;
	}
}
