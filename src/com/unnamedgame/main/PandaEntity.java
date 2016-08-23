package com.unnamedgame.main;

import com.openglengine.entities.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.math.*;

public class PandaEntity extends VisibleEntity {

	public PandaEntity(RawModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	@Override
	protected void update(UpdateEvent e) {
		this.rotY += 1.0f;
	}

}
