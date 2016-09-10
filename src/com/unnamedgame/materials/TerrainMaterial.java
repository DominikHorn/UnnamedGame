package com.unnamedgame.materials;

import com.openglengine.renderer.material.*;

public class TerrainMaterial extends Material {
	public float shineDamper = 1;
	public float reflectivity = 0;

	public TerrainMaterial(float reflectivity, float shineDamper) {
		this.reflectivity = reflectivity;
		this.shineDamper = shineDamper;
	}

	@Override
	public void initRendercode() {
		// Do nothing for now
	}

	@Override
	public void deinitRendercode() {
		// Do nothing for now
	}
}
