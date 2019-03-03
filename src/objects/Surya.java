package objects;

import org.joml.Vector3f;

public class Surya extends GameObject{
	private float gravity = -0.000002f;
	private PointLight pointLight;
	
	public Surya(Mesh mesh, Vector3f position){
		super(mesh);
		this.setPosition(position.x, position.y, position.z);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        float lightIntensity = 4.0f;
        pointLight = new PointLight(lightColour, this.getPosition(), lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(1.0f, 0.0f, 0.2f);
        pointLight.setAttenuation(att);
	}
	
	public Surya(Mesh mesh, Vector3f position, Vector3f lightColor, float lightIntensity){
		super(mesh);
		this.setPosition(position.x, position.y, position.z);
		pointLight = new PointLight(lightColor, this.getPosition(), lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(1.0f, 0.0f, 0.2f);
        pointLight.setAttenuation(att);
	}
	
	public void update(){
		
	}
	
	
	public void setGravity(float gravity){
		this.gravity = gravity;
	}
	
	public float getGravity(){
		return gravity;
	}
	
	public PointLight getLight(){
		return this.pointLight;
	}
}
