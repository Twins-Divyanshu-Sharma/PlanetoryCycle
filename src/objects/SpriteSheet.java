package objects;

public class SpriteSheet {
      private Texture tex;
      private int rows, cols;
      
      private float dw, dh;
      
  	public SpriteSheet(String texloc, int rows ,int cols) throws  Exception{
  	  tex = new Texture(texloc);
  	  this.rows = rows;
  	  this.cols = cols;
  	  
  	  dh = 1f/( (float)rows  );
  	  dw = 1f/( (float)cols  );
    }
      

     public Texture getTex() {
		return tex;
	}

	public void setTex(Texture tex) {
		this.tex = tex;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	 	  dh = 1f/( (float)rows  );
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	 	  dw = 1f/( (float)cols  );
	}


	public float getDw() {
		return dw;
	}

	public float getDh() {
		return dh;
	}


}
