package com.unnamedgame.materials;

import com.openglengine.renderer.material.*;
import com.unnamedgame.shaders.*;

public class TerrainMaterial extends Material<TerrainShader> {
	private float shineDamper = 1;
	private float reflectivity = 0;

	public TerrainMaterial(float reflectivity, float shineDamper) {
		this.reflectivity = reflectivity;
		this.shineDamper = shineDamper;
	}

	@Override
	public void initRendercode(TerrainShader shader) {
		shader.loadFloat(shader.location_shineDamper, this.shineDamper);
		shader.loadFloat(shader.location_reflectivity, this.reflectivity);
	}

	@Override
	public void deinitRendercode() {
		// Do nothing for now
	}
}
