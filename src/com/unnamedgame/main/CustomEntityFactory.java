package com.unnamedgame.main;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.util.math.*;

public class CustomEntityFactory {
	public static VisibleEntity getStallEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		try {
			return new VisibleEntity(Engine.MODEL_MANAGER.getTexturedModel(Engine.MODEL_FOLDER + "stall.obj"), position,
					rotX, rotY, rotZ, scale).addComponent(new RotatingComponent(0, 0.4f, 0));
		} catch (Exception e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not create stall entity");
		}
		return null;
	}

	public static VisibleEntity getDragonEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		try {
			return new VisibleEntity(
					Engine.MODEL_MANAGER.getTexturedModel(Engine.MODEL_FOLDER + "dragon.obj"),
					position, rotX, rotY, rotZ, scale).addComponent(new RotatingComponent(0, 0.4f, 0));
		} catch (Exception e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not create stall entity");
		}
		return null;
	}
}
