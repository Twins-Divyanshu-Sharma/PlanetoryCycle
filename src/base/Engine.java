package base;

import objects.MouseInput;

public class Engine implements Runnable{

	private double fps = 60.0 ;
	private double msPerFrame = 10/fps;
	
	private boolean runing = false;
	private Window window;
	private Game game;
	private MouseInput mouseInput;
	
	private Thread thread;
	
	
	public void init(){
		thread = new Thread(this);
		thread.start();
	}
	
	public void start() throws Exception{
		runing = true;
		window = new Window();
		mouseInput = new MouseInput();
		mouseInput.setCursor(window, "target.png");
		mouseInput.init(window);
		game = new Game(window);
		game.init();
		System.out.println("in Start below Game");
	}
	
	public void stop(){
	     game.cleanUp();
		window.shutdown();
	}
	
	private void processInput(){
		mouseInput.input(window);
       game.input(window,mouseInput);
	}
	
	private void update(){
		game.update(mouseInput);	
		window.poll();
	}
	
	private void render(double dt){
		window.clear();
	    game.render(dt);
		window.swapBuffers();
	}
	
	
	public void run(){
		try{
			start();
			loop();
			stop();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	///////////////////////////////////GAME LOOP//////////////////////////////
	
	private void loop(){
		double previous = System.currentTimeMillis();
		double current = 0;
		double lag=0,elapsed=0;
        System.out.println("inLoop");
    	while(runing){
   	     current = System.currentTimeMillis();
   	     elapsed = current - previous;
   	     lag += elapsed;
   	     previous = current;      	     
   	     processInput();   	     
   	            while(lag >= msPerFrame){
   		   		   update();
   		           lag -= msPerFrame;
   	            }
   	     render(lag/msPerFrame);
   	     
   	     if(game.shouldQuit()){
   	    	 window.close();
   	    	 runing = false;
   	     }
     }
		stop();
	}
}
