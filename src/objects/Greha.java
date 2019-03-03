package objects;

public class Greha extends GameObject{
	// Static fields
	private static float dt = 0.0001f;
	private static float squash = 0.8f;
    private static float stretch = 1.025f;
	
    // visible fields
	private float orbitalRadius;   // orbitalRadius from sun to planet
	private float size;
	private float jumpSpeed = 0.005f;
	private float phase = 0f;  // in radians
	private float orbitalW;
	private float gravity = -0.000002f;
	
	// not visible fields
	private float t=0;	
	private float jumpVelocity = 0;
	
 public Greha(Mesh mesh, float size, float orbitalRadius, float orbitalW, float phase){
	 super(mesh);
	 this.size = size;
	 this.setScale(size);
	 this.orbitalRadius = orbitalRadius;
	 this.phase = (float)(Math.toRadians(phase)); // supplied in degrees, converted to radians
	 this.orbitalW = orbitalW;
 }
 
 // neccessary fields
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
 
 // not so neccessary fields
 public void setJumpSpeed(float jumpSpeed){
	 this.jumpSpeed = jumpSpeed;
 }
 
 public float getJumpSpeed(){
	 return jumpSpeed;
 }
 
 public void setGravity(float gravity){
	 this.gravity = gravity;
 }
 
 public float getGravity(){
     return gravity;	 
 }
 
 public void update(Surya surya){
     move(surya);
     animate();
 }
 
 private void move(Surya surya){
	 t += dt;
	
	 float x = surya.getPosition().x + (float)(orbitalRadius * Math.cos(orbitalW * t + phase)); 
	 float y = this.getPosition().y + jumpVelocity;
	 float z = surya.getPosition().z + (float)(orbitalRadius * Math.sin(orbitalW * t + phase));
	 
	 if(y > surya.getPosition().y){
		 jumpVelocity += surya.getGravity();
		 this.setScale(size*squash, size*stretch, size*squash);
	 }
	 
	 if( y < surya.getPosition().y ){
		 y = surya.getPosition().y;
		 jumpVelocity = 0;
		 this.setScale(size);
	 }
	 
	 this.setPosition(x, y, z);
	 
	 if(orbitalW * t > 2*Math.PI){
		 t = 0;
	 }
	 
	 // set tidal locking
	 float rotationalTheta = (float)(Math.toDegrees( orbitalW * t  + phase + (Math.PI)));  // pi to adjust texture
	 this.setRotation(0, rotationalTheta, 0);
	 
	 
 }
 
 private void animate(){
	 
 }
 
 public void jump(){
	       jumpVelocity = jumpSpeed;
 }
 
 public boolean onLevelRelativeTo(GameObject go){
	return this.getPosition().y == go.getPosition().y ? true : false;
	 
 }

 
}
