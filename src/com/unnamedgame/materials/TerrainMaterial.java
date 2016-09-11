package com.unnamedgame.materials;

import com.openglengine.renderer.material.*;
import com.unnamedgame.shaders.*;

public class TerrainMaterial extends Material<TerrainShader> {
	@Override
	public void initRendercode(TerrainShader shader) {
	}

	@Override
	public void deinitRendercode() {
		// Do nothing for now
	}
}
