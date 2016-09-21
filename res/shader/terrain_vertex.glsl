#version 400 core

in vec3 position;
in vec3 normal;
in vec2 textureCoords;

out vec3 surfaceNormal;
out vec3 toLightVectors[4];
out vec3 toSpotLightVector;
out vec3 toCameraVector;
out vec2 pass_textureCoords;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPositions[4];
uniform vec3 spotLightPosition;

const float density = 0.005;
const float gradient = 1.5;

void main(void) {
	// Compute position
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	
	// Compute texture coords
	pass_textureCoords = textureCoords;
	
	// Compute surface normal
	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	for (int i = 0; i < 4; i++) {
		// Compute toLightVectors
		toLightVectors[i] = lightPositions[i] - worldPosition.xyz;
	}

	// Compute to camera vector
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;

	// Compute Fog variables
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);

	// Compute spotlight
	toSpotLightVector = spotLightPosition - worldPosition.xyz;
}