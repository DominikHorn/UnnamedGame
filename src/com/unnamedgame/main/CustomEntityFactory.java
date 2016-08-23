package com.unnamedgame.main;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.util.math.*;

public class CustomEntityFactory {
	public static Entity getStallEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		try {
			return new Entity(position, rotX, rotY, rotZ, scale).addComponent(new RotatingComponent())
					.addComponent(new TexturedRenderComponent("res/tex/stall.png", "res/model/stall.obj",
							new StaticShader("res/shader/vertex.glsl", "res/shader/fragment.glsl")));
		} catch (Exception e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not create stall entity");
		}
		return null;
	}
}
