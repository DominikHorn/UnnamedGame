package com.unnamedgame.main;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;

public class CustomEntityFactory {
	private static final String RES_FOLDER = "res/";
	private static final String SHADER_FOLDER = RES_FOLDER + "shader/";
	private static final String MODEL_FOLDER = RES_FOLDER + "model/";
	private static final String TEX_FOLDER = RES_FOLDER + "tex/";
	private static final Shader shader = new StaticShader(SHADER_FOLDER + "vertex.glsl",
			SHADER_FOLDER + "fragment.glsl", 10f, 1f);

	public static Entity getStallEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		try {
			return new Entity(position, rotX, rotY, rotZ, scale).addComponent(new RotatingComponent(0, 0.4f, 0))
					.addComponent(
							new TexturedRenderComponent(TEX_FOLDER + "stall.png", MODEL_FOLDER + "stall.obj", shader));
		} catch (Exception e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not create stall entity");
		}
		return null;
	}

	public static Entity getDragonEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		try {
			return new Entity(position, rotX, rotY, rotZ, scale).addComponent(new RotatingComponent(0, 0.4f, 0))
					.addComponent(new TexturedRenderComponent(TEX_FOLDER + "dragon.png", MODEL_FOLDER + "dragon.obj",
							shader));
		} catch (Exception e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not create stall entity");
		}
		return null;
	}
}
