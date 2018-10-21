
/* GRAPHICS */

import java.util.Random; 

public class Bitmap3D extends Bitmap {
  
  private double xCam, yCam, zCam, rot, rSin, rCos, fov;
  private double[] depthBuffer;
  private double[] depthBufferWall;
  
  public Bitmap3D(int width, int height) {
    
    super(width, height); 
    
    depthBuffer = new double[width * height];
    depthBufferWall = new double[width];
  }
  
  public void render(Game game) {
    
    for (int x = 0; x < width; ++x) {
      depthBufferWall[x] = 0;
    }
    
    fov = height;

    xCam = game.player.x; 
    yCam = game.player.y; 
    //zCam = 0; //Math.sin(game.time / 10.0) * 2;

    //rot = Math.sin(game.time / 40.0) * 0.5;
    rot = game.player.rot; 
    
    rSin = Math.sin(rot); 
    rCos = Math.cos(rot); 
    
    for (int y = 0; y < height; ++y) {
      
      double yd = ((y + 0.5) - (height / 2)) / fov; 
      double zd = (4 + zCam) / yd; 
      
      if (yd < 0) {
        zd = (4 - zCam) / -yd; 
      }
      
      for (int x = 0; x < width; ++x) {
        
        double xd = (x - (width / 2)) / fov; 
        xd *= zd; 
        
        double xx = xd * rCos - zd * rSin + (xCam + 0.5) * 8; 
        double yy = xd * rSin + zd * rCos + (yCam) *  8; 
        
        int xPix = (int) xx * 2;
        int yPix = (int) yy * 2;
        
        if (xx < 0) 
          xPix--;
        if (yy < 0) 
          yPix--;
        
        depthBuffer[x + y * width] = zd;
        pixels[x + y * width] = 0x00808080;

      }
    }
    
    Level level = game.level; 
    
    for (int y = -1; y <= level.height; ++y)  {
      for (int x = -1; x <= level.width; ++x)  {
        Block c = level.getBlock(x, y);      // center east west south north
        Block e = level.getBlock(x + 1, y);
        Block w = level.getBlock(x - 1, y);
        Block s = level.getBlock(x, y - 1);
        Block n = level.getBlock(x, y + 1);
        
        if (!c.SOLID) 
          continue;
        
        if (!e.SOLID)  
          renderWall(x + 1, y, x + 1, y + 1); 
        if (!w.SOLID)  
          renderWall(x, y + 1, x, y); 
        if (!s.SOLID)  
          renderWall(x, y, x + 1, y); 
        if (!n.SOLID)  
          renderWall(x + 1, y + 1, x, y + 1); 
        
      }
    }
  }
  
  public void renderFog() {
    
    for (int i = 0; i < depthBuffer.length; ++i) {
      int color = pixels[i];
      int r = (color >> 16) & 0xff; 
      int g = (color >> 8) & 0xff; 
      int b = (color) & 0xff; 
      
      double brightness = 50000 / (depthBuffer[i] * depthBuffer[i]);
      
      if (brightness < 0) {
        brightness = 0;
      }
      if (brightness > 255) {
        brightness = 255;
      }
      
      r = (int) (r / 255.0 * brightness);
      g = (int) (g / 255.0 * brightness);
      b = (int) (b / 255.0 * brightness);
      
      pixels[i] = r << 16 | g << 8 | b;
    }
    
  }
  
  public void renderWall(double x0, double y0, double x1, double y1) {
    
    double xo0 = x0 - 0.5 - xCam;
    double u0 = - 0.5 + zCam / 8;
    double d0 = + 0.5 + zCam / 8;
    double zo0 = y0 - yCam;
    
    double xx0 = xo0 * rCos + zo0 * rSin;
    double zz0 = -xo0 * rSin + zo0 * rCos;
    
    double xo1 = x1 - 0.5 - xCam;
    double u1 = - 0.5 + zCam / 8;
    double d1 = + 0.5 + zCam / 8;
    double zo1 = y1 - yCam;
    
    double xx1 = xo1 * rCos + zo1 * rSin;
    double zz1 = -xo1 * rSin + zo1 * rCos;
    
    double t0 = 0; 
    double t1 = 16; 
    
    double clip = 0.1; 
    
    if (zz0 < clip) {
      double p = (clip - zz0) / (zz1 - zz0);
      zz0 = zz0 + (zz1 - zz0) * p;
      xx0 = xx0 + (xx1 - xx0) * p; 
      t0 = t0 + (t1 - t0) * p; 
    }
    
    if (zz1 < clip) {
      double p = (clip - zz1) / (zz1 - zz0);
      zz1 = zz1 + (zz1 - zz0) * p;
      xx1 = xx1 + (xx1 - xx0) * p; 
      t1 = t1 + (t1 - t0) * p; 
    }
    
    double xPixel0 = xx0 / zz0 * fov + width / 2.0;
    double xPixel1 = xx1 / zz1 * fov + width / 2.0;
    
    if (xPixel0 > xPixel1)
      return; 
    int xp0 = (int) Math.ceil(xPixel0);
    int xp1 = (int) Math.ceil(xPixel1);
    if (xp0 < 0) 
      xp0 = 0;
    if (xp1 > width) 
      xp1 = width;
    
    double yPixel00 = (u0 / zz0 * fov + height / 2.0) + 0.5; 
    double yPixel10 = (u1 / zz1 * fov + height / 2.0) + 0.5; 
    double yPixel01 = (d0 / zz0 * fov + height / 2.0) + 0.5; 
    double yPixel11 = (d1 / zz1 * fov + height / 2.0) + 0.5;
    
    double iz0 = 1 / zz0;
    double iz1 = 1 / zz1; 
    
    double xt0 = t0 * iz0; 
    double xt1 = t1 * iz1; 
    
    for (int x = xp0; x < xp1; ++x) {
      
      double p = (x - xPixel0) / (xPixel1 - xPixel0);
      double yPixel0 = yPixel00 + (yPixel10 - yPixel00) * p;
      double yPixel1 = yPixel01 + (yPixel11 - yPixel01) * p;
      double iz = iz0 + (iz1 - iz0) * p;
      
      if (depthBufferWall[x] > iz) {
        continue; 
      }
      depthBufferWall[x] = iz; 

      if (xPixel0 > xPixel1)
        return; 
        
      int yp0 = (int) yPixel0;
      int yp1 = (int) yPixel1;
      
      if (yp0 < 0) 
        yp0 = 0;
      if (yp1 > height) 
        yp1 = height;
      
      for (int y = yp0; y < yp1; ++y) {
        depthBuffer[x + y * width] = 12 / iz;
        pixels[x + y * width] = 0x00C0C0C0;
      }
      
    }
  
  }
  
  public void drawFloor(double xx, double yy, double yd, int x, int y, int xPix, int yPix, int pX, int pY) {
    
    if (yd <= 0) return; // yd < 0 is ceiling
    
    if (xx >= pX * 16 && xx < pX * 16 + 16 && yy >= pY * 16 && yy < pY * 16 + 16) {
      pixels[x + y * width] = 0xff00ff;
    }
    
  }
  
  public void drawCeiling(double xx, double yy, double yd, int x, int y, int xPix, int yPix, int pX, int pY) {
    
    if (yd >= 0) return; // yd > 0 is floor
    
    if (xx >= pX * 16 && xx < pX * 16 + 16 && yy >= pY * 16 && yy < pY * 16 + 16) {
      pixels[x + y * width] = 0xff00ff;
    }
    
  }
  
  
}
























