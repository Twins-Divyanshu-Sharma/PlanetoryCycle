#version 330

out vec4 fragColor;
in vec2 outTexCoord;


uniform vec4 color;
uniform int hasTexture;
uniform sampler2D sampler;

void main()
{
   vec4 baseColor;
   if( hasTexture == 1)
   {
      baseColor = texture(sampler, outTexCoord);
   }
   else
   {
      baseColor = color;
   }
   
   if(baseColor.a < 0.1f)
   		discard;
   
   fragColor = baseColor;
}