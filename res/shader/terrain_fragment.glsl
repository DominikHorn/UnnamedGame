#version 400 core

in vec3 surfaceNormal;
in vec3 toLightVectors[4];
in vec3 toSpotLightVector;
in vec3 toCameraVector;
in vec2 pass_textureCoords;
in float visibility;

out vec4 out_color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 skyColor;
uniform vec3 lightColors[4];
uniform vec3 lightAttenuations[4];
uniform vec3 spotLightAttenuation;
uniform vec3 spotLightDirection;
uniform vec3 spotLightColor;
uniform float spotCosCutoff;
uniform float ambient = 0.05;

/**
 * Calculates color from the blendmap and corresponding textures
 */
vec4 calculateColorFromBlendmap() {
	vec4 blendMapColor = texture(blendMap, pass_textureCoords);

	float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec2 tiledCoords = pass_textureCoords * 40.0;
	vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoords) * backTextureAmount;
	vec4 rTextureColor = texture(rTexture, tiledCoords) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture, tiledCoords) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture, tiledCoords) * blendMapColor.b;
	return backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;
}

/**
 * Calculates diffuse light component for light source
 */
vec3 calculateDiffuse(vec3 unitSurfaceNormal, vec3 toLightVector, vec3 lightColor) {
	float nDotl = dot(unitSurfaceNormal, normalize(toLightVector));
	float angularBrightness = max(nDotl, 0.0);

	return angularBrightness * lightColor;
}

/**
 * Calculates light attenuation (decay of brightness over distance)
 */
float calculateLightAttenuation(vec3 attenuation, vec3 toLightVector) {
	float distance = length(toLightVector);
	return (attenuation.x) + (attenuation.y * distance) + (attenuation.z * distance * distance);
}

/**
 * Calculates spotlight values
 */
float calculateSpotlightEffect(vec3 unitSurfaceNormal) {
	float nDotl = dot(unitSurfaceNormal, normalize(toSpotLightVector));
	float angularBrightness = max(nDotl, 0.0);

	return angularBrightness * pow(dot(normalize(-toSpotLightVector), spotLightDirection), degrees(spotCosCutoff)) * 3;
}

/**
 * Calculates color, taking diffuse lighting into consideration
 */
vec4 calculateColor() {
	// Calculate color
	vec4 blendColor = calculateColorFromBlendmap();

	// normalize surface normal vector
	vec3 unitSurfaceNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);

	// Calculate specular and diffuse
	vec3 totalDiffuse = vec3(0.0);
	for (int i = 0; i < 4; i++) {
		float attFac = calculateLightAttenuation(lightAttenuations[i], toLightVectors[i]);
		totalDiffuse = totalDiffuse + (calculateDiffuse(unitSurfaceNormal, toLightVectors[i], lightColors[i]) / attFac);
	}
	totalDiffuse = max(totalDiffuse, ambient);

	// Handle spotlight
	float spoteffect = calculateSpotlightEffect(unitSurfaceNormal) / calculateLightAttenuation(spotLightAttenuation, toSpotLightVector);

	totalDiffuse = totalDiffuse + spoteffect * spotLightColor;
	//clamp(totalDiffuse, 0.0, 1.0);

	return vec4(totalDiffuse, 1.0) * blendColor;
}

void main(void) {
	// Fog computation with color from calculateColor()
	out_color = mix(vec4(skyColor, 1.0), calculateColor(), visibility);
}