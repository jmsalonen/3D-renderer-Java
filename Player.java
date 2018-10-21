


public class Player {
  
  public double x, y, xd, yd, rd, rot; 
  
  public Player() {
    
  }
  
  public void update(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight) {
    
    double wSpeed = 0.01;
    double rSpeed = 0.01;
    
    if (up) 
      ++xd;
    if (down) 
      --xd;
    if (left) 
      --yd;
    if (right) 
      ++yd;
    if (turnLeft) 
      ++rd;
    if (turnRight) 
      --rd;
    
    
    double rCos = Math.cos(rot);
    double rSin = Math.sin(rot);
    
    x += (xd * -rSin + yd * rCos) * wSpeed;
    y += (xd * rCos + yd * rSin) * wSpeed;
    
    rot += rd * rSpeed;
    //rot -= rd * rSpeed;
    
    xd *= 0.9; 
    yd *= 0.9; 
    rd *= 0.9; 
    
  }
  
  public void render() {
    
  }
  
}

