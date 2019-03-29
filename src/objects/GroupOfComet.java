package objects;

import java.util.ArrayList;

import org.joml.Vector3f;

public class GroupOfComet {
	public Ulkapind[] comets;
	public boolean[] alive;
	public boolean[] willRender;
	
	public float increament = 0.01f;
	public float counter=0;
	
	public float time = 0;
	public int timeLimit=1200;
	
	private Mesh ulkaMesh;
	

	private GameObject sun;
	
	int members;
	
	int currentMembers;
	
	
	private float maxUlkaSize = 0.5f; private float minUlkaSize = 0.2f;
	private int maxDistance = 15; private int minDistance = 7;
	private float maxAngle = 360f;
	private float maxW = 2f; private float minW = 0.5f;
	private int pathPoints = 40;
    
	
	public GroupOfComet(Mesh ulkaMesh, GameObject sun){
	this.ulkaMesh = ulkaMesh;
	this.sun = sun;
	comets = new Ulkapind[3];

    alive = new boolean[3];
    willRender = new boolean[3];
    for(int i=0; i<3; i++){
    	alive[i] = false;
    	willRender[i] = false;
    }
	}
	
	public void update(ArrayList<GameObject> gameComets, ArrayList<Path> cometOrbits, ArrayList<GameObject> collideWith){
		counter+=increament;
		if(counter > 1f){
			counter = 0;
			time++;
		}
	    if(time > timeLimit){
	    	time = 0;
	    }
        
	    // check death
	    for(int i=0; i<3; i++){
	    	if(comets[i] != null && comets[i].isDead()){
	    		alive[i] = false;
	    		cometOrbits.remove(comets[i].getPath());
	    	}
	    	if(comets[i]!=null && !comets[i].willRender() && comets[i].isDead()){
	    		//comets[i].animateDeath();
	    		gameComets.remove(comets[i]);
	    		willRender[i] = false;
	    	}

    	

	    }
	    move(gameComets,cometOrbits,collideWith);
	    
	    updateLife(gameComets,cometOrbits);
	    
	 
	    
	}
	
    public void updateLife(ArrayList<GameObject> gameComets, ArrayList<Path> cometOrbits){
    	if(time == 300){
    		//spawn 0
    		if(!alive[0] && !willRender[0]){
    			this.comets[0] = this.createRandomUlka();
    			alive[0] = true;
    			willRender[0] = true;
    			comets[0].setDead(false);
    			gameComets.add(comets[0]);
    			cometOrbits.add(comets[0].getPath());
    		}
	    }
	    if(time == 900){
	    	//kill 0 if not dead
	    	if(alive[0] && willRender[0]){
	    		alive[0] = false;
	    		comets[0].setDead(true);
	    		comets[0].setTangent();
	    		cometOrbits.remove(comets[0].getPath());
	    	}
	    }
	    if(time == 600){
	    	//spawn 1
	    	if(!alive[1] && !willRender[1]){
	    		this.comets[1] = this.createRandomUlka();
	    		alive[1] = true;
	    		willRender[1] = true;
	    		comets[1].setDead(false);
	        	gameComets.add(comets[1]);	
	        	cometOrbits.add(comets[1].getPath());
	    	}
	    }
	    if(time == 1200 || time == 0){
	    	//kill 1 if not dead
	    	if(alive[1] && willRender[1]){
	    		alive[1] = false;
	    		comets[1].setDead(true);
	    		comets[1].setTangent();
	    	    cometOrbits.remove(comets[1].getPath());
	    	    
	    	}
	    }
	    if(time == 900){
	    	// spawn 2
	    	if(!alive[2] && !willRender[2]){
	    		this.comets[2] = this.createRandomUlka();
	    		alive[2] = true;
	    		comets[2].setDead(false);
	    		willRender[2] = true;
	    		gameComets.add(comets[2]);
	        	cometOrbits.add(comets[2].getPath());
	    	}
	    }
	    if(time == 300){
	    	//kill 2 if not dead
	    	if(alive[2] && willRender[2]){
	    		alive[2] = false;
	    		comets[2].setDead(true);
	    		comets[2].setTangent();
	    	    cometOrbits.remove(comets[2].getPath());	    	    
	    	}
	    }
    }
    
    private Ulkapind createRandomUlka(){
    	float size = (float)(Math.random()*(maxUlkaSize-minUlkaSize) + minUlkaSize);
    	int dis = (int)(Math.random()*(maxDistance-minDistance) + minDistance);
    	float angle = (float)(Math.random()*maxAngle);
    	float w = (float)(Math.random()*(maxW- minW) + minW );
    	Ulkapind ulka = new Ulkapind(ulkaMesh,size,sun.getPosition(),dis,angle,w);
    	Path p = new Path(sun,ulka,pathPoints);
    	ulka.setPath(p);
    	return ulka;
    }
    
    private void move(ArrayList<GameObject> gameComets, ArrayList<Path> cometOrbits, ArrayList<GameObject> collideWith){
    	for(int i=0; i<gameComets.size(); i++){
    		GameObject g = gameComets.get(i);
    		Ulkapind ulk = (Ulkapind)g;
    		ulk.update(collideWith);
    		if(!ulk.isDead())
    			ulk.getPath().update();
    	}
    }
	
}
