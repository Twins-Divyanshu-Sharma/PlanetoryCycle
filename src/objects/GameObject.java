package objects;

import org.joml.Vector3f;

public class GameObject {
    private Vector3f position, rotation, scale;
    private Mesh mesh;

    
    public GameObject(Mesh mesh){
    	this.mesh = mesh;
    	position = new Vector3f(0,0,0);
    	rotation = new Vector3f(0,0,0);
    	scale = new Vector3f(1,1,1);
    }
    
    public void input(){
    	
    }
    public void update(){
    	
    }
    public void render(double dt){
    	mesh.render(dt);
    }
    
    public void cleanUp(){
    	mesh.cleanUp();
    }
    
    
    
    
    
    
    //////////////////////////////////GETTERS AND SETTERS///////////////////////////////
    public Vector3f getPosition(){
    	return position;
    }
    public void setPosition(float x, float y, float z){
    	this.position.x = x;
    	this.position.y = y;
    	this.position.z = z;
    }
    
    public Vector3f getRotation(){
        return rotation;
    }
    public void setRotation(float x, float y, float z){
    	this.rotation.x = x;
    	this.rotation.y = y;
    	this.rotation.z = z;
    }
    
    public Vector3f getScale(){
    	return scale;
    }
    public void setScale(float x, float y, float z){
    	this.scale.x = x;
    	this.scale.y = y;
    	this.scale.z = z;
    }
    public void setScale(float val){
    	this.scale.x = val;
    	this.scale.y = val;
    	this.scale.z = val;
    }
    
   public  float getSize(){
	 return 0;
   }
    
    public Mesh getMesh(){
    	return mesh;
    }
}
