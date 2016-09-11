package com.unnamedgame.main;

import org.lwjgl.opengl.*;

import com.openglengine.util.*;
import com.openglengine.util.math.*;

public class Sun extends MovingLightSource {
	// TODO: properly implement (should be centered around player
	public Sun(double daytimeTickRate) {
		super((source, tickCount) -> {
			// Shift tick so that we start within the day TODO: tmp
			tickCount += 2 / daytimeTickRate;

			// Rate at which the day flows (the smaller, the longer the day)
			double timeOfDay = ((double) daytimeTickRate * tickCount / (2 * Math.PI)) % (2 * Math.PI);
			float timeSin = (float) Math.sin(timeOfDay);

			// Daytime
			if (timeSin > 0.2) {
				// Change position only during the day
				source.position.y = timeSin * 500f;
				source.position.x = (float) Math.cos(timeOfDay) * 500f;

				// Change light source.brightness and depending on time of day
				source.brightness = timeSin;

				// Change color depending on time of day
				source.color.x = MathUtils.clamp(source.brightness * 1.30f, 0f, 1f);
				source.color.y = MathUtils.clamp(source.brightness * 1.10f, 0f, 1f);
				source.color.z = MathUtils.clamp(source.brightness * 1.00f, 0f, 1f);
			} else {
				// Nightime
				// Change light brightness and depending on time of days
				source.brightness = 0.2f;

				// Change color depending on time of day
				source.color.x = source.brightness;
				source.color.y = source.brightness;
				source.color.z = 0.25f;
			}

			GL11.glClearColor(source.color.x, source.color.y, source.color.z, 1.0f);
		}, new Vector3f(), new Vector3f(), 1f);
	}

}
