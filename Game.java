import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class Game {
  
  public Random random; 
  
  public Level level; 
  public Player player; 
  public int time; 

  public Game() {
    random = new Random();
    player = new Player(); 
    level = new Level(32, 32, random.nextInt());  
  }
  
  public void update(boolean[] keys) {
    ++time; 
    
    boolean up = keys[VK_W];
    boolean down = keys[VK_S];
    boolean left = keys[VK_A];
    boolean right = keys[VK_D];
    boolean turnLeft = keys[VK_LEFT];
    boolean turnRight = keys[VK_RIGHT];
    
    player.update(up, down, left, right, turnLeft, turnRight); 
  }
  
}
