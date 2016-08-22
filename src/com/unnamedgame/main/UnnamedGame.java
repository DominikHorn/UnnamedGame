package com.unnamedgame.main;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entities.*;
import com.openglengine.eventsystem.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.renderer.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.texture.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * Game entry point
 * 
 * @author Dominik
 *
 */
public class UnnamedGame {
	public static final String APP_VERSION = "0.0.1_a";
	public static final String DISPLAY_TITLE = "UnnamedGame " + APP_VERSION;
	public static final int DEFAULT_DISPLAY_WIDTH = 1920;
	public static final int DEFAULT_DISPLAY_HEIGHT = 1080;

	// TODO: tmp refactor
	private static final float FOV = 70;
	private static final float ASPECT = DEFAULT_DISPLAY_WIDTH / DEFAULT_DISPLAY_HEIGHT;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;

	private final GlfwManager glfwManager;

	private UnnamedGame() {
		this(DEFAULT_DISPLAY_WIDTH, DEFAULT_DISPLAY_HEIGHT, false);
	}

	private UnnamedGame(int screenWidth, int screenHeight, boolean fullscreen) {
		// Initialize glfw
		this.glfwManager = new GlfwManager(screenWidth, screenHeight, fullscreen, DISPLAY_TITLE);
	}

	/* TODO: refactor start() and terminate() */
	public void start() {
		this.initGL();
		this.loop();
	}

	public void cleanup() {
		this.glfwManager.cleanup();
	}

	private void initGL() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		OpenGLEngine.PROJECTION_MATRIX_STACK.setPerspectiveMatrix(FOV, ASPECT, NEAR_PLANE, FAR_PLANE);
	}

	private void loop() {
		// TODO: tmp
		ModelLoader loader = new ModelLoader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();

		Camera camera = new Camera();

		shader.startUsingShader();
		shader.loadProjectionMatrix(OpenGLEngine.PROJECTION_MATRIX_STACK.getCurrentMatrix());
		shader.stopUsingShader();

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

		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		Texture texture = null;
		try {
			texture = OpenGLEngine.TEXTURE_MANAGER.loadTexture("res/tex/panda.png");
		} catch (IOException e) {
			e.printStackTrace();
			OpenGLEngine.LOGGER.err("Could not load panda texture!");
		}
		TexturedModel texturedModel = new TexturedModel(model, texture);
		BaseEntity entity = new BaseEntity(texturedModel, new Vector3f(0, 0, -1), 0, 0, 0, 1);

		// TODO: Enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// Propper view loop stuff
		double secsPerUpdate = 1.0 / 60.0;
		long previous = System.nanoTime();
		double steps = 0.0;

		while (!this.glfwManager.getWindowShouldClose()) {
			long now = System.nanoTime();
			long elapsed = now - previous;
			previous = now;
			steps += elapsed / (Math.pow(10, 9));

			while (steps >= secsPerUpdate) {
				EventManager.dispatch(new UpdateEvent()); // TODO: delta time
				steps -= secsPerUpdate;
			}

			entity.increasePosition(0, 0, 0);
			entity.increaseRotation(0, 1, 0);

			renderer.prepare();
			renderer.render(entity, shader);

			this.glfwManager.swapBuffers();
			this.glfwManager.pollEvents();
		}

		// TODO: tmp
		shader.cleanup();
		loader.cleanup();
	}

	public static void main(String argv[]) {
		UnnamedGame game = new UnnamedGame();

		try {
			game.start();
		} finally {
			game.cleanup();
		}
	}
}
