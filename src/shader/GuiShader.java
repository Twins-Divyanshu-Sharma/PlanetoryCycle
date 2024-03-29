package shader;

import static org.lwjgl.opengl.GL20.glUniform1i;

import utility.Reader;

public class GuiShader extends ShaderProgram {
  
	private static String vs = "gui.vs";
	private static String fs = "gui.fs";
	
	public GuiShader(Reader reader) throws Exception{
		super();
   	 createVertexShader(reader.getCodeString(vs));
   	 createFragmentShader(reader.getCodeString(fs));
   	 linkProgram();
	}
    
	public void initialize() throws Exception{
 	    createUniform("transformationMatrix");
 		createUniform("sampler");
 		createUniform("color");
 		createUniform("hasTexture");
	}
	
    public void setUniform(String uniformName, boolean val){
    	int set = val ? 1 : 0;
    	glUniform1i(uniforms.get(uniformName),set);
    }
}
