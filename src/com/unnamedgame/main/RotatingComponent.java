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

	@Override
	public void update(Entity entity) {
		entity.rotY += 0.4f;
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
