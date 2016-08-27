package com.unnamedgame.main;

import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.math.*;

/**
 * Temporary component used for testing stuff
 * 
 * @author Dominik
 *
 */
public class RotatingComponent extends Component {
	private Vector3f deltaRotation;

	public RotatingComponent(Vector3f deltaRotation) {
		super();
		this.deltaRotation = deltaRotation;
	}

	@Override
	public void update(Entity entity) {
		Vector3f entRot = (Vector3f) entity.getProperty(DefaultEntityProperties.PROPERTY_ROTATION).getValue();
		entRot.addVector(this.deltaRotation);
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
