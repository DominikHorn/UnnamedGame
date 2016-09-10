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

		standardShader = new DynamicShader(UnnamedGame.LIGHT_SOURCE, UnnamedGame.SKY_COLOR);
		standardShader.compileShaderFromFiles(UnnamedGame.SHADER_FOLDER + "standard_vertex.glsl",
				UnnamedGame.SHADER_FOLDER + "standard_fragment.glsl");

		ModelData stallModelData = Engine.getModelDataManager().loadModelData(UnnamedGame.MODEL_FOLDER + "stall.obj");
		SimpleTexturedModel stallModel = new SimpleTexturedModel(UnnamedGame.TEX_FOLDER + "stall.png", standardShader,
				new DynamicMaterial(0, 1, false, false), stallModelData.getVertices(),
				stallModelData.getTextureCoords(), stallModelData.getNormals(), stallModelData.getIndices());
		stallModelData.cleanup();

		ModelData dragonModelData = Engine.getModelDataManager().loadModelData(UnnamedGame.MODEL_FOLDER + "dragon.obj");
		SimpleTexturedModel dragonModel = new SimpleTexturedModel(UnnamedGame.TEX_FOLDER + "dragon.png", standardShader,
				new DynamicMaterial(0, 1, false, false), dragonModelData.getVertices(),
				dragonModelData.getTextureCoords(), dragonModelData.getNormals(), dragonModelData.getIndices());
		dragonModelData.cleanup();

		ModelData bunnyModelData = Engine.getModelDataManager().loadModelData(UnnamedGame.MODEL_FOLDER + "bunny.obj");
		SimpleTexturedModel bunnyModel = new SimpleTexturedModel(UnnamedGame.TEX_FOLDER + "bunny.png", standardShader,
				new DynamicMaterial(0, 1, false, false), bunnyModelData.getVertices(),
				bunnyModelData.getTextureCoords(), bunnyModelData.getNormals(), bunnyModelData.getIndices());
		bunnyModelData.cleanup();

		ModelData fernModelData = Engine.getModelDataManager().loadModelData(UnnamedGame.MODEL_FOLDER + "fern.obj");
		SimpleTexturedModel fernModel = new SimpleTexturedModel(UnnamedGame.TEX_FOLDER + "fern.png", standardShader,
				new DynamicMaterial(0, 1, true, false), fernModelData.getVertices(), fernModelData.getTextureCoords(),
				fernModelData.getNormals(), fernModelData.getIndices());
		fernModelData.cleanup();

		ModelData grassModelData = Engine.getModelDataManager().loadModelData(UnnamedGame.MODEL_FOLDER + "grass.obj");
		SimpleTexturedModel grassModel = new SimpleTexturedModel(UnnamedGame.TEX_FOLDER + "grass.png", standardShader,
				new DynamicMaterial(0, 1, true, true), grassModelData.getVertices(), grassModelData.getTextureCoords(),
				grassModelData.getNormals(), grassModelData.getIndices());
		grassModelData.cleanup();

		ModelData treeModelData = Engine.getModelDataManager().loadModelData(UnnamedGame.MODEL_FOLDER + "tree.obj");
		SimpleTexturedModel treeModel = new SimpleTexturedModel(UnnamedGame.TEX_FOLDER + "tree.png", standardShader,
				new DynamicMaterial(0, 1, false, false), treeModelData.getVertices(), treeModelData.getTextureCoords(),
				treeModelData.getNormals(), treeModelData.getIndices());
		treeModelData.cleanup();

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

		models.put("stall", stallModel);
		models.put("dragon", dragonModel);
		models.put("bunny", bunnyModel);
		models.put("fern", fernModel);
		models.put("tree", treeModel);
		models.put("lowPolyTree", lowPolyTreeModel);
		models.put("grass", grassModel);
		models.put("player", playerModel);
	}

	public static Entity getEntityByName(Vector3f position, Vector3f scale, String modelname) {
		Vector3f rotation = new Vector3f();
		rotation.x = 0;
		rotation.y = random.nextFloat() * 360 * ROTATION_FLOAT_SCALE;
		rotation.z = 0;
		Entity e = new Entity(position, rotation, scale);
		e.model = models.get(modelname);

		return e;
	}

	public static Entity getPlayerEntity(Vector3f position, Vector3f scale) {
		Entity e = new Entity(position, new Vector3f(), scale);
		e.model = models.get("player");
		e.addComponent(new PlayerPhysicsComponent());
		e.addComponent(new CameraComponentFollowEntity());
		return e;
	}

	public static Entity getCamera(Vector3f position) {
		Entity e = new Entity(position, new Vector3f(), new Vector3f());
		e.addComponent(new CameraComponentFlyoverObserver(1, 0.1f));
		return e;
	}

	public static Entity getRandomEntity(Vector3f position) {
		int randHashKeyPos = random.nextInt(models.keySet().size());
		String modelname = new ArrayList<String>(models.keySet()).get(randHashKeyPos);
		return getEntityByName(position, new Vector3f(1, 1, 1), modelname);
	}

	public static void cleanup() {
		models.values().forEach(m -> m.cleanup());
		standardShader.forceDelete();
	}
}
