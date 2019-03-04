package objects;

import java.util.ArrayList;

import org.joml.Vector3f;

import base.Game;

public class Ulkapind extends GameObject{
	
	private Vector3f tangent;
	private Vector3f focus1;
	
	private Vector3f centre;
	
	float radius;
	
	private float tiltAngle;
	
	private float dt = 0.0001f;
	private float t;
	private float w;
	
	private float shift;
	
	private float a = 5f, b = 5f;
	
	private Path path;
	
	public Ulkapind(Mesh mesh, float size, Vector3f focus, int distance, float tiltAngle, float w){
		super(mesh);
		distance = distance/2;
		renderMe = true;
		this.setDead(false);
		this.tiltAngle = (float)Math.toRadians(tiltAngle);
		this.w = w;
		this.setScale(size);
		radius = size;
		
		focus1 = focus;
		float cx = focus.x + (float)(distance*Math.cos(tiltAngle));
		float cz = focus.z + (float)(distance*Math.sin(tiltAngle));
		
		centre = new Vector3f(cx, focus.y, cz);
		
		b += distance;
		
		shift = distance;
	}
	
		
	public void update(ArrayList<GameObject> list){
		if(!this.isDead()){
			move(list);
		}else{
				animateDeath();
		}

		
	}
	
	
	public void move(ArrayList<GameObject> list){
		t += dt;		
		
		double x = ( b * Math.cos(w*t + dt) + shift ) * Math.cos(tiltAngle)   -   a * Math.sin(w*t + dt) * Math.sin(tiltAngle) ;
		double z = ( b * Math.cos(w*t + dt) + shift) * Math.sin(tiltAngle)  +  a * Math.sin(w*t + dt) * Math.cos(tiltAngle);
		
		this.setPosition((float)x, this.getPosition().y, (float)z);
		
		if(w*t > 2*Math.PI){
			 t = 0;
		 }
		
		theCollision(list);
	}
	
	
	public boolean theCollision(ArrayList<GameObject> list){
		
		for(int j =0; j<list.size(); j++){
			
			GameObject i = list.get(j);
			float r = i.getSize();

			
			//double dist = (i.getPosition().x - this.getPosition().x)*(i.getPosition().x - this.getPosition().x) + (i.getPosition().y - this.getPosition().y)*(i.getPosition().y - this.getPosition().y);
			double dist = i.getPosition().distance(this.getPosition());
			if(dist < r + radius){
				//System.out.println("collided with "+j);
				this.setDead(true);
				renderMe = false;
				i.setDead(true);
				return true;
			}
		}
		
		return false;
	}
	
	public float geta() {
		return a;
	}
	
	public float getb() {
		return b;
	}
	
	public float getAngle() {
		return tiltAngle;
	}
	
	public Vector3f getCentre() {
		return centre;
	}
	
	public float getShift() {
		return shift;
	}
	
	public void setPath(Path path){
		this.path = path;
	}
	
	public Path getPath(){
		return path;
	}
	
	public void animateDeath(){
		//this.setPosition(this.getPosition().x + 0.001f, this.getPosition().y+0.001f, this.getPosition().z+0.001f);
		this.setPosition(this.getPosition().x + this.tangent.x*0.001f, this.getPosition().y, this.getPosition().z+this.tangent.z*0.001f);
		if(this.getPosition().distance(focus1) > 50f){
			renderMe = false;
		}
	}
	public void setTangent(){
		//tangent = new Vector3f();
		t -= 2*dt;		
		tangent = new Vector3f();
		double x = ( b * Math.cos(w*t + dt) + shift ) * Math.cos(tiltAngle)   -   a * Math.sin(w*t + dt) * Math.sin(tiltAngle) ;
		double z = ( b * Math.cos(w*t + dt) + shift) * Math.sin(tiltAngle)  +  a * Math.sin(w*t + dt) * Math.cos(tiltAngle);
		Vector3f prev = new Vector3f((float)x,this.getPosition().y,(float)z);
		this.getPosition().sub(prev, tangent);
		tangent.normalize();
	}

}
