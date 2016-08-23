package com.unnamedgame.main;

import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;

/**
 * Temporary component used for testing stuff
 * 
 * @author Dominik
 *
 */
public class RotatingComponent extends Component {
	private float rotXDelta, rotYDelta, rotZDelta;

	public RotatingComponent(float rotXDelta, float rotYDelta, float rotZDelta) {
		super();
		this.rotXDelta = rotXDelta;
		this.rotYDelta = rotYDelta;
		this.rotZDelta = rotZDelta;
	}

	@Override
	public void update(Entity entity) {
		entity.rotX += rotXDelta;
		entity.rotY += rotYDelta;
		entity.rotZ += rotZDelta;
	}

	@Override
	public void receiveEvent(BaseEvent event) {
		// Do nothing
	}

	@Override
	public void cleanup() {
		// No resources -> no cleanup
	}

}
