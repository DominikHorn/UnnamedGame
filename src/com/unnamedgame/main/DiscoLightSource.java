package com.unnamedgame.main;

import java.util.*;

import com.openglengine.util.*;
import com.openglengine.util.math.*;

public class DiscoLightSource extends MovingLightSource {
	private Random random = new Random();
	private int tickCount = 0;

	public DiscoLightSource(Vector3f position, Vector3f color, float brightness) {
		super(position, color, brightness);
	}

	@Override
	protected void update() {
		// this.tickCount++;
		// if (this.tickCount > 20) {
		// switch (this.random.nextInt(3)) {
		// case 0:
		// this.color.x = random.nextFloat();
		// break;
		// case 1:
		// this.color.y = random.nextFloat();
		// break;
		// case 2:
		// this.color.z = random.nextFloat();
		// break;
		// default:
		// break;
		// }
		// tickCount = 0;
		//
		// System.out.println(this.color);
		// }
	}

}
