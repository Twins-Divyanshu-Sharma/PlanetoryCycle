package objects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;

import org.joml.Vector3f;

public class Path {

	private float[] vertices;
	private Vector3f position, color;
	private int vao;
	private int vbo;


	private double t , dt;
	private float r;
	
	private GameObject parent;
	private Ulkapind ulka;
	
	private boolean isMoonPath = false;
	
	public Path(GameObject parent, float r, int noOfPoints) {
		color = new Vector3f(0.5f, 0.5f, 0.5f);
		
		this.parent = parent;
		position = parent.getPosition();
		
		this.r = r;
		dt = 360.0/(double)noOfPoints;
		
		vertices = new float[(noOfPoints+1)*3]; 
		
		createMesh(parent.getPosition());
		generate();
	}
	
	public Path(GameObject parent,  Ulkapind ulka, int noOfPoints) {
		color = new Vector3f(0.5f, 0.1f, 0.1f);
		this.ulka = ulka;
		this.parent = parent;
		this.position = new Vector3f(parent.getPosition());
		
		dt = 360.0/(double)noOfPoints;
		
		vertices = new float[(noOfPoints+1)*3]; 
		
		createMeshOval(parent.getPosition());
		generate();
	}
	
	private void createMesh(Vector3f centre) {						
		t=0;
		for(int i=0; i<vertices.length/3; i++){
			t = t + dt;
			double x = centre.x + r*Math.cos(Math.toRadians(t));
			double y = centre.y;
			double z = centre.z + r*Math.sin(Math.toRadians(t));
			
			vertices[3*i] = (float) x;
			vertices[3*i + 1] = (float) y;
			vertices[3*i + 2] = (float) z;

		}
		
	}
	
	private void createMeshOval(Vector3f centre) {
		t=0;
		
		
		ArrayList<Vector3f> verticesList = new ArrayList<Vector3f>();
		ArrayList<Vector3f> verticesList2 = new ArrayList<Vector3f>();
		ArrayList<Vector3f> verticesList3 = new ArrayList<Vector3f>();
		
		for(int i=0; i<vertices.length/3; i++) {
			t += dt;
			
			double x = ( ulka.getb() * Math.cos(t) + ulka.getShift() ) * Math.cos(ulka.getAngle()) -  ulka.geta() * Math.sin(t) * Math.sin(ulka.getAngle()) ;
			double z = ( ulka.getb() * Math.cos(t) + ulka.getShift()) * Math.sin(ulka.getAngle())  +  ulka.geta() * Math.sin(t) * Math.cos(ulka.getAngle());
			double y = ulka.getCentre().y;

			verticesList.add(new Vector3f((float)x, (float)y, (float)z));
		}
		
		
		verticesList2.add(verticesList.get(0));
		verticesList.remove(0);
		
		while(!verticesList.isEmpty()) {
			float shortest = 100000f;
			int index = 0;
			for(int i=0; i<verticesList.size(); i++) {
				
				float dist = verticesList2.get(verticesList2.size()-1).distance(verticesList.get(i));
				if( dist < shortest ) {
					shortest = dist;
					index = i;
				}
			}
			
			verticesList2.add(verticesList.get(index));
			verticesList.remove(index);
		}

			for(int i=0; i <verticesList2.size(); i++) {
				vertices[3*i] = verticesList2.get(i).x;
				vertices[3*i + 1] = verticesList2.get(i).y;
				vertices[3*i + 2] = verticesList2.get(i).z;
			}
			
			vertices[vertices.length-1] = verticesList2.get(0).z;
			vertices[vertices.length-2] = verticesList2.get(0).y;
			vertices[vertices.length-3] = verticesList2.get(0).x;
	}
	
	public void generate() {
		vao = glGenVertexArrays();
    	glBindVertexArray(vao);
    	vbo = glGenBuffers();
    	glBindBuffer(GL_ARRAY_BUFFER,vbo);
    	glBufferData(GL_ARRAY_BUFFER,vertices,GL_STATIC_DRAW);
    	glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
	}
	
	
	public void update() {
		if(isMoonPath)
			setPosition(parent.getPosition());
	}
	
    public void render(double dt){
    	
    	glBindVertexArray(vao);
    	glEnableVertexAttribArray(0);
    	glDrawArrays(GL_LINE_STRIP, 0, vertices.length/3);
    	glDisableVertexAttribArray(0);
    	glBindVertexArray(0);
    }
	
	public void setPosition(Vector3f targetPosition) {
		this.position.x = targetPosition.x;
		this.position.y = targetPosition.y;
		this.position.z = targetPosition.z;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setColor(float r, float g, float b) {
		color.x = r;
		color.y = g;
		color.z = b;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public void cleanUp() {
		glBindBuffer(GL_ARRAY_BUFFER,0);
		glDisableVertexAttribArray(0);
		glDeleteBuffers(vbo);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
		glBindVertexArray(0);
    	glDeleteVertexArrays(vao);
	}
	
	public void isAMoonPath() {
		isMoonPath = true;
	}
	
	
}
