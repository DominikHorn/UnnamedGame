package com.unnamedgame.main;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entities.*;
import com.openglengine.eventsystem.defaultevents.*;
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
public class UnnamedGame extends Basic3DGame {
	@Override
	protected void loop() {
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

		ModelLoader loader = new ModelLoader();
		StaticShader shader = new StaticShader();
		Camera camera = new Camera();

		Texture texture = null;
		try {
			texture = Engine.TEXTURE_MANAGER.loadTexture("res/tex/panda.png");
		} catch (IOException e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not load panda texture!");
		}
		TexturedModel model = new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices, shader), texture);
		// RawModel model = loader.loadToVAO(vertices, indices, shader);
		VisibleEntity entity = new VisibleEntity(model, new Vector3f(0, 0, -1), 0, 0, 0, 1);

		// Propper view loop stuff
		double secsPerUpdate = 1.0 / 60.0;
		long previous = System.nanoTime();
		double steps = 0.0;

		while (!Engine.GLFW_MANAGER.getWindowShouldClose()) {
			/* update */

			long now = System.nanoTime();
			long elapsed = now - previous;
			previous = now;
			steps += elapsed / (Math.pow(10, 9));

			while (steps >= secsPerUpdate) {
				Engine.EVENT_MANAGER.dispatch(new UpdateEvent());
				steps -= secsPerUpdate;
			}

			/* render */
			glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			entity.render();

			Engine.GLFW_MANAGER.swapBuffers();
		}

		// TODO: tmp
		shader.cleanup();
		loader.cleanup();
	}

	public static void main(String argv[]) {
		UnnamedGame game = new UnnamedGame();
		Engine.cleanup();
	}
}
