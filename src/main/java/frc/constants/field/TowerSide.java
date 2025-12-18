package frc.constants.field;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public enum TowerSide {

	CLOSE_TOWER(new Pose2d(Field.Tower.getX() - 0.4, Field.Tower.getY(), Rotation2d.k180deg)),
	FAR_TOWER(new Pose2d(Field.Tower.getX() + 0.4, Field.Tower.getY(), Rotation2d.kZero)),
	RIGHT_TOWER(new Pose2d(Field.Tower.getX(), Field.Tower.getY() - 0.4, Rotation2d.kCCW_90deg.times(-1))),
	LEFT_TOWER(new Pose2d(Field.Tower.getX(), Field.Tower.getY() + 0.4, Rotation2d.kCCW_90deg));

	private final Pose2d towerSide;

	TowerSide(Pose2d tower) {
		this.towerSide = tower;
	}

	public Pose2d getPose() {
		return towerSide;
	}

}
