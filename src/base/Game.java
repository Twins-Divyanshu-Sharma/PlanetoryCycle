package base;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import fileParsers.OBJFileLoader;
import objects.Camera;
import objects.Chandra;
import objects.GameObject;
import objects.Greha;
import objects.GroupOfComet;
import objects.GuiNumbers;
import objects.GuiObject;
import objects.Material;
import objects.Mesh;
import objects.MouseInput;
import objects.Path;
import objects.Player;
import objects.SpriteSheet;
import objects.Surya;
import objects.Texture;
import objects.Ulkapind;



public class Game {
	
	
	private int netCycles=0;
	private int netJumps=0;
	private int finalScore=0;
	
	private int cometIndex;
	
	private int minObjects = 2;
	
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
     private GameObject keiperBelt;
     private GameObject keiperBelt2;
     private GroupOfComet cometGroup;
     

     private GuiNumbers guiNetNumbers;
     private GuiNumbers guiNetJumps;
     private GuiNumbers guiFinalScore;

     
     private Mesh ulkaMesh;
     
     private ArrayList<Path> pathList = new ArrayList<Path>();
     
     private Vector3f menuBG = new Vector3f(0.1f,0.1f,0.1f);

     ///////////////////////////// GUI /////////////////////////////////////
     private GuiObject bg;
     private GuiObject buttonHighlight; private boolean renderHighlight = false;
     private ArrayList<GuiObject> currMenu;
     private GuiObject cursor;
     
     
     private ArrayList<GuiObject> baseMenu = new ArrayList<GuiObject>(); 
     private ArrayList<GuiObject> helpMenu = new ArrayList<GuiObject>();
     private ArrayList<GuiObject> creditsMenu = new ArrayList<GuiObject>();
     private GuiObject resume;
     private GuiObject newgame;
     private GuiObject help;
     private GuiObject gameOver;
     private GuiObject credits;
     private GuiObject meteor;
     
     public Game(Window window){
    	 this.window = window;
    	 cameraInc = new Vector3f(0,0,0);
     }
     
     public void init() throws Exception{
         ambientLight = new Vector3f(0.25f, 0.25f, 0.25f);    	 
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
    	 sun = new Surya(sunMesh,new Vector3f(0,0,0), new Vector3f(1,1,1), 30f); //sunMesh, position, lightcolour, lightintensity
    	 sun.setScale(2f);
    	 objList.add(sun);
    	 
         //Keiper Belt
         Mesh keiperBeltMesh = OBJFileLoader.loadMesh("KB.obj");
         Texture keiperTexture = new Texture("kb2.png");
         Material matKeiper = new Material(keiperTexture,reflectance);
         keiperBeltMesh.setMaterial(matKeiper);
         this.keiperBelt = new GameObject(keiperBeltMesh);
         keiperBelt.setPosition(sun.getPosition().x, sun.getPosition().y, sun.getPosition().z);
         keiperBelt.setScale(2f,2f,2f);
         objList.add(keiperBelt);
    	
         keiperBelt2 = new GameObject(keiperBeltMesh);
         keiperBelt2.setPosition(sun.getPosition().x, sun.getPosition().y, sun.getPosition().z);
         keiperBelt2.setScale(8f, 8f, 8f);
         keiperBelt2.setRotation(0,0,90);
         objList.add(keiperBelt2);
    		
    	 Path earthOrbit = new Path(sun, earth.getOrbitalRadius(), 50);
    	 earth.setPath(earthOrbit);
    	 pathList.add(earthOrbit);
    	 
    	 Path venusOrbit = new Path(sun, venus.getOrbitalRadius(), 50);
    	 venus.setPath(venusOrbit);
    	 pathList.add(venusOrbit);
    	 
    	 Path moonOrbit = new Path(earth, moon.getOrbitalRadius(), 30);
    	 moonOrbit.isAMoonPath();
    	 moon.setPath(moonOrbit);
    	 pathList.add(moonOrbit);
    	 
    
         cometGroup = new GroupOfComet(ulkaMesh,sun);	 
    	// comet = new Ulkapind(ulkaMesh, 0.5f, sun.getPosition(), 30, 60, 5f);	//cometMesh, size, sunAsFocus1, distanceOfFocus2, pathTiltAngle, orbitalVelocity
    	// cometList.add(comet);
    	 
    	 camera = new Camera();
    	 camera.setPosition(-3.489f, 8.083f, -16.83f);
    	 camera.setRotation(32.87f, -190.4f, 0f);
    	 
    	 //////////////// GUIS ////////////////////////////
    	 SpriteSheet buttonSprites = new SpriteSheet("buttons.png",3,3);
    	 
    	 
    	 Vector2f bgPos = new Vector2f(-1f,1f);
    	 Vector4f bgColor = new Vector4f(menuBG, 1f);
    	 bg = new GuiObject(bgPos,bgColor,2f,2f);
    	 
    	 buttonHighlight = new GuiObject(new Vector2f(0,0), buttonSprites, 2, 2, GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
    
    	 
    	 float sizeX = (64f*3/Window.width); float sizeY = (64f*3/Window.height);
    	 cursor  = new GuiObject(new Vector2f(0f,0f),buttonSprites,0,0,sizeX,sizeY);   	
    	 
    	 // numbers
    	 GuiObject scoreCycles = new GuiObject(new Vector2f(-0.95f,0.92f), "Cycles.png", 0.2f, 0.15f);
    	 hudList.add(scoreCycles);
    	 SpriteSheet numAtlas = new SpriteSheet("squareNumb.png",4,4);
    	 this.guiNetNumbers = new GuiNumbers(numAtlas,new Vector2f(-0.95f,0.8f));
    	 for(int i=0; i<guiNetNumbers.getLetters().length; i++){
    		 hudList.add(guiNetNumbers.getLetters()[i]);
    	 }
    	 GuiObject scoreJumps = new GuiObject(new Vector2f(0.6f,0.92f), "jumps.png",0.2f, 0.15f);
    	 hudList.add(scoreJumps);
    	 this.guiNetJumps = new GuiNumbers(numAtlas,new Vector2f(0.6f,0.8f));
    	 for(int i=0; i<guiNetJumps.getLetters().length; i++){
    		 hudList.add(guiNetJumps.getLetters()[i]);
    	 }
    	 this.guiFinalScore = new GuiNumbers(numAtlas,new Vector2f(0.4f,0f));
    	 
    	 // baseMenuButtons
    	 newgame = new GuiObject(new Vector2f(0.3f,0f),buttonSprites, 1, 2,GuiObject.buttonWIDTH*2, GuiObject.buttonHEIGHT*2);
    	 newgame.setButton(CursorOn.PLAY);
    	 baseMenu.add(newgame);
    	 
    	 resume = new GuiObject(new Vector2f(0.3f,0f), buttonSprites, 2, 0,GuiObject.buttonWIDTH*2, GuiObject.buttonHEIGHT*2);
         resume.setButton(CursorOn.RESUME);
         
         gameOver = new GuiObject(new Vector2f(0.4f,0.8f), "gameOver.png",GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
         
         
         help = new GuiObject(new Vector2f(-0.6f,0f),buttonSprites, 0, 2,GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
         help.setButton(CursorOn.HELP);
         baseMenu.add(help);
         
         Vector2f meteorPos = new Vector2f(help.getPosition().x+0.2f,help.getPosition().y+0.05f);
          meteor = new GuiObject(meteorPos,buttonSprites,0,1,GuiObject.buttonWIDTH/2,GuiObject.buttonHEIGHT/2);
         baseMenu.add(meteor);
         
         Vector2f logoPos = new Vector2f(-0.2f,1f);
         GuiObject logo = new GuiObject(logoPos,"logo.png",GuiObject.buttonWIDTH,GuiObject.buttonHEIGHT);
         baseMenu.add(logo);
         
         credits = new GuiObject(new Vector2f(0.2f,0.7f),buttonSprites, 1, 1,GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
         credits.setButton(CursorOn.CREDITS);
         baseMenu.add(credits);
         
         GuiObject quit = new GuiObject(new Vector2f(-0.9f,0.9f), buttonSprites, 1, 0,GuiObject.buttonWIDTH, GuiObject.buttonHEIGHT);
         quit.setButton(CursorOn.QUIT);
         baseMenu.add(quit);
         
         // help Menu
         GuiObject back = new GuiObject(new Vector2f(0.5f,0.9f), buttonSprites, 2, 1,GuiObject.buttonWIDTH/2, GuiObject.buttonHEIGHT/2);
         back.setButton(CursorOn.BACK);
         helpMenu.add(back);
       
         GuiObject instruction = new GuiObject(new Vector2f(-1f,1f), "HELP.png", 1.2f,1.2f);
         helpMenu.add(instruction);
         
         GuiObject lore = new GuiObject(new Vector2f(0.4f,-0.1f),"Lore.png",0.6f,1f);
         helpMenu.add(lore);
         
         // credits Menu
         GuiObject creditsBack = new GuiObject(new Vector2f(0.2f,0.8f), buttonSprites, 2, 1,GuiObject.buttonWIDTH/2, GuiObject.buttonHEIGHT/2);
         creditsBack.setButton(CursorOn.CREDITSBACK);
         creditsMenu.add(creditsBack);
         
         GuiObject extraCredits = new GuiObject(new Vector2f(-0.8f,0.8f),"Credits.png", 1f,1.5f);
         creditsMenu.add(extraCredits);
         
         currMenu = baseMenu;
         
         
     }
     
     
     
     public void input(Window window, MouseInput mi){
    	 
    	 cameraInc.set(0,0,0);
    	 forward = 0; strafe = 0;
    	 
    	 if(window.isKeyPressed(GLFW_KEY_ESCAPE) && state == State.INGAME){
    		state = State.MENU;
    	 }
    	/* 
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
    	 }*/
    	 
          //jump
    	 if(window.isKeyPressed(GLFW_KEY_W) && earth.onLevelRelativeTo(sun)){
    		 earth.jump();
    	 }
    	 if(window.isKeyPressed(GLFW_KEY_E) && moon.onLevelRelativeTo(earth)){
    		 moon.jump();
    	 }
    	 if(window.isKeyPressed(GLFW_KEY_Q) && venus.onLevelRelativeTo(sun)){
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
    	 cursor.setPosition( mi.getGlPos());
    	 this.cursorCollision();
    	 if(mi.isLeftButtonPressed()){
    		 changeState();
    	 }
     }
        
     public void updateInGame(MouseInput mi){
 
    	 
       mi.setMenuMovement(false); 	 

    	 camera.moveDistance(forward*CAMERA_POS_STEP);
    	 camera.strafeDistance(strafe*CAMERA_POS_STEP);
    		 Vector2f rotVec = mi.getDispPos();
    		 camera.moveRotation(rotVec.x * MOUSE_SENSTIVITY, rotVec.y*MOUSE_SENSTIVITY, 0);  
    		 
    	earth.update(sun);if(earth.isDead()){pathList.remove(earth.getPath());} if(!earth.willRender()){objList.remove(earth);}
    	venus.update(sun);if(venus.isDead()){pathList.remove(venus.getPath());}if(!venus.willRender()){objList.remove(venus);}
    	moon.update(earth);if(moon.isDead()){pathList.remove(moon.getPath());}if(!moon.willRender()){objList.remove(moon);}
    	
    	float roty = keiperBelt.getRotation().y + 0.001f;
    	keiperBelt.getRotation().y = roty;
    	keiperBelt.setRotation(0, roty , 0);
    	if(keiperBelt.getRotation().y > 360)
    		keiperBelt.setRotation(0, 0, 0);
    	
    	float rotx = keiperBelt2.getRotation().x + 0.001f;
    	keiperBelt2.getRotation().x = rotx;
    	keiperBelt2.setRotation(rotx, 0 , 90);
    	if(keiperBelt2.getRotation().y > 360)
    		keiperBelt2.setRotation(0, 0, 90);
    	
    	cometGroup.update(this.cometList,this.pathList,this.objList);
    	

    	if(objList.size() <= this.minObjects && state == State.INGAME){ // only sun and belt
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
    	if(curJumps > netJumps){
    		netJumps = curJumps;
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
    	 renderer.render(dt, pathList, camera);
         renderer.render(dt, objList, camera, ambientLight, sun.getLight());
         renderer.render(dt, cometList, camera, ambientLight, sun.getLight());
         renderer.renderGui(dt, hudList);
     }
     
     public void renderMenu(double dt){
    	 renderer.renderSingleGui(dt, bg);
    	 renderer.renderGui(dt,currMenu);
    	 if(this.renderHighlight)
    		 renderer.renderSingleGui(dt, buttonHighlight);
    	 renderer.renderSingleGui(dt,cursor);
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
    	 for(Path path : pathList){
    		 path.cleanUp();
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
    		 if(buttonId == CursorOn.PLAY){
    				 newGame = false;
    				 baseMenu.remove(newgame);
    				 baseMenu.add(resume);
    				 state = State.INGAME;
    	 	 }else if(buttonId == CursorOn.RESUME){
    			 state = State.INGAME;
    		 }else if(buttonId == CursorOn.QUIT){
    			 state = State.QUIT;
    		 }else if(buttonId == CursorOn.HELP){
    			 currMenu = helpMenu;
    		 }else if(buttonId == CursorOn.BACK){
    			 currMenu = baseMenu;
    		 }else if(buttonId == CursorOn.GAMEOVER){
    			 baseMenu.remove(resume);
    			 createAndAddFinalScore(baseMenu);
    			 baseMenu.add(gameOver);
    			 baseMenu.remove(help);
    			 baseMenu.remove(meteor);
    			 baseMenu.remove(credits);
    			 currMenu = baseMenu;
    		 }else if(buttonId == CursorOn.CREDITS){
    			 currMenu = creditsMenu;
    		 }else if(buttonId == CursorOn.CREDITSBACK){
    			 currMenu = baseMenu;
    		 }
    	 }
     }
     
     
     public void cursorCollision(){
    	 boolean somewhere = false;
    	 if(state == State.MENU){ // check only if in menu
    		 for(int i=0 ; i<currMenu.size(); i++){
    			 GuiObject gui = currMenu.get(i);
    			 if(gui.isButton && collidedWith(cursor,gui)){ // if gui is button and mouse on it
    				this.buttonId = gui.id;
    				this.buttonHighlight.setScale(gui.getWidth()/GuiObject.buttonWIDTH, gui.getHeight()/GuiObject.buttonHEIGHT);
    				this.buttonHighlight.setPosition(gui.getPosition().x, gui.getPosition().y);
    				somewhere = true;
    			 }
    		 }
    	 }
    	 this.renderHighlight = somewhere;
    	 if(!somewhere)
    		 this.buttonId = CursorOn.NOWHERE;
     }
     
     public boolean collidedWith(GuiObject cursor, GuiObject gui){
    	 float boxRatio = 0.2f;
    	 boolean xCollision = (cursor.getPosition().x+cursor.getWidth()*0.5f  >= (gui.getPosition().x+boxRatio*gui.getWidth())	&& cursor.getPosition().x+cursor.getWidth()*0.5f <= gui.getPosition().x + gui.getWidth()*(1-boxRatio) );
    	 boolean yCollision = (cursor.getPosition().y-cursor.getWidth()*0.5f <= (gui.getPosition().y - boxRatio*gui.getHeight())&& 	cursor.getPosition().y-cursor.getHeight()*0.5f >= gui.getPosition().y - gui.getHeight()*(1-boxRatio) );
    	 return xCollision && yCollision;
     }
     
     public void createAndAddFinalScore(ArrayList<GuiObject> someMenu){
    	 updateScore(netCycles - netJumps, this.guiFinalScore);
         for(int i=0; i<guiFinalScore.getLength(); i++){
        	 someMenu.add(guiFinalScore.getLetters()[i]);
         }
     }
     
     
     
}
