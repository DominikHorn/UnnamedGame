package com.unnamedgame.main;

import java.util.*;

import com.openglengine.util.*;
import com.openglengine.util.math.*;

public class DiscoLightSource extends MovingLightSource {
	private Random random = new Random();
	private int tickCount = 0;

	public DiscoLightSource(Vector3f position, Vector3f color, float brightness, int tickCountStart) {
		super(position, color, brightness);

		this.tickCount = tickCountStart;
	}

	@Override
	protected void update() {
		if (this.tickCount++ > 60) {
			this.color.x = random.nextFloat();
			this.color.y = random.nextFloat();
			this.color.z = random.nextFloat();

			this.tickCount = 0;
		}
	}

}
