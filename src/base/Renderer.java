package base;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glAlphaFunc;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import objects.Camera;
import objects.GameObject;
import objects.GuiObject;
import objects.Path;
import objects.PointLight;
import shader.GuiShader;
import shader.ShaderProgram;
import utility.MatrixCalculator;
import utility.Reader;

public class Renderer {
     private static final float fov = (float)Math.toRadians(60.0);
     private static final float zNear = 0.01f;
     private static final float zFar = 1000f;
     private static float aspect;
     private MatrixCalculator matrixCal = new MatrixCalculator();
     
     private ShaderProgram shaderProgram;
     private GuiShader guiShader;
     private ShaderProgram pathShader;
     
     private float specularPower = 10f;

    
     public Renderer(float aspect) throws Exception{
    	 this.aspect = aspect;
    	 shaderProgram = new ShaderProgram();
    	 shaderProgram.createVertexShader(Reader.getCode("vertex.vs"));
    	 shaderProgram.createFragmentShader(Reader.getCode("fragment.fs"));
    	 shaderProgram.linkProgram();
    	 shaderProgram.createUniform("projectionMatrix");
    	 shaderProgram.createUniform("viewMatrix");
    	 shaderProgram.createUniform("transformationMatrix");
    	 shaderProgram.createUniform("sampler");
    	 shaderProgram.createMaterialUniform("material");
         // Create lighting related uniforms
         shaderProgram.createUniform("specularPower");
         shaderProgram.createUniform("ambientLight");
         shaderProgram.createPointLightUniform("pointLight");    
         
         guiShader = new GuiShader();
         guiShader.initialize();
         
         initPathShader();
     }
     
     
     private void initPathShader() throws Exception {
    	 pathShader = new ShaderProgram();
    	 pathShader.createVertexShader(Reader.getCode("pathVertex.vs"));
    	 pathShader.createFragmentShader(Reader.getCode("pathFragment.fs"));
    	 pathShader.linkProgram();
    	 pathShader.createUniform("projectionMatrix");
    	 pathShader.createUniform("viewMatrix");
    	 pathShader.createUniform("transformationMatrix");
    	 pathShader.createUniform("color");
     }
     
     
     
     public void render(double dt, ArrayList<GameObject> objList, Camera camera,  Vector3f ambientLight, PointLight pointLight){
    	shaderProgram.bind();
    	 Matrix4f projectionMatrix = matrixCal.getProjectionMatrix(fov, aspect, zNear, zFar);
    	 Matrix4f viewMatrix = matrixCal.getViewMatrix(camera);
		 shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		 shaderProgram.setUniform("viewMatrix", viewMatrix);
    	 shaderProgram.setUniform("sampler", 0);
    	 shaderProgram.setUniform("ambientLight", ambientLight);
         shaderProgram.setUniform("specularPower", specularPower);
         shaderProgram.setUniform("pointLight", pointLight);
    	 for(GameObject go : objList){
    		 if(go.willRender()){
    		  Matrix4f transformationMatrix = matrixCal.getTransformationMatrix(go);
    		  shaderProgram.setUniform("transformationMatrix",transformationMatrix);
    		  shaderProgram.setUniform("material", go.getMesh().getMaterial());
    	      go.render(dt);
    		 }
    	 }

    	 
    	 shaderProgram.unbind();
     }
     
     
     public void render(double dt, ArrayList<Path> pathList, Camera camera) {
    	 this.wireframe(true);
    	 pathShader.bind();
    	 Matrix4f projectionMatrix = matrixCal.getProjectionMatrix(fov, aspect, zNear, zFar);
    	 Matrix4f viewMatrix = matrixCal.getViewMatrix(camera);
		pathShader.setUniform("projectionMatrix", projectionMatrix);
		pathShader.setUniform("viewMatrix", viewMatrix);
		for(Path p : pathList) {
			Matrix4f transformation = matrixCal.getTransformationMatrix(p);
			pathShader.setUniform("transformationMatrix", transformation);
			pathShader.setUniform("color", p.getColor());
			p.render(dt);	
		}
		pathShader.unbind();
		this.wireframe(false);
		
     }
     
     
     public void renderGui(double dt, ArrayList<GuiObject> guiList){
   	    glEnable(GL_BLEND);
   	    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    	 
    	guiShader.bind();
    	 guiShader.setUniform("sampler", 0);
    	 for(GuiObject gui : guiList){
    		  Matrix4f transformationMatrix = matrixCal.getTransformationMatrix(gui);
    		  guiShader.setUniform("transformationMatrix",transformationMatrix);
    		  guiShader.setUniform("color", gui.getColor());
    		  guiShader.setUniform("hasTexture",gui.isTextured());
    		  gui.getMesh().render(dt, gui.isTextured(), gui.getTexture());
    	 }

    	 
    	 guiShader.unbind();
          
    	 glDisable(GL_BLEND);
         glEnable(GL_ALPHA_TEST);
         glAlphaFunc(GL_GREATER,0.1f);
    
    }
     // single gui
     public void renderSingleGui(double dt, GuiObject gui){
    	    glEnable(GL_BLEND);
    	    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
     	 
     	guiShader.bind();
     	 guiShader.setUniform("sampler", 0);
     		  Matrix4f transformationMatrix = matrixCal.getTransformationMatrix(gui);
     		  guiShader.setUniform("transformationMatrix",transformationMatrix);
     		  guiShader.setUniform("color", gui.getColor());
     		  guiShader.setUniform("hasTexture",gui.isTextured());
     		  gui.getMesh().render(dt, gui.isTextured(), gui.getTexture());
	 
     	 guiShader.unbind();
           
     	 glDisable(GL_BLEND);
          glEnable(GL_ALPHA_TEST);
          glAlphaFunc(GL_GREATER,0.1f);
     
     }
     
     
     public void wireframe(boolean wireFrame){
    	 if(wireFrame){
    		 glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
    	 }else
    		 glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
    	 
     }

     
     
     public void cleanUp(){
    	 shaderProgram.cleanUp();
     }
}
