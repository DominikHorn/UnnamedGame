package com.unnamedgame.main;

import java.io.*;

import com.openglengine.core.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.renderer.texture.*;
import com.openglengine.util.math.*;

/**
 * Game entry point
 * 
 * @author Dominik
 *
 */
public class UnnamedGame extends Basic3DGame {
	// TODO: eventually to be moved to a config file
	private static final int SCREEN_WIDTH = 1920;
	private static final int SCREEN_HEIGHT = 1080;
	private static final float FOV = 70f;
	private static final float ASPECT = (float) SCREEN_WIDTH / (float) SCREEN_HEIGHT;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	private static final boolean FULLSCREEN = true;
	private static final String WINDOW_TITLE = "Engine " + Engine.ENGINE_VERSION;

	public UnnamedGame(float fov, float aspect, float near_plane, float far_plane) {
		super(SCREEN_WIDTH, SCREEN_HEIGHT, FULLSCREEN, WINDOW_TITLE, fov, aspect, near_plane, far_plane);
	}


	// TODO tmp
	private ShaderProgram shader;
	private ModelLoader loader;
	private PandaEntity entity;

	@Override
	protected void setup() {
		// TODO: tmp
		//@formatter:off
		float[] vertices = {
			-0.5f, +0.5f, +0.0f,
			-0.5f, -0.5f, +0.0f,
			+0.5f, -0.5f, +0.0f,
			+0.5f, +0.5f, +0.0f
		};
		
		float[] textureCoords = {
			0,0,
			0,1,
			1,1,
			1,0
		};
		
		int [] indices = {
			0,1,3,
			3,1,2,
		};		
		//@formatter:on

		loader = new ModelLoader();
		shader = new StaticShader();

		Texture texture = null;
		try {
			texture = Engine.TEXTURE_MANAGER.loadTexture("res/tex/panda.png");
		} catch (IOException e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not load panda texture!");
		}
		TexturedModel model = new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices, shader), texture);
		// RawModel model = loader.loadToVAO(vertices, indices, shader);
		entity = new PandaEntity(model, new Vector3f(0, 0, -1), 0, 0, 0, 1);
	}

	@Override
	protected void update(UpdateEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void render(RenderEvent e) {
		entity.render();
	}

	@Override
	protected void cleanup() {
		loader.cleanup();
		shader.cleanup();
	}

	public static void main(String argv[]) {
		UnnamedGame game = new UnnamedGame(FOV, ASPECT, NEAR_PLANE, FAR_PLANE);
		game.cleanup();
		Engine.cleanup();
	}
}
