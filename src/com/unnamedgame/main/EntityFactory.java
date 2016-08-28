package com.unnamedgame.main;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.defaultcomponents.*;
import com.openglengine.renderer.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.math.*;
import com.unnamedgame.components.*;
import com.unnamedgame.materials.*;
import com.unnamedgame.shaders.*;

public class EntityFactory {
	public static String RES_FOLDER = "res/";
	public static String MODEL_FOLDER = RES_FOLDER + "model/";
	public static String TEX_FOLDER = RES_FOLDER + "tex/";
	public static String SHADER_FOLDER = RES_FOLDER + "shader/";

	private static Random random = new Random();
	private static float ROTATION_FLOAT_SCALE = (float) (180.0 / Math.PI);

	private static StandardShader standardShader;
	private static TerrainShader terrainShader;
	private static LightSource lightSource;

	private static Map<String, TexturedModel> models = new HashMap<>();

	public static void load() {
		lightSource = new LightSource(new Vector3f(0, 500f, 0), new Vector3f(1.0f, 1.0f, 1.0f), 2000f);
		terrainShader = new TerrainShader(lightSource);
		standardShader = new StandardShader(lightSource);
		standardShader.compileShaderFromFiles(SHADER_FOLDER + "standard_vertex.glsl",
				SHADER_FOLDER + "standard_fragment.glsl");
		terrainShader.compileShaderFromFiles(SHADER_FOLDER + "terrain_vertex.glsl",
				SHADER_FOLDER + "terrain_fragment.glsl");

		TexturedModel stallModel = Engine.getModelManager().loadTexturedModel(MODEL_FOLDER + "stall.obj");
		stallModel.setMaterial(new DynamicMaterial(0, 1, false));
		stallModel.setTexture(Engine.getTextureManager().loadTexture(TEX_FOLDER + "stall.png"));
		stallModel.setShader(standardShader);

		TexturedModel dragonModel = Engine.getModelManager().loadTexturedModel(MODEL_FOLDER + "dragon.obj");
		dragonModel.setMaterial(new DynamicMaterial(1, 50, false));
		dragonModel.setTexture(Engine.getTextureManager().loadTexture(TEX_FOLDER + "dragon.png"));
		dragonModel.setShader(standardShader);

		TexturedModel bunnyModel = Engine.getModelManager().loadTexturedModel(MODEL_FOLDER + "bunny.obj");
		bunnyModel.setMaterial(new DynamicMaterial(1, 3, false));
		bunnyModel.setTexture(Engine.getTextureManager().loadTexture(TEX_FOLDER + "dragon.png"));
		bunnyModel.setShader(standardShader);

		TexturedModel fernModel = Engine.getModelManager().loadTexturedModel(MODEL_FOLDER + "fern.obj");
		fernModel.setMaterial(new DynamicMaterial(0, 1, true));
		fernModel.setTexture(Engine.getTextureManager().loadTexture(TEX_FOLDER + "fern.png"));
		fernModel.setShader(standardShader);

		TexturedModel treeModel = Engine.getModelManager().loadTexturedModel(MODEL_FOLDER + "tree.obj");
		treeModel.setMaterial(new DynamicMaterial(0, 1, true));
		treeModel.setTexture(Engine.getTextureManager().loadTexture(TEX_FOLDER + "tree.png"));
		treeModel.setShader(standardShader);

		models.put("stall", stallModel);
		models.put("dragon", dragonModel);
		models.put("bunny", bunnyModel);
		models.put("fern", fernModel);
		models.put("tree", treeModel);
	}

	public static Entity getEntityByName(Vector3f position, Vector3f scale, String modelname) {
		Vector3f rotation = new Vector3f();
		rotation.x = 0;
		rotation.y = random.nextFloat() * 360 * ROTATION_FLOAT_SCALE;
		rotation.z = 0;
		Entity e = new Entity(position, rotation, scale);
		e.putProperty(DefaultEntityProperties.PROPERTY_MODEL, models.get(modelname));

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
