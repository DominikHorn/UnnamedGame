package com.unnamedgame.main;

import java.util.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.defaultcomponents.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.math.*;
import com.unnamedgame.components.*;
import com.unnamedgame.materials.*;
import com.unnamedgame.models.*;
import com.unnamedgame.shaders.*;

public class EntityFactory {
	private static Random random = new Random();
	private static float ROTATION_FLOAT_SCALE = (float) (180.0 / Math.PI);

	private static DynamicShader standardShader;

	private static Map<String, SimpleTexturedModel> models = new HashMap<>();

	public static void load() {
		// TODO: refactor
		GL11.glClearColor(UnnamedGame.SKY_COLOR.x, UnnamedGame.SKY_COLOR.y, UnnamedGame.SKY_COLOR.z, 1.0f);

		standardShader = new DynamicShader();
		standardShader.compileShaderFromFiles(UnnamedGame.SHADER_FOLDER + "standard_vertex.glsl",
				UnnamedGame.SHADER_FOLDER + "standard_fragment.glsl");

		ModelData fernModelData = Engine.getModelDataManager().loadModelData(UnnamedGame.MODEL_FOLDER + "fern.obj");
		SimpleTexturedModel fernModel = new SimpleTexturedModel(UnnamedGame.TEX_FOLDER + "fern.png", standardShader,
				new DynamicMaterial(0, 1, true, false), fernModelData.getVertices(), fernModelData.getTextureCoords(),
				fernModelData.getNormals(), fernModelData.getIndices());
		fernModelData.cleanup();

		ModelData lowPolyTreeModelData = Engine.getModelDataManager()
				.loadModelData(UnnamedGame.MODEL_FOLDER + "lowPolyTree.obj");
		SimpleTexturedModel lowPolyTreeModel = new SimpleTexturedModel(UnnamedGame.TEX_FOLDER + "lowPolyTree.png",
				standardShader,
				new DynamicMaterial(0, 1, false, false), lowPolyTreeModelData.getVertices(),
				lowPolyTreeModelData.getTextureCoords(), lowPolyTreeModelData.getNormals(),
				lowPolyTreeModelData.getIndices());
		lowPolyTreeModelData.cleanup();

		ModelData playerModelData = Engine.getModelDataManager().loadModelData(UnnamedGame.MODEL_FOLDER + "person.obj");
		SimpleTexturedModel playerModel = new SimpleTexturedModel(UnnamedGame.TEX_FOLDER + "playerTexture.png",
				standardShader,
				new DynamicMaterial(0, 1, false, true), playerModelData.getVertices(),
				playerModelData.getTextureCoords(), playerModelData.getNormals(), playerModelData.getIndices());
		playerModelData.cleanup();

		models.put("fern", fernModel);
		models.put("tree", lowPolyTreeModel);
		models.put("player", playerModel);
	}

	public static RenderableEntity getEntityByName(Vector3f position, Vector3f scale, String modelname) {
		Vector3f rotation = new Vector3f();
		rotation.x = 0;
		rotation.y = random.nextFloat() * 360 * ROTATION_FLOAT_SCALE;
		rotation.z = 0;
		RenderableEntity e = new RenderableEntity(models.get(modelname), position, rotation, scale);

		return e;
	}

	public static RenderableEntity getPlayerEntity(Vector3f position, Vector3f scale) {
		RenderableEntity e = new RenderableEntity(models.get("player"), position, new Vector3f(), scale);
		e.addComponent(new PlayerPhysicsComponent());
		e.addComponent(new CameraComponentFollowEntity());
		return e;
	}

	public static Entity getCamera(Vector3f position) {
		Entity e = new Entity(position, new Vector3f());
		e.addComponent(new CameraComponentFlyoverObserver(1, 0.1f));
		return e;
	}

	public static RenderableEntity getRandomEntity(Vector3f position) {
		int randHashKeyPos = random.nextInt(models.keySet().size());
		String modelname = new ArrayList<String>(models.keySet()).get(randHashKeyPos);
		return getEntityByName(position, new Vector3f(1, 1, 1), modelname);
	}

	public static void cleanup() {
		models.values().forEach(m -> m.cleanup());
		standardShader.forceDelete();
	}
}
