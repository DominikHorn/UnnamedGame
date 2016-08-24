package com.unnamedgame.main;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.util.math.*;

public class CustomEntityFactory {
	private static Random random = new Random();
	private static float ROTATION_SPEED_SCALE = 20f;

	public static VisibleEntity getStallEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		try {
			float rotXDelta = random.nextFloat() * ROTATION_SPEED_SCALE;
			float rotYDelta = random.nextFloat() * ROTATION_SPEED_SCALE;
			float rotZDelta = random.nextFloat() * ROTATION_SPEED_SCALE;
			return new VisibleEntity(Engine.MODEL_MANAGER.getTexturedModel(Engine.MODEL_FOLDER + "stall.obj"), position,
					rotX, rotY, rotZ, scale).addComponent(new RotatingComponent(rotXDelta, rotYDelta, rotZDelta));
		} catch (Exception e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not create stall entity");
		}
		return null;
	}

	public static VisibleEntity getDragonEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		try {
			float rotXDelta = random.nextFloat() * ROTATION_SPEED_SCALE;
			float rotYDelta = random.nextFloat() * ROTATION_SPEED_SCALE;
			float rotZDelta = random.nextFloat() * ROTATION_SPEED_SCALE;
			return new VisibleEntity(Engine.MODEL_MANAGER.getTexturedModel(Engine.MODEL_FOLDER + "dragon.obj"),
					position, rotX, rotY, rotZ, scale)
							.addComponent(new RotatingComponent(rotXDelta, rotYDelta, rotZDelta));
		} catch (Exception e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not create stall entity");
		}
		return null;
	}
}
