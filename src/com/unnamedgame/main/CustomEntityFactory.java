package com.unnamedgame.main;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.defaultcomponents.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;
import com.unnamedgame.components.*;
import com.unnamedgame.materials.*;

public class CustomEntityFactory {
	private static Random random = new Random();
	private static float ROTATION_SPEED_SCALE = 3f;

	private static Map<String, TexturedModel> models = new HashMap<>();

	public static void load() {
		TexturedModel stallModel = Engine.getModelManager().loadTexturedModel("res/model/stall.obj");
		stallModel.setMaterial(new StallMaterial());
		TexturedModel dragonModel = Engine.getModelManager().loadTexturedModel("res/model/dragon.obj");
		dragonModel.setMaterial(new DragonMaterial());
		
		models.put("stall", stallModel);
		models.put("dragon", dragonModel);
	}

	public static Entity getStallEntity(Vector3f position) {
		try {
			Vector3f rotation = new Vector3f();
			rotation.x = random.nextFloat() * ROTATION_SPEED_SCALE;
			rotation.y = random.nextFloat() * ROTATION_SPEED_SCALE;
			rotation.z = random.nextFloat() * ROTATION_SPEED_SCALE;

			Entity e = new Entity(position);
			e.addComponent(new RotatingComponent(rotation));
			e.putProperty(DefaultEntityProperties.PROPERTY_MODEL, models.get("stall"));

			return e;
		} catch (Exception e) {
			e.printStackTrace();
			Engine.getLogger().err("Could not create stall entity");
		}
		return null;
	}

	public static Entity getDragonEntity(Vector3f position) {
		try {
			Vector3f rotation = new Vector3f();
			rotation.x = 0;
			rotation.y = random.nextFloat() * ROTATION_SPEED_SCALE;
			rotation.z = 0;
			Entity e = new Entity(position);
			e.addComponent(new RotatingComponent(rotation));
			e.putProperty(DefaultEntityProperties.PROPERTY_MODEL, models.get("dragon"));

			return e;
		} catch (Exception e) {
			e.printStackTrace();
			Engine.getLogger().err("Could not create dragon entity");
		}
		return null;
	}

	public static Entity getCamera(Vector3f position) {
		return new Entity(position).addComponent(new CameraInputComponent(1)).addComponent(new CameraComponent());
	}

	// TODO: refactor terrain code
	public static Entity getTerrainChunk(int gridX, int gridZ, Shader terrainShader) {
		Entity e = new Entity(new Vector3f(gridX, 0, gridZ));
		e.addComponent(new TerrainChunkComponent(terrainShader));
		return e;
	}

	public static void cleanup() {
		models.values().forEach(m -> m.cleanup());
	}
}
