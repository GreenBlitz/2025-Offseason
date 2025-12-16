package frc.constants.field;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public enum Tower {

	CLOSE_TOWER(new Pose2d(Field.Tower.getX() - 0.2, Field.Tower.getY(), Rotation2d.kZero)),
	FAR_TOWER(new Pose2d(Field.Tower.getX() - 0.2, Field.Tower.getY(), Rotation2d.k180deg)),
	RIGHT_TOWER(new Pose2d(Field.Tower.getX(), Field.Tower.getY() - 0.2, Rotation2d.kCCW_90deg.times(-1))),
	LEFT_TOWER(new Pose2d(Field.Tower.getX(), Field.Tower.getY() + 0.2, Rotation2d.kCCW_90deg));

	private final Pose2d tower;

	Tower(Pose2d tower) {
		this.tower = tower;
	}

	public Pose2d getPose() {
		return tower;
	}

}
