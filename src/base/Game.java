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
import objects.GuiNumbers;
import objects.GuiObject;
import objects.Material;
import objects.Mesh;
import objects.MouseInput;
import objects.Player;
import objects.SpriteSheet;
import objects.Surya;
import objects.Texture;
import objects.Ulkapind;



public class Game {
	private int netCycles=0;
	private int netJumps=0;
	private int finalScore=0;
	
	public boolean newGame = true;
	
     public enum State  {
			MENU , INGAME, QUIT
	}
	

	 public State state = State.MENU;

	 public CursorOn  buttonId = CursorOn.NOWHERE;
	 
	 
     private Renderer renderer;
     private Window window;
     private ArrayList<GameObject> objList = new ArrayList<GameObject>();
     private ArrayList<GameObject> objList2 = new ArrayList<GameObject>();
     private ArrayList<GameObject> cometList = new ArrayList<GameObject>();
     
     private ArrayList<GuiObject> hudList = new ArrayList<GuiObject>();
     
     
     
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

     ///////////////////////////// GUI /////////////////////////////////////
     private GuiObject bg;
     private GuiObject buttonHighlight; private boolean renderHighlight = false;
     private ArrayList<GuiObject> currMenu;
     private GuiObject cursor;
     
     
     private ArrayList<GuiObject> baseMenu = new ArrayList<GuiObject>(); 
     private ArrayList<GuiObject> helpMenu = new ArrayList<GuiObject>();
     private GuiObject resume;
     private GuiObject newgame;
     private GuiObject help;
     
     private GuiObject gameOver;
     private GuiNumbers guiNetNumbers;
     private GuiNumbers guiNetJumps;
     
     
     public Game(Window window){
    	 this.window = window;
    	 cameraInc = new Vector3f(0,0,0);
     }
     
     public void init() throws Exception{
         ambientLight = new Vector3f(0.5f, 0.5f, 0.5f);    	 
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
    	 Vector2f bgPos = new Vector2f(-1f,1f);
    	 Vector4f bgColor = new Vector4f(menuBG, 1f);
    	 bg = new GuiObject(bgPos,bgColor,2f,2f);
    	 
    	 buttonHighlight = new GuiObject(new Vector2f(0,0), new Vector4f(1f,1f,1f,0.3f),GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
    	 
    	 float sizeX = (64f/Window.width); float sizeY = (64f/Window.height);
    	 cursor  = new GuiObject(new Vector2f(0f,0f),"Cursor.png",sizeX,sizeY);   	
    	 
    	 // numbers
    	 SpriteSheet numAtlas = new SpriteSheet("squareNumb.png",4,4);
    	 this.guiNetNumbers = new GuiNumbers(numAtlas,new Vector2f(-0.95f,0.8f));
    	 for(int i=0; i<guiNetNumbers.getLetters().length; i++){
    		 hudList.add(guiNetNumbers.getLetters()[i]);
    	 }
    	 this.guiNetJumps = new GuiNumbers(numAtlas,new Vector2f(0.6f,0.8f));
    	 for(int i=0; i<guiNetJumps.getLetters().length; i++){
    		 hudList.add(guiNetJumps.getLetters()[i]);
    	 }
    	 
    	 // baseMenuButtons
    	 newgame = new GuiObject(new Vector2f(-0.3f,0.5f), new Vector4f(0.4f,0.4f,0.8f,1f),GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
    	 newgame.setButton(CursorOn.RESUME);
    	 baseMenu.add(newgame);
    	 
    	 resume = new GuiObject(new Vector2f(-0.3f,0.5f), new Vector4f(0.1f,0.6f,0.3f,1f),GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
         resume.setButton(CursorOn.RESUME);
         
         gameOver = new GuiObject(new Vector2f(-0.6f,0.8f), new Vector4f(0f,0f,0f,1f),GuiObject.buttonWIDTH*2, GuiObject.buttonHEIGHT*3);
         
         
         help = new GuiObject(new Vector2f(-0.3f,0f), new Vector4f(0.1f,0.6f,0.3f,1f),GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
         help.setButton(CursorOn.HELP);
         baseMenu.add(help);
         
         
         GuiObject quit = new GuiObject(new Vector2f(-0.3f,-0.5f), new Vector4f(0.1f,0.6f,0.3f,1f),GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
         quit.setButton(CursorOn.QUIT);
         baseMenu.add(quit);
         
         // help Menu
         GuiObject back = new GuiObject(new Vector2f(0.2f,-0.8f), new Vector4f(0.1f,0.6f,0.3f,1f),GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
         back.setButton(CursorOn.BACK);
         helpMenu.add(back);
       
         GuiObject instruction = new GuiObject(new Vector2f(-0.8f,0.8f), new Vector4f(0.8f,0.6f,0.1f,1f), 1.5f,1.5f);
         helpMenu.add(instruction);
         
         currMenu = baseMenu;
     }
     
     
     
     public void input(Window window, MouseInput mi){
    	 
    	 cameraInc.set(0,0,0);
    	 forward = 0; strafe = 0;
    	 
    	 if(window.isKeyPressed(GLFW_KEY_ESCAPE) && state == State.INGAME){
    		state = State.MENU;
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
    	 
          //jump
    	 if(window.isKeyPressed(GLFW_KEY_E) && earth.onLevelRelativeTo(sun)){
    		 earth.jump();
    	 }
    	 if(window.isKeyPressed(GLFW_KEY_M) && moon.onLevelRelativeTo(earth)){
    		 moon.jump();
    	 }
    	 if(window.isKeyPressed(GLFW_KEY_V) && venus.onLevelRelativeTo(sun)){
    		 venus.jump();
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
    	 mi.setMenuMovement(true);
    	 cursor.setPosition(mi.getGlPos());
    	 this.cursorCollision();
    	 if(mi.isLeftButtonPressed()){
    		 changeState();
    	 }
     }
        
     public void updateInGame(MouseInput mi){
       mi.setMenuMovement(false);
    	 
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
    		 
    	earth.update(sun); if(!earth.willRender()){objList.remove(earth);}
    	venus.update(sun);if(!venus.willRender()){objList.remove(venus);}
    	moon.update(earth);if(!moon.willRender()){objList.remove(moon);}
    	
    	
    	if(!cometList.isEmpty())
    	for(int i =0; i<cometList.size(); i++){
    		cometList.get(i).update();
    		Ulkapind u = (Ulkapind)(cometList.get(i));
    		if(u.theCollision(objList))
    			cometList.remove(i);    		
    	}
    
    	if(objList.size() <= 1 && state == State.INGAME){ // only sun
    		state = State.MENU;
    		buttonId = CursorOn.GAMEOVER;
    		changeState();
    	}
    	
    	int curCycles = earth.getCycles() + venus.getCycles() + moon.getCycles();
    	if(curCycles > netCycles){
    		netCycles = curCycles;
    		updateScore(netCycles, this.guiNetNumbers);
    		
    	}
    	int curJumps = earth.getJumps() + venus.getJumps() + moon.getJumps();
    	//System.out.println(curJumps);
    	if(curJumps > netJumps){
    		netJumps = curJumps;
    		System.out.println(netJumps);
    		updateScore(netJumps, this.guiNetJumps);		
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
    	 // decreasing order ... I know its weird
    	 renderer.renderSingleGui(dt,cursor);
    	 if(this.renderHighlight)
    		 renderer.renderSingleGui(dt, buttonHighlight);
    	 renderer.renderGui(dt,this.currMenu);
    	 renderer.renderSingleGui(dt, bg);
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
     
     
     public void reset(){
    	 objList.add(earth); earth.reset();
    	 objList.add(venus); venus.reset();
    	 objList.add(moon); moon.reset();
     }
     
     public void updateScore(int updateTo, GuiNumbers score){
    	 int number = updateTo;
    	 int i=1;
    	 while(number > 0){
    		 int digit = number%10;
    		 number = number/10;
    		 int digitPos = score.getLength() - i;
    		 score.setAndChange(digit, digitPos);
    		 i++;
    	 }
     }
     
     
     //////////////////////////////  HARD CODED MENU SYSTEM /////////////////////////////
     public void changeState(){
    	 if(state == State.MENU){ // do clicky things only in menu state
    		 if(buttonId == CursorOn.RESUME){
    			 state = State.INGAME;
    			 if(newGame){
    				 newGame = false;
    				 baseMenu.remove(newgame);
    				 baseMenu.add(resume);
    			 }
    		 }else if(buttonId == CursorOn.QUIT){
    			 state = State.QUIT;
    		 }else if(buttonId == CursorOn.HELP){
    			 currMenu = helpMenu;
    		 }else if(buttonId == CursorOn.BACK){
    			 currMenu = baseMenu;
    		 }else if(buttonId == CursorOn.GAMEOVER){
    			 baseMenu.remove(resume);
    			 baseMenu.add(gameOver);
    			 baseMenu.remove(help);
    			 currMenu = baseMenu;
    		 }
    	 }
     }
     
     
     public void cursorCollision(){
    	 boolean somewhere = false;
    	 if(state == State.MENU){ // check only if in menu
    		 for(GuiObject gui : this.currMenu){
    			 if(gui.isButton && collidedWith(cursor.getPosition(),gui)){ // if gui is button and mouse on it
    				this.buttonId = gui.id;
    				this.buttonHighlight.setPosition(gui.getPosition());
    				somewhere = true;
    			 }
    		 }
    	 }
    	 this.renderHighlight = somewhere;
    	 if(!somewhere)
    		 this.buttonId = CursorOn.NOWHERE;
     }
     
     public boolean collidedWith(Vector2f cursorPos, GuiObject gui){
    	 boolean xCollision = (cursorPos.x >= gui.getPosition().x	&& cursorPos.x <= gui.getPosition().x + gui.getWidth() );
    	 boolean yCollision = (cursorPos.y <= gui.getPosition().y	&& cursorPos.y >= gui.getPosition().y - gui.getHeight() );
    	 return xCollision && yCollision;
     }
     
     
     
     
}
