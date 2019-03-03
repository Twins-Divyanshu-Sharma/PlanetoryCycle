package utility;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import objects.Camera;
import objects.GameObject;

public class MatrixCalculator {
   private Matrix4f projectionMatrix = new Matrix4f();
   private Matrix4f transformationMatrix = new Matrix4f();
   private Matrix4f modelMatrix = new Matrix4f();
   private Matrix4f viewMatrix = new Matrix4f();
   
   public Matrix4f getProjectionMatrix(float fovy, float aspect, float zNear, float zFar){
	   projectionMatrix.identity();
	   projectionMatrix.perspective(fovy, aspect, zNear, zFar);
	   return projectionMatrix;
   }
   
   public Matrix4f getViewMatrix(Camera camera){
	   viewMatrix.identity().rotate((float)Math.toRadians(camera.getRotation().x),new Vector3f(1,0,0))
	                        .rotate((float)Math.toRadians(camera.getRotation().y),new Vector3f(0,1,0)) 
	                        .rotate((float)Math.toRadians(camera.getRotation().z),new Vector3f(0,0,1))
	                        .translate(-1*camera.getPosition().x,-1*camera.getPosition().y,-1*camera.getPosition().z);
	   return viewMatrix;
   }
   
   public Matrix4f getTransformationMatrix(GameObject go){
	   Vector3f rotation = go.getRotation();
	   transformationMatrix.identity();
	   transformationMatrix.translate(go.getPosition()).rotateX((float)Math.toRadians(-rotation.x))
	                                  .rotateY((float)Math.toRadians(-rotation.y))
	                                  .rotateZ((float)Math.toRadians(-rotation.z))
	                                  .scale(go.getScale());
	   return transformationMatrix;
   }
   
   
   public Matrix4f getModelMatrix(GameObject go, Matrix4f viewMatrix){
	   Vector3f rotation = go.getRotation();
	   modelMatrix.identity();
	   modelMatrix.translate(go.getPosition()).rotateX((float)Math.toRadians(-rotation.x))
	                                  .rotateY((float)Math.toRadians(-rotation.y))
	                                  .rotateZ((float)Math.toRadians(-rotation.z))
	                                  .scale(go.getScale());
	   Matrix4f viewCurr = new Matrix4f(viewMatrix);
	   return viewCurr.mul(modelMatrix);
   }
   
   
}
