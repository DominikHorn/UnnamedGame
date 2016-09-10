package com.unnamedgame.components;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.util.math.*;
import com.unnamedgame.main.*;

public class PlayerPhysicsComponent implements RenderableEntityComponent {

	private static final float RUN_SPEED = 1f;
	private static final float TURN_SPEED = 0.05f;
	private static final float JUMP_VELOCITY = 1f;
	private static final float GRAVITY = 0.05f;
	private static final double ANGLE_OFFSET = 90.0 * Math.PI / 180.0;

	private Vector3f speed;
	private Vector3f rotationSpeed;

	private boolean canJump = false;

	@Override
	public void init(RenderableEntity entity) {
		this.speed = new Vector3f();
		this.rotationSpeed = new Vector3f();
	}

	@Override
	public void update(RenderableEntity entity) {
		// Fetch input
		this.fetchInput(entity);

		// Apply gravity
		this.speed.y -= GRAVITY;

		// Apply movement
		this.move(entity);
	}

	private void fetchInput(RenderableEntity entity) {
		InputManager input = Engine.getInputManager();

		// Reset old movement
		this.speed.z = 0;
		this.speed.x = 0;
		this.rotationSpeed.y = 0;

		// Handle turning
		if (input.isKeyDown(InputManager.KEY_Q)) {
			this.rotationSpeed.y = TURN_SPEED;
		}

		// Handle turning
		if (input.isKeyDown(InputManager.KEY_E)) {
			this.rotationSpeed.y = -TURN_SPEED;
		}
		this.rotate(entity);

		if (input.isKeyDown(InputManager.KEY_W)) {
			this.speed.z += (float) (RUN_SPEED * Math.cos(entity.rotation.y));
			this.speed.x += (float) (RUN_SPEED * Math.sin(entity.rotation.y));
		}

		if (input.isKeyDown(InputManager.KEY_S)) {
			this.speed.z -= (float) (RUN_SPEED * Math.cos(entity.rotation.y));
			this.speed.x -= (float) (RUN_SPEED * Math.sin(entity.rotation.y));
		}

		if (input.isKeyDown(InputManager.KEY_A)) {
			this.speed.z += (float) (RUN_SPEED * Math.cos(entity.rotation.y + ANGLE_OFFSET));
			this.speed.x += (float) (RUN_SPEED * Math.sin(entity.rotation.y + ANGLE_OFFSET));
		}

		if (input.isKeyDown(InputManager.KEY_D)) {
			this.speed.z += (float) (RUN_SPEED * Math.cos(entity.rotation.y + 3 * ANGLE_OFFSET));
			this.speed.x += (float) (RUN_SPEED * Math.sin(entity.rotation.y + 3 * ANGLE_OFFSET));
		}

		// Handle jumping
		if (this.canJump && input.isKeyDown(InputManager.KEY_SPACE)) {
			this.speed.y = JUMP_VELOCITY;
			this.canJump = false;
		}
	}

	private void rotate(RenderableEntity entity) {
		entity.rotation.addVector(this.rotationSpeed);
	}

	/**
	 * Moves entity and returns whether or not the entity has actually moved
	 * 
	 * @param entity
	 * @return
	 */
	private void move(RenderableEntity entity) {
		// Add speed vectors
		entity.position.addVector(this.speed);

		// Make sure player does not fall through ground
		float terrainHeight = UnnamedGame.TERRAIN.getHeightAt(entity.position.x, entity.position.z);
		if (entity.position.y < terrainHeight) {
			entity.position.y = terrainHeight;
			this.speed.y = 0;
			this.canJump = true;
		}
	}

	@Override
	public void cleanup() {
		// Do nothing for now
	}

}
