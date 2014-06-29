package piles.shite;

import java.awt.*;
import java.awt.event.* ;
import java.applet.*;

public class StartButton extends Applet
{
    Button button = null ;
    boolean running = false ;
    StartButton thisApplet = this ;
    
	public void init()
	{
        Color background = null ;
	    String value = null ;
        
        try {
            value = getParameter("background") ;
            background = new Color(Integer.parseInt(value,16)) ;
        } catch(Exception e) {
            background = Color.white ;
        }
        
        this.setBackground(background) ;

        button = new Button("Click to play.") ;
      
        this.add(button) ;
        
        button.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                if(!running)
                {
                    Piles game = new Piles(thisApplet) ;
                    running = true ;
                }
            }       
        }) ;
	}

    public void paint(Graphics g)
    {
        //System.out.println("StartButton paint") ;
        if(running)
            button.setLabel("Running...") ;
        else
            button.setLabel("Click to play.") ;
    }
}
