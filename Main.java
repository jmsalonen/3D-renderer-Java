

/* MAIN */

// 58:00

import java.awt.Canvas; 
import java.awt.Dimension; 
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy; 
import java.awt.image.DataBufferInt; 

import javax.swing.JFrame;


public class Main extends Canvas implements Runnable {
  
  //public static final int WIDTH = 1366; 
  //public static final int HEIGHT = 768; 
  public static final int WIDTH = 320; 
  public static final int HEIGHT = WIDTH * 3 / 4; 
  public static final int SCALE = 2; 
  public static final String TITLE = "3D renderer"; 
  public static final double FRAME_LIMIT = 60.0; 
  
  private boolean isRunning = false; 
  
  public final BufferedImage image;
  public final int[] pixels;
  
  
  private Screen screen; 
  private Game game; 
  private InputHandler inputHandler; 
  
  public Main() {
    Dimension d = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
    setMinimumSize(d); 
    setMaximumSize(d); 
    setPreferredSize(d); 
    
    image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); 
    pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    
    inputHandler = new InputHandler();
    
    addKeyListener(inputHandler); 
    addMouseListener(inputHandler); 
    addFocusListener(inputHandler); 
    addMouseMotionListener(inputHandler); 
    addMouseWheelListener(inputHandler); 
  }
  
  public void start() {
    if (isRunning) 
      return; 
    
    isRunning = true; 
    
    init(); 
    new Thread(this).start();
  }
  
  public void init() {
    game = new Game(); 
    screen = new Screen(WIDTH, HEIGHT); 
  }
  
  
  public void run() {
    
    final double nsPerUpdate = 1000000000.0 / FRAME_LIMIT;
    
    long lastTime = System.nanoTime(); 
    double unprocesssedTime = 0; 
    
    int frames = 0;
    int updates = 0; 
    
    long frameCounter = System.currentTimeMillis(); 
    
    while (isRunning) {
      long currentTime = System.nanoTime(); 
      long passedTime = currentTime - lastTime; 
      lastTime = currentTime; 
      unprocesssedTime += passedTime; 
      
      if (unprocesssedTime >= nsPerUpdate) {
        unprocesssedTime = 0;
        update(); 
        ++updates;
      }
      
      render();
      ++frames;

      if (System.currentTimeMillis() - frameCounter >= 1000) {
        System.out.println("Frames: " + frames + ", Updates: " + updates); 
        frames = 0; 
        updates = 0; 
        frameCounter += 1000; 
      }

    }
    
    dispose(); 
  }
  
  public void render() {
    
    BufferStrategy bs = getBufferStrategy(); 
    
    if (bs == null) {
      createBufferStrategy(3); 
      return; 
    }
    
    Graphics g = bs.getDrawGraphics(); 

    screen.render(game); 
    
    for (int i = 0; i < pixels.length; ++i) {
      pixels[i] = screen.pixels[i];
    }
    
    g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null); 
    
    g.dispose();
    bs.show(); 
    
  }
  
  public void update() {
    
    game.update(inputHandler.keys); 
    screen.update(); 
  }
  
  public void stop() {
    if (!isRunning)
      return; 
      
    isRunning = false; 
  }
  
  public void dispose() {
    System.exit(0);
  }
  
  public static void main(String[] args)  {
    
    JFrame frame = new JFrame(); 
    frame.setTitle(TITLE);
    frame.setResizable(false);
    Main game = new Main(); 

    
    frame.add(game); 
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    frame.setAlwaysOnTop(true);
    frame.setVisible(true); 
    
    game.start(); 
    
  }
  
}



