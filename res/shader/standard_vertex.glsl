#version 400 core

in vec3 position;
in vec3 normal;
in vec2 textureCoords;

out vec3 surfaceNormal;
out vec3 toLightVectors[4];
out vec3 toCameraVector;
out vec3 toSpotLightVector;
out vec2 pass_textureCoords;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPositions[4];
uniform vec3 spotLightPosition;
uniform float useFakeLighting;


// Texture stuff
uniform vec2 texOffset;
uniform int texAtlasRowCount;

// Fog stuff
uniform float density = 0.005;
uniform float gradient = 1.5;

void main(void) {
	// Calculate glPosition
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;

	// Calculate texture coordinates
	pass_textureCoords = textureCoords / texAtlasRowCount + texOffset;
	
	// recalculate normal if fake lighting is enables
	vec3 actualNormal = normal;
	if (useFakeLighting > 0.5) {
		actualNormal = vec3(0.0,1.0,0.0);
	}
	
	// Calculate vectors	
	surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
	for (int i = 0; i < 4; i++) {
		toLightVectors[i] = lightPositions[i] - worldPosition.xyz;
	}
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
	
	// Compute fog variables per vertex
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);

	// Compute spotlight
	toSpotLightVector = spotLightPosition - worldPosition.xyz;
}