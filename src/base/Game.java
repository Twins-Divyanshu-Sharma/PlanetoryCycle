package base;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import fileParsers.OBJFileLoader;
import objects.Camera;
import objects.Chandra;
import objects.GameObject;
import objects.Greha;
import objects.GuiObject;
import objects.Material;
import objects.Mesh;
import objects.MouseInput;
import objects.Player;
import objects.Surya;
import objects.Texture;
import objects.Ulkapind;



public class Game {
	public boolean newGame = true;
	
     public enum State  {
			MENU , INGAME, QUIT
	};
	
	 public State state = State.MENU;
	
     private Renderer renderer;
     private Window window;
     private ArrayList<GameObject> objList = new ArrayList<GameObject>();
     private ArrayList<GameObject> objList2 = new ArrayList<GameObject>();
     private ArrayList<GameObject> cometList = new ArrayList<GameObject>();
     
     private ArrayList<GuiObject> hudList = new ArrayList<GuiObject>();
     
     private ArrayList<GuiObject> baseMenuList = new ArrayList<GuiObject>();
     
     private Camera camera;
     private Player player;
     private Vector3f cameraInc;
     private float CAMERA_POS_STEP = 0.0005f;
     private float MOUSE_SENSTIVITY = 0.002f;
     
     private float forward = 0;
     private float strafe = 0;
     
     private Vector3f ambientLight;
     private float reflectance = 0.3f;
     private Greha earth;
     private Chandra moon;
     private Greha venus;
     private Surya sun;
     private Ulkapind comet;
     
     private float t , dt = 0.03f;
     private float limit0 =200 + (float) Math.random()*1000;
     private float limit1 =500 + (float) Math.random()*1000;
     private float limit2 = 700 + (float) Math.random()*1000;
     
     private boolean lm0bool = false, lm1bool=false, lm2bool=false;
     private boolean layer1 = true;
     
     private Mesh ulkaMesh;
     private Vector3f menuBG = new Vector3f(0.2f,0.7f,0.6f);

     
     public Game(Window window){
    	 this.window = window;
    	 cameraInc = new Vector3f(0,0,0);
     }
     
     public void init() throws Exception{
         ambientLight = new Vector3f(0.1f, 0.1f, 0.1f);    	 
         String planetOBJ = "planet.obj";
    	 String suryaObj = "sun.obj";
    	 String cometObj = "comet.obj";
    	 renderer = new Renderer(window.getAspectRatio());
    	 
    	 Mesh sphereMesh = OBJFileLoader.loadMesh(planetOBJ);
    	 Mesh suryaMesh = OBJFileLoader.loadMesh(suryaObj);
         
    	 ulkaMesh = OBJFileLoader.loadMesh(cometObj);
         Texture ulk = new Texture("comet.png");
         Material ulkMat = new Material(ulk,reflectance);
         ulkaMesh.setMaterial(ulkMat);
 
    	 Mesh earthMesh = new Mesh(sphereMesh); 
    	 Texture tex = new Texture("Green.png");
    	 Material mat = new Material(tex,reflectance);
    	 earthMesh.setMaterial(mat);
         earth = new Greha(earthMesh, 1f, 10f, 1.5f, 0f); // mesh , size, orbitalradius, orbitalW, phase
         objList.add(earth);
         
         
         //Mesh moonMesh = OBJFileLoader.loadMesh(planetOBJ);
         Mesh moonMesh = new Mesh(sphereMesh);
         Texture texMoon = new Texture("moon.png");
    	 Material matMoon = new Material(texMoon,reflectance);
         moonMesh.setMaterial(matMoon);
         moon = new Chandra(moonMesh, 0.2f, 2f, 5f, 0f);
         objList.add(moon);
         
    	// Mesh venusMesh = OBJFileLoader.loadMesh(planetOBJ);
         Mesh venusMesh = new Mesh(sphereMesh);
    	 Texture texVenus = new Texture("Violet.png");
    	 Material matVenus = new Material(texVenus,reflectance);
    	 venusMesh.setMaterial(matVenus);
    	 venus = new Greha(venusMesh, 1f, 5f, 1f, 180f); // mesh, size, orbitalradius, orbitalW, phase
    	 objList.add(venus);
    	 
    	 //Mesh sunMesh = OBJFileLoader.loadMesh("bap.obj");
    	 Mesh sunMesh = new Mesh(suryaMesh);
    	 Texture texSun = new Texture("SUN.png");
    	 Material matSun = new Material(texSun,reflectance);
    	 matSun.setAmbientColor(new Vector4f(1,1,1,1));
    	 matSun.setEmission(true);
    	 sunMesh.setMaterial(matSun);
    	 sun = new Surya(sunMesh,new Vector3f(0,0,0), new Vector3f(1,1,1), 10f); //sunMesh, position, lightcolour, lightintensity
    	 sun.setScale(2f);
    	 objList.add(sun);
    	 
    	 comet = new Ulkapind(ulkaMesh, 0.5f, sun.getPosition(), 30, 15, 5f);	//cometMesh, size, sunAsFocus1, distanceOfFocus2, pathTiltAngle, orbitalVelocity
    	// cometList.add(comet);
    	 
    	 camera = new Camera();
    	 camera.setPosition(-3.489f, 8.083f, -16.83f);
    	 camera.setRotation(32.87f, -190.4f, 0f);
    	 
    	 //////////////// GUIS ////////////////////////////
    	 Vector2f test0Pos = new Vector2f(-1f,1f);
    	 Vector4f test0Color = new Vector4f(menuBG, 0.4f);
    	 GuiObject test0 = new GuiObject(test0Pos,test0Color,2f,2f);
    	 baseMenuList.add(test0);
    	 
     }
     
     
     
     public void input(Window window, MouseInput mi){
    	 cameraInc.set(0,0,0);
    	 forward = 0; strafe = 0;
    	 if(window.isKeyPressed(GLFW_KEY_O)){
    		 state = State.QUIT;
    	 }
    	 
    	 if(window.isKeyPressed(GLFW_KEY_ESCAPE) && state == State.INGAME){
    		state = State.MENU;
    	 }
    	 if(window.isKeyPressed(GLFW_KEY_BACKSPACE) && state == State.MENU){
    		 state = State.INGAME;
    	 }
    	 
    	 
    	 if(window.isKeyPressed(GLFW_KEY_UP)){
    		 cameraInc.z = -1;
    		 forward = 1;
    	 }else if(window.isKeyPressed(GLFW_KEY_DOWN)){
    		 cameraInc.z = 1;
    		 forward  = -1;
    	 }
    	 
    	 if(window.isKeyPressed(GLFW_KEY_LEFT)){
    		 cameraInc.x = -1;
    		 strafe = -1;
    	 }else if(window.isKeyPressed(GLFW_KEY_RIGHT)){
    		 cameraInc.x = 1;
    		 strafe = 1;
    	 }
    	 
    	 // JUMP CONTROLS
    	 if(window.isKeyPressed(GLFW_KEY_G) && earth.onLevelRelativeTo(sun)){ // for earth
    	    earth.jump();
    	 }
    	 
    	 if(window.isKeyPressed(GLFW_KEY_V) && venus.onLevelRelativeTo(sun) ){  // for venus
    		venus.jump(); 
    	 }
    	 
    	 if(window.isKeyPressed(GLFW_KEY_M) && moon.onLevelRelativeTo(earth)){   // for moon
    		 moon.jump();
    	 }
    	 
     }
     
     public void update(MouseInput mi){
    	 if(state == State.INGAME){
    		 updateInGame(mi);
    	 }else if(state == State.MENU){
    		 updateMenu(mi);
    	 }
     }
     
     public void updateMenu(MouseInput mi){
    	 
     }
     
     
     
     public void updateInGame(MouseInput mi){
       
    	 
    	 t += dt;
    	 if( t >= limit0 && !lm0bool){
    		 cometList.add(comet);
    		 lm0bool = true;
    	 }
    	 if(t >= limit1 && !lm1bool){
    		 Ulkapind dynComet = new Ulkapind(ulkaMesh, 0.5f, sun.getPosition(), 15, 10, 5f);	//cometMesh, size, sunAsFocus1, distanceOfFocus2, pathTiltAngle, orbitalVelocity;
    		 cometList.add(dynComet);
    		 lm1bool = true;
    	 }
    	 if(t >= limit2 && !lm2bool){
    		 Ulkapind dynComet = new Ulkapind(ulkaMesh, 0.5f, sun.getPosition(), 60, 20, 5f);
    		 cometList.add(dynComet);
    		 lm2bool = true;
    		 t = 0;
    	 }
    	 camera.moveDistance(forward*CAMERA_POS_STEP);
    	 camera.strafeDistance(strafe*CAMERA_POS_STEP);
    		 Vector2f rotVec = mi.getDispPos();
    		 camera.moveRotation(rotVec.x * MOUSE_SENSTIVITY, rotVec.y*MOUSE_SENSTIVITY, 0);  
    		 
    	System.out.println(mi.getGlPos());	
    		 
    	earth.update(sun);
    	venus.update(sun);
    	moon.update(earth);
    	
    	if(!cometList.isEmpty())
    	for(int i =0; i<cometList.size(); i++){
    		cometList.get(i).update();
    		Ulkapind u = (Ulkapind)(cometList.get(i));
    		if(u.theCollision(objList))
    			cometList.remove(i);    		
    	}
    
    	
    	
     }
     
     public void render(double dt){
         if(state == state.INGAME){
        	 renderScene(dt);
         }else if(state == state.MENU){
        	 renderMenu(dt);
         }
     }
     
     public void renderScene(double dt){
         renderer.render(dt, objList, camera, ambientLight, sun.getLight());
         renderer.render(dt, cometList, camera, ambientLight, sun.getLight());
         renderer.renderGui(dt, hudList);
     }
     
     public void renderMenu(double dt){
         renderer.render(dt, objList, camera, ambientLight, sun.getLight());
         renderer.render(dt, cometList, camera, ambientLight, sun.getLight());
    	 renderer.renderGui(dt, baseMenuList);
     }
     
     public void cleanUp(){

    	 renderer.cleanUp();
    	 for(GameObject go : objList){
    		 go.cleanUp();
    	 }
    	 for(GameObject go : objList2){
    		 go.cleanUp();
    	 }
    	 for(GuiObject gui : hudList){
    		 gui.cleanUp();
    	 }

     }
     
     public boolean shouldQuit(){
    	 return (state == State.QUIT);
     }
}
