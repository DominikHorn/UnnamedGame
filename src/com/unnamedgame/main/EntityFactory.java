package com.unnamedgame.main;

import java.util.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.defaultcomponents.*;
import com.openglengine.renderer.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.math.*;
import com.unnamedgame.components.*;
import com.unnamedgame.materials.*;
import com.unnamedgame.models.*;
import com.unnamedgame.shaders.*;

public class EntityFactory {
	public static String RES_FOLDER = "res/";
	public static String MODEL_FOLDER = RES_FOLDER + "model/";
	public static String TEX_FOLDER = RES_FOLDER + "tex/";
	public static String SHADER_FOLDER = RES_FOLDER + "shader/";

	private static Random random = new Random();
	private static float ROTATION_FLOAT_SCALE = (float) (180.0 / Math.PI);

	private static DynamicShader standardShader;
	private static TerrainShader terrainShader;
	private static LightSource lightSource = new LightSource(new Vector3f(0, 500f, 0), new Vector3f(1.0f, 1.0f, 1.0f),
			0f);
	// private static Vector3f skyColor = new Vector3f(0.478f, 1f, 0.952f);
	private static Vector3f skyColor = new Vector3f(1f, 1f, 1f);

	private static Map<String, SimpleTexturedModel> models = new HashMap<>();

	public static void load() {
		// TODO: refactor
		GL11.glClearColor(skyColor.x, skyColor.y, skyColor.z, 1.0f);

		terrainShader = new TerrainShader(lightSource, skyColor);
		standardShader = new DynamicShader(lightSource, skyColor);
		standardShader.compileShaderFromFiles(SHADER_FOLDER + "standard_vertex.glsl",
				SHADER_FOLDER + "standard_fragment.glsl");
		terrainShader.compileShaderFromFiles(SHADER_FOLDER + "terrain_vertex.glsl",
				SHADER_FOLDER + "terrain_fragment.glsl");

		ModelData stallModelData = Engine.getModelDataManager().loadModelData(MODEL_FOLDER + "stall.obj");
		SimpleTexturedModel stallModel = new SimpleTexturedModel(TEX_FOLDER + "stall.png", standardShader,
				new DynamicMaterial(0, 1, false, false), stallModelData.getVertices(),
				stallModelData.getTextureCoords(), stallModelData.getNormals(), stallModelData.getIndices());

		ModelData dragonModelData = Engine.getModelDataManager().loadModelData(MODEL_FOLDER + "dragon.obj");
		SimpleTexturedModel dragonModel = new SimpleTexturedModel(TEX_FOLDER + "dragon.png", standardShader,
				new DynamicMaterial(0, 1, false, false), dragonModelData.getVertices(),
				dragonModelData.getTextureCoords(), dragonModelData.getNormals(), dragonModelData.getIndices());

		ModelData bunnyModelData = Engine.getModelDataManager().loadModelData(MODEL_FOLDER + "bunny.obj");
		SimpleTexturedModel bunnyModel = new SimpleTexturedModel(TEX_FOLDER + "bunny.png", standardShader,
				new DynamicMaterial(0, 1, false, false), bunnyModelData.getVertices(),
				bunnyModelData.getTextureCoords(), bunnyModelData.getNormals(), bunnyModelData.getIndices());

		ModelData fernModelData = Engine.getModelDataManager().loadModelData(MODEL_FOLDER + "fern.obj");
		SimpleTexturedModel fernModel = new SimpleTexturedModel(TEX_FOLDER + "fern.png", standardShader,
				new DynamicMaterial(0, 1, true, false), fernModelData.getVertices(), fernModelData.getTextureCoords(),
				fernModelData.getNormals(), fernModelData.getIndices());

		ModelData grassModelData = Engine.getModelDataManager().loadModelData(MODEL_FOLDER + "grassModel.obj");
		SimpleTexturedModel grassModel = new SimpleTexturedModel(TEX_FOLDER + "grassTexture.png", standardShader,
				new DynamicMaterial(0, 1, true, true), grassModelData.getVertices(), grassModelData.getTextureCoords(),
				grassModelData.getNormals(), grassModelData.getIndices());

		ModelData treeModelData = Engine.getModelDataManager().loadModelData(MODEL_FOLDER + "tree.obj");
		SimpleTexturedModel treeModel = new SimpleTexturedModel(TEX_FOLDER + "tree.png", standardShader,
				new DynamicMaterial(0, 1, false, false), treeModelData.getVertices(), treeModelData.getTextureCoords(),
				treeModelData.getNormals(), treeModelData.getIndices());

		models.put("stall", stallModel);
		models.put("dragon", dragonModel);
		models.put("bunny", bunnyModel);
		models.put("fern", fernModel);
		models.put("tree", treeModel);
		models.put("grass", grassModel);
	}

	public static Entity getEntityByName(Vector3f position, Vector3f scale, String modelname) {
		Vector3f rotation = new Vector3f();
		rotation.x = 0;
		rotation.y = random.nextFloat() * 360 * ROTATION_FLOAT_SCALE;
		rotation.z = 0;
		Entity e = new Entity(position, rotation, scale);
		e.putProperty(RenderableEntityProperties.PROPERTY_MODEL, models.get(modelname));

		return e;
	}

	public static Entity getRandomEntity(Vector3f position) {
		int randHashKeyPos = random.nextInt(models.keySet().size());
		String modelname = new ArrayList<String>(models.keySet()).get(randHashKeyPos);
		return getEntityByName(position, new Vector3f(1, 1, 1), modelname);
	}

	public static Entity getCamera(Vector3f position) {
		return new Entity(position).addComponent(new CameraInputComponent(1)).addComponent(new CameraComponent());
	}

	// TODO: refactor terrain code
	public static Entity getTerrainChunk(int gridX, int gridZ) {
		gridX = (int) (gridX * Terrain.CHUNK_SIZE);
		gridZ = (int) (gridZ * Terrain.CHUNK_SIZE);

		Entity e = new Entity(new Vector3f(gridX, 0, gridZ));
		e.addComponent(new TerrainChunkComponent(terrainShader));
		return e;
	}

	public static void cleanup() {
		models.values().forEach(m -> m.cleanup());
		terrainShader.forceDelete();
		standardShader.forceDelete();
	}
}
