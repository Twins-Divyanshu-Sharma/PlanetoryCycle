package objects;

public class Chandra extends GameObject{
	
	// Static fields
	private static float dt = 0.0001f;
	private static float squash = 0.5f;
    private static float stretch = 1.2f;
	
    // visible fields
	private float orbitalRadius;   // orbitalRadius from planet to moon
	private float size;
	private float jumpSpeed = 0.0025f;  // moon half jump 
	private float phase = 0f;  // in radians
	private float orbitalW;
	private float orbitalA = 0.05f;
	
	// not visible fields
	private float t=0;	
	private float jumpVelocity = 0;
	
	private int cycles = 0;
	private int jumps = 0;

	
 public Chandra(Mesh mesh, float size, float orbitalRadius, float orbitalW, float phase){
	 super(mesh);
	 this.size = size;
	 this.setScale(size);
	 this.orbitalRadius = orbitalRadius;
	 this.phase = (float)(Math.toRadians(phase)); // supplied in degrees, converted to radians
	 this.orbitalW = orbitalW;
 }
 
 public void setOrbitalRadius(float orbitalRadius){
	 this.orbitalRadius = orbitalRadius;
 }
 
 public float getOrbitalRadius(){
	 return orbitalRadius;
 }
 
 public void setOrbitalW(float orbitalW){
	 this.orbitalW = orbitalW;
 }
 
 public float getOrbitalW(){
	 return orbitalW;
 }
 
 public void setSize(float size){
	 this.size = size;
	 this.setScale(size);
 }
 
 public float getSize(){
	 return size;
 }
 
 public float getPhase(){
	 return (float)(Math.toDegrees(phase));
 }
 
 public void setPhase(float phase){
	 this.phase = (float)(Math.toRadians(phase));
 }
 
 public void setJumpSpeed(float jumpSpeed){
	 this.jumpSpeed = jumpSpeed;
 }
 
 public float getJumpSpeed(){
	 return jumpSpeed;
 }
 
 public void update(Greha greha){
	 if(!isDead()){
		 move(greha);
		 if(greha.isDead())
			 this.setDead(true);
     	 //animate();
	 }else{
		 if(jumpVelocity == 0 && renderMe)
			 jumpVelocity -= jumpSpeed;
		 animateDeath(greha);
	 }
 }
 
 private void move(Greha greha){
	 t += dt;
	
	 float x = greha.getPosition().x + (float)(orbitalRadius * Math.cos(orbitalW * t + phase)); 
	 float y = this.getPosition().y + jumpVelocity;
	 float z = greha.getPosition().z + (float)(orbitalRadius * Math.sin(orbitalW * t + phase));
	 
	 if(y > greha.getPosition().y){
		 jumpVelocity += greha.getGravity();
		 this.setScale(size*squash, size*stretch, size*squash);
	 }
	 
	 if( y < greha.getPosition().y ){
		 y = greha.getPosition().y;
		 jumpVelocity = 0;
		 this.setScale(size);
	 }
	 
	 this.setPosition(x, y, z);
	 
	 if(orbitalW * t > 2*Math.PI){
		 t = 0;
		 cycles++;
	 }
	 
	 // set tidal locking
	 float rotationalTheta = (float)(Math.toDegrees( orbitalW * t  + phase));
	 this.setRotation(0, rotationalTheta, 0);
	 
	 
 }
 
 private void animateDeath(Greha greha){
	 float y = this.getPosition().y + jumpVelocity;
	 if(y > greha.getPosition().y){
		 jumpVelocity += greha.getGravity();
		 this.setScale(size*squash, size*stretch, size*squash);
	 }
	 
	 if( y < greha.getPosition().y - 15f ){
		 y = greha.getPosition().y - 15f;
		 jumpVelocity = 0;
		 this.setScale(size);
		 renderMe = false;
	 }
	 
	 this.setPosition(this.getPosition().x, y, this.getPosition().z);
 }
 
 public void jump(){
	       jumpVelocity = jumpSpeed;
	       jumps++;
 }
 
 public boolean onLevelRelativeTo(GameObject go){
	return this.getPosition().y == go.getPosition().y ? true : false;
	 
 }


 public int getCycles(){
	 return cycles;
 }
 
 public int getJumps(){
	 return jumps;
 }
 
 public void reset(){
	 t=0;
	 this.setDead(false);
	 this.cycles = 0;
	 renderMe = true;
 }

 
}
