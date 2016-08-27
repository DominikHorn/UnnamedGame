package com.unnamedgame.main;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.defaultcomponents.*;
import com.openglengine.util.math.*;

public class CustomEntityFactory {
	private static Random random = new Random();
	private static float ROTATION_SPEED_SCALE = 3f;

	public static Entity getStallEntity(Vector3f position) {
		try {
			Vector3f rotation = new Vector3f();
			rotation.x = random.nextFloat() * ROTATION_SPEED_SCALE;
			rotation.y = random.nextFloat() * ROTATION_SPEED_SCALE;
			rotation.z = random.nextFloat() * ROTATION_SPEED_SCALE;
			return getNewDefaultEntity().addComponent(new RotatingComponent(rotation))
					.putProperty(DefaultEntityProperties.PROPERTY_MODEL,
							Engine.getModelManager().getTexturedModel("res/model/stall.obj"))
					.putProperty(DefaultEntityProperties.PROPERTY_POSITION, position)
					.putProperty(CustomEntityProperties.PROPERTY_REFLECTIVITY, 1f)
					.putProperty(CustomEntityProperties.PROPERTY_SHINE_DAMPER, 10f);
		} catch (Exception e) {
			e.printStackTrace();
			Engine.getLogger().err("Could not create stall entity");
		}
		return null;
	}

	public static Entity getDragonEntity(Vector3f position) {
		try {
			Vector3f rotation = new Vector3f();
			rotation.x = random.nextFloat() * ROTATION_SPEED_SCALE;
			rotation.y = random.nextFloat() * ROTATION_SPEED_SCALE;
			rotation.z = random.nextFloat() * ROTATION_SPEED_SCALE;
			return getNewDefaultEntity().addComponent(new RotatingComponent(rotation))
					.putProperty(DefaultEntityProperties.PROPERTY_MODEL,
							Engine.getModelManager().getTexturedModel("res/model/dragon.obj"))
					.putProperty(DefaultEntityProperties.PROPERTY_POSITION, position)
					.putProperty(CustomEntityProperties.PROPERTY_REFLECTIVITY, 1f)
					.putProperty(CustomEntityProperties.PROPERTY_SHINE_DAMPER, 10f);
		} catch (Exception e) {
			e.printStackTrace();
			Engine.getLogger().err("Could not create dragon entity");
		}
		return null;
	}

	public static Entity getCamera(Vector3f position) {
		return getNewDefaultEntity().addComponent(new CameraInputComponent(5)).addComponent(new CameraComponent())
				.putProperty(DefaultEntityProperties.PROPERTY_POSITION, position);
	}

	private static Entity getNewDefaultEntity() {
		return new Entity().putEmptyDefaultProperties();
	}
}
