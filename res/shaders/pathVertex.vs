#version 330

in vec3 position;

out vec3 outWorldPosition;
out vec3 outColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 color;

void main()
{
   vec4 worldPos  = transformationMatrix * vec4(position,1.0);  
   vec4 cameraPos = viewMatrix * worldPos;
   vec4 screenPos = projectionMatrix * cameraPos;
   gl_Position = screenPos;
   
   outColor = color;
   outWorldPosition = worldPos.xyz;

}