package com.unnamedgame.components;

import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.util.math.*;
import com.unnamedgame.main.*;

// TODO: refactor
public class SpotLightFollowComponent implements RenderableEntityComponent {

	@Override
	public void init(RenderableEntity<?> entity) {

	}

	@Override
	public void update(RenderableEntity<?> entity) {
		UnnamedGame.CAMERA.update(entity);
		Vector3f lookat = MathUtils.createLookatVector(UnnamedGame.CAMERA.getRotation());
		UnnamedGame.SPOTLIGHT.position.x = entity.position.x + lookat.x * 2f;
		UnnamedGame.SPOTLIGHT.position.y = entity.position.y + 10f;
		UnnamedGame.SPOTLIGHT.position.z = entity.position.z + lookat.z * 2f;

		UnnamedGame.SPOTLIGHT.direction = lookat;
	}

	@Override
	public void cleanup() {

	}

}
