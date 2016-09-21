#version 400 core

in vec3 position;
in vec3 normal;
in vec2 textureCoords;

out vec3 surfaceNormal;
out vec3 toLightVectors[4];
out vec3 toCameraVector;
out vec2 pass_textureCoords;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPositions[4];
uniform float useFakeLighting;

// Texture stuff
uniform vec2 texOffset;
uniform int texAtlasRowCount;

const float density = 0.005;
const float gradient = 1.5;

void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	vec4 positionRelativeToCam = viewMatrix * worldPosition;

	gl_Position = projectionMatrix * positionRelativeToCam;
	pass_textureCoords = textureCoords / texAtlasRowCount + texOffset;
	
	vec3 actualNormal = normal;
	if (useFakeLighting > 0.5) {
		actualNormal = vec3(0.0,1.0,0.0);
	}
		
	surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
	for (int i = 0; i < 4; i++) {
		toLightVectors[i] = lightPositions[i] - worldPosition.xyz;
	}
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
	
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}