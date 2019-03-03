package objects;

import org.joml.Vector3f;

public class Planet extends GameObject {

	private float rotationSpeed;
	private float revolution;
	private float distanceFromSun;
	private Vector3f center;
	
	private float theta;
	
	public Planet(Mesh mesh) {
		super(mesh);
	}
	
	public void setRotationSpeed(float rotaionSpeed){
		this.rotationSpeed = rotaionSpeed;
	}
	public void setRevolution(float revolution){
		this.revolution = revolution;
	}
	public void setDistanceFromSun(float d, Vector3f center){
		distanceFromSun = d;
		this.center = center;
	}
	
	public void update(){
		getRotation().y += rotationSpeed;
		if(getRotation().y > 360)
			setRotation(0,0,0);
		
		theta += revolution;
		if(theta > 360)
			theta = 0;
		
		setPosition((float)(distanceFromSun*Math.cos(theta))+ center.x , 0.0f ,(float)(distanceFromSun*Math.sin(theta)) + center.z );
	}
	
	
}
