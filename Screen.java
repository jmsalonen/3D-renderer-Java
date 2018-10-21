
/* GRAPHICS */

import java.util.Random; 

public class Screen extends Bitmap {
  
  public Random r = new Random(); 
  
  public Bitmap test; 
  public Bitmap3D perspectiveVision; 
  
  public Screen(int width, int height) {
    
    super(width, height);
    
    test = new Bitmap(50, 50); 
    perspectiveVision = new Bitmap3D(width, height);
  }
  
  public void render(Game game) {

    clear(); 
    perspectiveVision.render(game);
    perspectiveVision.renderFog();
    render(perspectiveVision, 0, 0);
//  render(test, (width - 50) / 2 + ox, (height - 50) / 2 + oy);
  }
  
  public void update() {
    
  }
  
}

