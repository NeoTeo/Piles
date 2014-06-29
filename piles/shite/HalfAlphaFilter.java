package piles.shite;

import java.awt.image.* ;

public class HalfAlphaFilter extends RGBImageFilter {

  public HalfAlphaFilter() {
    canFilterIndexColorModel = true ;
  }

  public int filterRGB(int x, int y, int rgb)
  {
    return (rgb & 0x7fffffff) ;
  }
}
