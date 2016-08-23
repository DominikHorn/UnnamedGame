package com.unnamedgame.main;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.util.math.*;

public class EntityFactory {
	public static Entity getStallEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		try {
			// Create entity
			Entity stallEntity = new Entity(position, rotX, rotY, rotZ, scale);

			// Add all the components
			stallEntity.addComponent(new RotatingComponent());
			stallEntity.addComponent(
					new TexturedRenderComponent("res/tex/stall.png", "res/model/stall.obj", new StaticShader()));

			return stallEntity;
		} catch (Exception e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not create stall entity");
		}
		return null;
	}
}
