#version 400 core

in vec3 surfaceNormal;
in vec3 toLightVectors[4];
in vec3 toCameraVector;
in vec2 pass_textureCoords;
in float visibility;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 skyColor;
uniform vec3 lightColors[4];
uniform vec3 lightAttenuations[4];
uniform float shineDamper;
uniform float reflectivity;
uniform float transparent;

/**
 * Calculates diffuse light component for light source
 */
vec3 calculateDiffuse(vec3 unitSurfaceNormal, vec3 toLightVector, vec3 lightColor) {
	float nDotl = dot(unitSurfaceNormal, normalize(toLightVector));
	float angularBrightness = max(nDotl, 0.0);

	return angularBrightness * lightColor;
}

/**
 * Calculates specular light component for light source
 */
vec3 calculateSpecular(vec3 unitSurfaceNormal, vec3 unitVectorToCamera, vec3 toLightVector, vec3 lightColor) {
	vec3 lightDirection = -normalize(toLightVector);
	vec3 reflectedLightDirection = reflect(lightDirection, unitSurfaceNormal);
	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);

	return dampedFactor * reflectivity *  lightColor;
}

/**
 * Calculates light attenuation (decay of brightness over distance)
 */
float calculateLightAttenuation(vec3 attenuation, vec3 toLightVector) {
	float distance = length(toLightVector);
	return attenuation.x + (attenuation.y * distance) + (attenuation.z * distance * distance);
}

/**
 * Calculates color, taking both diffuse and specular lighting into consideration
 */
vec4 calculateColor() {
	// Texture color calculation
	vec4 blendColor = texture(textureSampler, pass_textureCoords);
	if (transparent == 1 && blendColor.a < 0.5) {
		discard;
	}

	// normalize surface normal vector
	vec3 unitSurfaceNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);

	// Calculate specular and diffuse
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	for (int i = 0; i < 4; i++) {
		float attFac = calculateLightAttenuation(lightAttenuations[i], toLightVectors[i]);
		totalDiffuse = totalDiffuse + (calculateDiffuse(unitSurfaceNormal, toLightVectors[i], lightColors[i]) / attFac);
		totalSpecular = totalSpecular + (calculateSpecular(unitSurfaceNormal, unitVectorToCamera, toLightVectors[i], lightColors[i]) / attFac);
	}
	totalDiffuse = max(totalDiffuse, 0.05);

	return vec4(totalDiffuse, 1.0) * blendColor + vec4(totalSpecular, 1.0);
}

void main(void) {
	// Fog computation with color from calculateColor()
	out_color = mix(vec4(skyColor, 1.0), calculateColor(), visibility);
}