package objects;

import java.util.ArrayList;

import org.joml.Vector3f;

import base.Game;

public class Ulkapind extends GameObject{
	
	private Vector3f focus1;
	private Vector3f focus2;
	
	private Vector3f centre;
	private Vector3f bigF;
	
	float radius;
	
	private float tiltAngle;
	
	private float dt = 0.0001f;
	private float t;
	private float w;
	
	private float a = 0.3f, b = 0.5f;
	
	public Ulkapind(Mesh mesh, float size, Vector3f focus, int distance, float tiltAngle, float w){
		super(mesh);
		
		this.tiltAngle = (float)Math.toRadians(tiltAngle);
		this.w = w;
		this.setScale(size);
		radius = size;
		
		focus1 = focus;
		System.out.println(focus1.x+" "+focus1.z);
		
		float fx = focus.x + (float)(distance*Math.cos(tiltAngle));
		float fz = focus.z + (float)(distance*Math.sin(tiltAngle));
		
		focus2 = new Vector3f(fx, focus.y, fz);
		System.out.println(focus2.x+" "+focus2.z);
	
		bigF = focus1.x > focus2.x ? focus1 : focus2;
		
		float cx = (focus1.x + focus2.x) / 2;
		float cz = (focus1.z + focus2.z) / 2;
		centre = new Vector3f(cx, focus.y, cz);
		
		float db = (bigF.x - centre.x)*(bigF.x - centre.x) + (bigF.z - centre.z)*(bigF.z - centre.z);
		db = (float)Math.sqrt(db);
		b += db;
	}
	
		
	public void update(){
		
		t += dt;
		
		double x = ( b*Math.cos(tiltAngle) - a*Math.sin(tiltAngle) ) * Math.cos(w*t + dt) + centre.x;
		double z = ( b* Math.sin(tiltAngle)+ a*Math.cos(tiltAngle)) * Math.sin(w*t + dt) + centre.y;
		
		this.setPosition((float)x, this.getPosition().y, (float)z);
		
		if(w*t > 2*Math.PI){
			 t = 0;
		 }
		
	}
	
	public boolean theCollision(ArrayList<GameObject> list){
		
		for(int j =0; j<list.size(); j++){
			
			GameObject i = list.get(j);
			float r = i.getSize();

			
			//double dist = (i.getPosition().x - this.getPosition().x)*(i.getPosition().x - this.getPosition().x) + (i.getPosition().y - this.getPosition().y)*(i.getPosition().y - this.getPosition().y);
			double dist = i.getPosition().distance(this.getPosition());
			if(dist < r + radius){
				//System.out.println("collided with "+j);
				//i.setDead(true);
				return true;
			}
		}
		
		return false;
	}
	
	
}
