package frc.robot.autonomous;

import frc.utils.auto.PathPlannerAutoWrapper;

import java.util.List;
import java.util.function.Supplier;

public class AutosBuilder {

	public static List<Supplier<PathPlannerAutoWrapper>> getAllTestAutos() {
		return List.of(
			() -> new PathPlannerAutoWrapper("Rotate"),
			() -> new PathPlannerAutoWrapper("Straight 2m")
		);
	}

    public static List<Supplier<PathPlannerAutoWrapper>> getAllStartTest() {
		return List.of(
			() -> new PathPlannerAutoWrapper("Rotate 2m")
		);
	}

}
