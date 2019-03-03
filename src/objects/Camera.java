package objects;

import org.joml.Vector3f;

public class Camera {
//	private Player player;
   private Vector3f position, rotation, scale;
   // rotatiion.x = pitch;   rotation.y = yaw ; rotation.z = roll;
   
   public Camera(/*Player player*/){
	   position = new Vector3f(0,0,0);
	   rotation = new Vector3f(0,0,0);
	//   this.player = player;
//	   scale = new Vector3f(1,1,1);
   }
   

public Vector3f getPosition() {
	return position;
}

public void setPosition(float x, float y, float z) {
	this.position.x = x;
	this.position.y = y;
	this.position.z = z;
}

public Vector3f getRotation() {
	return rotation;
}

public void setRotation(float x, float y, float z) {
	this.rotation.x = x;
	this.rotation.y = y;
	this.rotation.z = z;
}

public void movePosition(float offsetX, float offsetY, float offsetZ){
	if(offsetZ!=0){
		position.x += (float)Math.sin(Math.toRadians(rotation.y))*-1.0f*offsetZ;
		position.z += (float)Math.sin(Math.toRadians(rotation.y))*offsetZ;
	}
	if(offsetX!=0){
		position.x += (float)Math.sin(Math.toRadians(rotation.y-90))*-1.0f*offsetX;
		position.z += (float)Math.sin(Math.toRadians(rotation.y-90))*offsetX;
	}
	this.position.y += offsetY;
	
}

public void moveDistance(float dis){
	position.y += dis*(float)Math.sin(Math.toRadians(-rotation.x));   // dsin(pitch)
	position.z +=  -1* dis*(float)Math.cos(Math.toRadians(-rotation.x))*(float)Math.cos(Math.toRadians(rotation.y));  //dcos(pitch)cos(rotY)
	position.x += dis*(float)Math.cos(Math.toRadians(-rotation.x))*(float)Math.sin(Math.toRadians(rotation.y));  //dcos(pitch)sin(rotY)
	
}

public void strafeDistance(float dis){
	//position.y += dis*(float)Math.sin(Math.toRadians(rotation.x));   // dsin(pitch)
	position.z +=  dis*(float)Math.cos(Math.toRadians(rotation.x))*(float)Math.sin(Math.toRadians(rotation.y));  //dcos(pitch)cos(rotY)
	position.x += dis*(float)Math.cos(Math.toRadians(rotation.x))*(float)Math.cos(Math.toRadians(rotation.y));  //dcos(pitch)sin(rotY)
	
}

public void moveRotation(float offsetX, float offsetY, float offsetZ){
      rotation.x += offsetX;
      rotation.y += offsetY;
      rotation.z += offsetZ;
}
}