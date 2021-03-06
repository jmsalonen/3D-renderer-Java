

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class InputHandler implements KeyListener, MouseListener, FocusListener, MouseMotionListener, MouseWheelListener {
  
  public boolean keys[]; 
  
  public InputHandler() {
    keys = new boolean[65535];
  }
  
  @Override
  public void mouseWheelMoved(MouseWheelEvent arg0) {  
  }
  
  @Override
  public void mouseClicked(MouseEvent arg0) {  
  }
  
  @Override
  public void mouseEntered(MouseEvent arg0) {  
  }
  
  @Override
  public void mouseExited(MouseEvent arg0) {  
  }
  
  @Override
  public void mousePressed(MouseEvent arg0) {  
  }
  
  @Override
  public void mouseReleased(MouseEvent arg0) {  
  }
  
  @Override
  public void mouseDragged(MouseEvent arg0) {  
  }
  
  @Override
  public void mouseMoved(MouseEvent arg0) {  
  }
  
  @Override
  public void focusGained(FocusEvent arg0) {
  }
  
  @Override
  public void focusLost(FocusEvent arg0) {
    for (int i = 0; i < keys.length; ++i) {
      keys[i] = false; 
    }
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    
    keys[e.getKeyCode()] = true; 
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    
    keys[e.getKeyCode()] = false; 
  }
  
  @Override
  public void keyTyped(KeyEvent arg0) {

  }  
  
}
