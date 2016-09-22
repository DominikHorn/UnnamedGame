package com.unnamedgame.main;

import java.util.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.math.*;
import com.unnamedgame.components.*;
import com.unnamedgame.materials.*;
import com.unnamedgame.shaders.*;

public class EntityFactory {
	private static Random random = new Random();
	private static float ROTATION_FLOAT_SCALE = (float) (180.0 / Math.PI);

	private static DynamicShader standardShader;

	private static Map<String, TexturedModel<?>> models = new HashMap<>();

	public static void load() {
		// TODO: refactor
		GL11.glClearColor(UnnamedGame.SKY_COLOR.x, UnnamedGame.SKY_COLOR.y, UnnamedGame.SKY_COLOR.z, 1.0f);

		standardShader = new DynamicShader();

		ModelData fernModelData = Engine.getModelDataManager().loadModelData(UnnamedGame.MODEL_FOLDER + "fern.obj");
		TexturedModel<?> fernModel = new TexturedModel<>(UnnamedGame.TEX_FOLDER + "fern.png", 2, standardShader,
				new DynamicMaterial(0, 1, true, false), fernModelData);
		fernModelData.cleanup();

		ModelData lowPolyTreeModelData = Engine.getModelDataManager()
				.loadModelData(UnnamedGame.MODEL_FOLDER + "lowPolyTree.obj");
		TexturedModel<?> lowPolyTreeModel = new TexturedModel<>(UnnamedGame.TEX_FOLDER + "lowPolyTree.png",
				standardShader, new DynamicMaterial(0, 1, false, false), lowPolyTreeModelData);
		lowPolyTreeModelData.cleanup();

		ModelData playerModelData = Engine.getModelDataManager().loadModelData(UnnamedGame.MODEL_FOLDER + "person.obj");
		TexturedModel<?> playerModel = new TexturedModel<>(UnnamedGame.TEX_FOLDER + "playerTexture.png", standardShader,
				new DynamicMaterial(0, 1, false, false), playerModelData);
		playerModelData.cleanup();

		ModelData mushroomModelData = Engine.getModelDataManager()
				.loadModelData(UnnamedGame.MODEL_FOLDER + "mushroom.obj");
		TexturedModel<?> mushroomModel = new TexturedModel<>(UnnamedGame.TEX_FOLDER + "mushroom.png", standardShader,
				new DynamicMaterial(0, 1, false, false), mushroomModelData);
		mushroomModelData.cleanup();

		models.put("fern", fernModel);
		models.put("tree", lowPolyTreeModel);
		models.put("player", playerModel);
		models.put("mushroom", mushroomModel);
	}

	public static RenderableEntity<?> getEntityByName(Vector3f position, Vector3f scale, String modelname) {
		Vector3f rotation = new Vector3f();
		rotation.x = 0;
		rotation.y = random.nextFloat() * 360 * ROTATION_FLOAT_SCALE;
		rotation.z = 0;
		RenderableEntity<?> e = new RenderableEntity<>(models.get(modelname), position, rotation, scale);

		return e;
	}

	public static RenderableEntity<?> getPlayerEntity(Vector3f position, Vector3f scale) {
		RenderableEntity<?> e = new RenderableEntity<>(models.get("player"), position, new Vector3f(), scale);
		e.addComponent(new PlayerPhysicsComponent());
		e.addComponent(new SpotLightFollowComponent());
		return e;
	}

	public static RenderableEntity<?> getRandomEntity(Vector3f position) {
		int randHashKeyPos = random.nextInt(models.keySet().size());
		String modelname = new ArrayList<String>(models.keySet()).get(randHashKeyPos);
		return getEntityByName(position, new Vector3f(1, 1, 1), modelname);
	}

	public static void cleanup() {
		models.values().forEach(m -> m.cleanup());
		standardShader.forceDelete();
	}
}
