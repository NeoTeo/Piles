package piles.shite;

import java.awt.* ;
import java.awt.event.* ;
          
public class QuitRetryDlg extends Dialog 
{
  Piles dad ;  
  public QuitRetryDlg(Frame dw,String title,Piles parent) 
  {
    super(dw, title, true);
    dad = parent ;
    Panel p1 = new Panel();
    p1.setLayout(new FlowLayout(FlowLayout.RIGHT));
    Label label = new Label("No more moves. Would you like to play again ?");
    p1.add(label);
    Button quit = new Button("Quit");
    Button retry = new Button("Retry");
    p1.add(retry);
    p1.add(quit);
    add("Center", p1);
    //Initialize this dialog to its preferred size.
    pack();
    
    quit.addMouseListener(
      new MouseAdapter()
      {
        public void mousePressed(MouseEvent e) 
        {
          System.out.println("quit") ;
          quitgame(true) ;
          dispose() ;
        }                        
      }
    ) ;
    
    retry.addMouseListener(
      new MouseAdapter()
      {
        public void mousePressed(MouseEvent e) 
        {
          System.out.println("retry") ;
          quitgame(false) ;
          dispose() ;
        }                        
      }
    ) ;
  }    
  void
  quitgame(boolean val)
  {
    //dad.game_on = val ;
    if(val)
      dad.quit() ;
    else
      dad.restart() ;
  }
}
