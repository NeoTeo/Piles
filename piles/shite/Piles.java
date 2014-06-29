package piles.shite;

import java.applet.* ;
import java.awt.* ;
import java.awt.event.* ;
import java.net.* ;
import java.awt.image.* ;
import java.util.* ;

public class Piles extends Frame
{

  public static final int F_NOFILTER      = 0 ;
  public static final int F_TRANSPARENT   = 1 ;
  public static final int F_HALFALPHA     = 2 ;

  public static final int BOARD_COLS      = 30 ;
  public static final int BOARD_ROWS      = 16 ;

  public static final int SCR_WID         = 630 ;
  public static final int SCR_DEP         = 480 ;

  public static final int DISPLAY_WID     = 800 ;
  public static final int DISPLAY_DEP     = 600 ;


  public static final int BRICK_WID = SCR_WID / BOARD_COLS ;          // should be 21
  public static final int BRICK_DEP = SCR_DEP / BOARD_ROWS ;          // should be 30


  public static final int B_FREE = -1 ;
  public static final int MAX_BRICKS = 144 ;
  public static final int BRICK_TYPES = MAX_BRICKS / 4 ;
  public static Random rand = new Random() ;
  public static final int current_background = 0 ;

  private int scr_x_off = 50 ;
  private int scr_y_off = 50 ;

  // each brick spans two elements on each side
  public static final int BOARD_LVLS = 5 ;

  public int placed[] = new int[BRICK_TYPES] ;

  public int drawn[][] = new int[BOARD_ROWS][BOARD_COLS] ;

  public int board[][][] = new int[BOARD_ROWS][BOARD_COLS][BOARD_LVLS] ;// lookup for the bricks

  public int pattern[][] =
  {
    {0,0, 0,0, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 0,0},
    {0,0, 0,0, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 0,0},

    {0,0, 0,0, 0,0, 0,0, 1,1, 2,2, 2,2, 2,2, 2,2, 2,2, 2,2, 1,1, 0,0, 0,0, 0,0},
    {0,0, 0,0, 0,0, 0,0, 1,1, 2,2, 2,2, 2,2, 2,2, 2,2, 2,2, 1,1, 0,0, 0,0, 0,0},

    {0,0, 0,0, 0,0, 1,1, 1,1, 2,2, 3,3, 3,3, 3,3, 3,3, 2,2, 1,1, 1,1, 0,0, 0,0},
    {0,0, 0,0, 0,0, 1,1, 1,1, 2,2, 3,3, 3,3, 3,3, 3,3, 2,2, 1,1, 1,1, 0,0, 0,0},

    {0,0, 0,0, 1,1, 1,1, 1,1, 2,2, 3,3, 4,4, 4,4, 3,3, 2,2, 1,1, 1,1, 1,1, 0,0},
    {1,1, 1,1, 1,1, 1,1, 1,1, 2,2, 3,3, 4,5, 5,4, 3,3, 2,2, 1,1, 1,1, 1,1, 1,1},

    {1,1, 1,1, 1,1, 1,1, 1,1, 2,2, 3,3, 4,5, 5,4, 3,3, 2,2, 1,1, 1,1, 1,1, 1,1},
    {0,0, 0,0, 1,1, 1,1, 1,1, 2,2, 3,3, 4,4, 4,4, 3,3, 2,2, 1,1, 1,1, 1,1, 0,0},

    {0,0, 0,0, 0,0, 1,1, 1,1, 2,2, 3,3, 3,3, 3,3, 3,3, 2,2, 1,1, 1,1, 0,0, 0,0},
    {0,0, 0,0, 0,0, 1,1, 1,1, 2,2, 3,3, 3,3, 3,3, 3,3, 2,2, 1,1, 1,1, 0,0, 0,0},

    {0,0, 0,0, 0,0, 0,0, 1,1, 2,2, 2,2, 2,2, 2,2, 2,2, 2,2, 1,1, 0,0, 0,0, 0,0},
    {0,0, 0,0, 0,0, 0,0, 1,1, 2,2, 2,2, 2,2, 2,2, 2,2, 2,2, 1,1, 0,0, 0,0, 0,0},

    {0,0, 0,0, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 0,0},
    {0,0, 0,0, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 1,1, 0,0}


  };            // holds the pattern of the level


  public int last_x, last_y;                             // holds the mouse click coords
  public int sel_r, sel_c, sel_l;
  private int p_xoff ;
  private int p_yoff ;

  public int bricklist[] = new int[MAX_BRICKS] ;
  public Image brick_imgs[] = new Image[BRICK_TYPES] ;
  public Image images[] = new Image[1] ;
  public Image sel_img ;
  public Image hint_img ;
  public Image background[] = new Image[2] ;
  public Image brick[] = new Image[5] ;
  public Image off_image ;
  public Graphics off_graphic,bg_graphic ;

  public int selected = -1 , hint1 = -1, hint2 = -1 ;                                  // the selected tile
  public boolean game_on = true ;
  public int no_bricks = MAX_BRICKS ;

  public Graphics g ;

  public Button button ;
  public Label label ;
  final private StartButton parent ;

  public Piles(StartButton pparent)
  {
    this.parent = pparent ;
    System.out.println("Welcome to piles.") ;
    this.show() ;
    this.setLayout(null) ;

    this.setBackground(Color.blue) ;
    this.setSize(DISPLAY_WID,DISPLAY_DEP) ;
    this.setResizable(false) ;
    Insets ins = this.getInsets() ;

    p_xoff = ins.right+((DISPLAY_WID - 705) / 2) ;
    p_yoff = ins.top+((DISPLAY_DEP - 520) / 2) ;


    Button b = new Button("Hint") ;

    b.addMouseListener(
      new MouseAdapter()
      {
        public void mousePressed(MouseEvent e)
        {
          System.out.println("Hint") ;
          if(next_move(true))
            System.out.println("Bingo") ;
          else
          {
            System.out.println("Bongo") ;
          }
          repaint() ;
        }
      }
    ) ;

    this.addKeyListener(
      new KeyAdapter()
      {
        public void keyTyped(KeyEvent e)
        {
          System.out.println("key") ;
          no_more_moves() ;
        }
      }
    ) ;
    // inner class definition to catch the mouse click
    this.addMouseListener(
      new MouseAdapter()
      {
        public void mousePressed(MouseEvent e)
        {
          System.out.println("mouse") ;
          if(game_on)
          {
            last_x = e.getX() ;
            last_y = e.getY() ;
            is_at(last_x-scr_x_off,last_y-scr_y_off) ;
            repaint() ;
          }
        }
      }
    ) ;

    this.addWindowListener(
      new WindowAdapter()
      {
        public void windowClosing(WindowEvent e)
        {
          System.out.println("Closing") ;
          parent.running = false ;
          parent.repaint() ;
          dispose() ;

        }
      }
    ) ;

    this.add(b) ;
    this.show() ;
    // place the button at the desired coordinates
    b.setBounds(730,30,50,20) ;
    this.requestFocus() ;

    // load the background image
    if((background = load_images("piles/images/background","jpg",1,F_NOFILTER)) == null)
      quit() ;

    // 705 = 47*15 (actual graphic size of tile times no of tiles)
    // 520 = 65*8
    off_image = this.createImage(800,600) ;
    off_graphic = off_image.getGraphics() ;

    images = load_images("piles/images/images","gif",1,F_NOFILTER) ;

    build_brick_images(images[0]) ;



    // initialize the board to -1
    for(int l=0;l<BOARD_LVLS;l++)
      for(int r=0;r<BOARD_ROWS;r++)
      for(int c=0;c<BOARD_COLS;c++)
      board[r][c][l] = -1 ;

      no_bricks = generate_bricks() ;
      assign_tiles() ;
    repaint() ;
  }


  public void build_brick_images(Image images)
  {
    MediaTracker tracker = new MediaTracker(this) ;
    CropImageFilter crop ;
    TransFilter f = new TransFilter() ;
    ImageProducer crop_prod, tran_prod ;
    int xp,yp ;
    // get the selector from sel_img
    crop = new CropImageFilter(0,0,42,65) ;
    crop_prod = new FilteredImageSource(images.getSource(),crop) ;
    sel_img = parent.createImage(crop_prod) ;
    // now make it transparent
    tran_prod = new FilteredImageSource(sel_img.getSource(),f) ;
    sel_img = parent.createImage(tran_prod) ;
    // make sure the image is loaded before continuing
    tracker.addImage(sel_img,1) ;
    try
    {
      tracker.waitForID(1) ;
    } catch(InterruptedException e){}

    // get the five blank brick images & make them transparent
    xp = 46 ;
    for(int b_no=0;b_no<5;b_no++)
    {
      crop = new CropImageFilter(xp,0,47,65) ;
      crop_prod = new FilteredImageSource(images.getSource(),crop) ;
      brick[b_no] = parent.createImage(crop_prod) ;
      tran_prod = new FilteredImageSource(brick[b_no].getSource(),f) ;
      brick[b_no] = parent.createImage(tran_prod) ;
      tracker.addImage(brick[b_no],b_no) ;
      try
      {
        tracker.waitForID(b_no) ;
      } catch(InterruptedException e){}
      xp += 47 ;
    }

    // get the hint image
    crop = new CropImageFilter(xp,0,42,65) ;
    crop_prod = new FilteredImageSource(images.getSource(),crop) ;
    hint_img = parent.createImage(crop_prod) ;
    // now make it transparent
    tran_prod = new FilteredImageSource(hint_img.getSource(),f) ;
    hint_img = parent.createImage(tran_prod) ;
    // make sure the image is loaded before continuing
    tracker.addImage(hint_img,1) ;
    try
    {
      tracker.waitForID(1) ;
    } catch(InterruptedException e){}

    xp = 0;
    yp = 65 ;
    // load the pictures to go on the bricks
    for(int b=0;b<BRICK_TYPES;b++)
    {
      // crop the image from the main one
      crop = new CropImageFilter(xp,yp,47,65) ;
      crop_prod = new FilteredImageSource(images.getSource(),crop) ;
      brick_imgs[b] = parent.createImage(crop_prod) ;
      tran_prod = new FilteredImageSource(brick_imgs[b].getSource(),f) ;
      brick_imgs[b] = parent.createImage(tran_prod) ;
      tracker.addImage(brick_imgs[b],b) ;
      try
      {
        tracker.waitForID(b) ;
      } catch(InterruptedException e){}

      if((xp += 47) >= 470)
      {
        xp = 0 ;
        yp += 65 ;
      }
      if(yp >= 325)
        return ;
    }
  }


  public int generate_bricks()
  {
    int brick_idx = 0 ;
    for(int level=0;level<BOARD_LVLS;level++)
      for(int rows=0;rows<BOARD_ROWS-1;rows++)
      for(int cols=0;cols<BOARD_COLS-1;cols++)
    {  // place a brick if there is one to be placed and there isn't already one there
      if((pattern[rows][cols] > level) && (board[rows][cols][level] < 0))
      {
        // generate an index for the four slots the brick covers
        // eg row,col and row+1,col and row,col+1 and row+1,col+1
        board[rows][cols][level] = brick_idx ;
        board[rows+1][cols][level] = brick_idx ;
        board[rows+1][cols+1][level] = brick_idx ;
        board[rows][cols+1][level] = brick_idx ;

        brick_idx += 1 ;

      }
    }
    return brick_idx ;
  }

  public void assign_tiles()
  {
    int val ;
    for(int brik=0;brik<no_bricks;brik++)
    {
      int i = generate_index() ;
      bricklist[brik] = i ;//generate_index() ;
    }
  }

  public int generate_index()
  {
    int no,count=0 ;
    // get a valid random no
    do
    {
      if((count += 1) > MAX_BRICKS*4)
        return -1 ;

      no = Math.abs(rand.nextInt()) % BRICK_TYPES ;

    } while((placed[no] += 1) > 4) ;


    return no ;

  }

  public void paint(Graphics g)
  {
    //System.out.println("Paint") ;
    // draws the board onto the off_image
    display_board() ;
    g.drawImage(off_image,0,0,null) ;

    g.drawString("Bricks left : "+no_bricks,5,400) ;
    if(!game_on)
    {
      g.setColor(Color.red) ;
      g.drawString("No more moves left ! ",200,200) ; //SCR_WID/2-10,SCR_DEP/2-299) ;
    }
  }

  public void update(Graphics g)
  {
    paint(g) ;
  }

  public boolean next_move(boolean show_move)
  {
    int tar_idx ;
    for(int l1=BOARD_LVLS-1;l1>=0;l1--)
    {
      for(int r1=0;r1<BOARD_ROWS;r1++)
        for(int c1=0;c1<BOARD_COLS;c1++)
        if(board[r1][c1][l1] != -1)
      {
        if(is_free(c1,r1,l1))
        {
          // get the type of the free tile
          int src_type = bricklist[board[r1][c1][l1]] ;
          // find another free one
          for(int l2=BOARD_LVLS-1;l2>=0;l2--)
          {
            for(int r2=0;r2<BOARD_ROWS;r2++)
              for(int c2=0;c2<BOARD_COLS;c2++)
              if((tar_idx = board[r2][c2][l2]) != -1)
            {
              int tar_type = bricklist[tar_idx] ;
              if(is_free(c2,r2,l2) && (src_type == tar_type) && (board[r2][c2][l2] != board[r1][c1][l1])) //(c2 == c1) && (r2 == r1) && (l2 == l1)) )
              {
                if(show_move)
                {
                  //System.out.println("first is : "+c1+", "+r1+", "+l1) ;
                  //System.out.println("second is : "+c2+", "+r2+", "+l2) ;
                  if(hint1 == -1)
                    hint1 = board[r1][c1][l1] ;
                  else
                    hint1 = -1 ;
                  if(hint2 == -1)
                    hint2 = board[r2][c2][l2] ;
                  else
                    hint2 = -1 ;
                }
                return true;
              }
            }
          }
        }
      }
    }
    return false ;
  }


  public void display_board()
  {
    //System.out.println("Display Board") ;
    int idx ;
    int lev_off = 0 ;

    off_graphic.drawImage(background[current_background],0,0,null) ;

    for(int l=0;l<BOARD_LVLS;l++)
    {
      for(int c=0;c<BOARD_COLS-1;c++)
        for(int r=0;r<BOARD_ROWS-1;r++)
      {
        int xpos = c*BRICK_WID+scr_x_off ;
        int ypos = r*BRICK_DEP+scr_y_off ;
        if(drawn[r][c] == 0)
        {
          if((idx = board[r][c][l]) != -1)
          {
            off_graphic.drawImage(brick[l],xpos+lev_off,ypos+lev_off,null)  ;
            off_graphic.drawImage(brick_imgs[bricklist[idx]],xpos+lev_off,ypos+lev_off,null) ;
            drawn[r][c] = 1 ;
            drawn[r+1][c] = 1 ;
            drawn[r][c+1] = 1 ;
            drawn[r+1][c+1] = 1 ;

            if(idx == selected)
              off_graphic.drawImage(sel_img,xpos+lev_off,ypos+lev_off,null) ;

            // display hints if they are set
            if((idx == hint1) || (idx == hint2))
              off_graphic.drawImage(hint_img,xpos+lev_off,ypos+lev_off,null) ;
          }

        }
      }

      lev_off -= 5 ;
      // reset the drawn array
      for(int r=0;r<BOARD_ROWS;r++)
        for(int c=0;c<BOARD_COLS;c++)
        drawn[r][c] = 0 ;
    }
  }

  // remember that calling getImage doesn't actually load it until you actually draw it
  public Image[] load_images(String basename,String ext,int no_of_images,int filter)
  {
    MediaTracker tracker = new MediaTracker(this) ;
    Image tempImg ;
    Image[] faces ;
    ImageFilter f = null ;
    faces = new Image[no_of_images] ;

    switch(filter)
    {
      case F_TRANSPARENT :
      f = new TransFilter() ;
      break ;
      case F_HALFALPHA :
      f = new HalfAlphaFilter() ;
      break ;
    }

    for(int i = 0;i<no_of_images;i++)
    {
      tempImg = parent.getImage(parent.getCodeBase(),basename+i+"."+ext) ;
      tracker.addImage(tempImg,i) ;
      System.out.println("attempting to load: "+parent.getCodeBase()+basename+i+"."+ext) ;
      parent.showStatus("loading image "+i) ;

      try
      {
        tracker.waitForID(i) ;
      } catch(InterruptedException e){}

      if(tracker.isErrorID(i))
      {
        parent.showStatus("Error loading image "+i+"; quitting.") ;
        System.out.println("failed Loading : "+parent.getCodeBase()+basename+i+"."+ext) ;
        return null ;
      }

      if(filter != F_NOFILTER)
      {
        ImageProducer producer = new FilteredImageSource(tempImg.getSource(),f) ;
        faces[i] = parent.createImage(producer) ;
      }
      else
        faces[i] = tempImg ;
    }
    parent.showStatus("Running.") ;
    return faces ;
  }

  public void is_at(int x,int y)
  {
    int idx ;
    for(int l=BOARD_LVLS-1;l>=0;l--)
    {
      int xoff = l * 5 ;
      int yoff = l * 5 ;
      int col = (x+xoff) / BRICK_WID ;
      int row = (y+yoff) / BRICK_DEP ;

      if((col > -1) && (col < BOARD_COLS))
        if((row > -1) && (row < BOARD_ROWS))
      {
        if((idx = board[row][col][l]) != -1)
        {
          System.out.println("found index : "+idx+" at level "+l) ;
          if(is_free(col,row,l))
          {
            System.out.println("It is free.") ;

            if(selected == -1)
            {
              System.out.println("Selected.") ;
              selected = idx ;
            }
            else
              if(selected == idx)
            {
              System.out.println("Deselected.") ;
              selected = -1 ;
            }
            else
            {
              System.out.println("bricklist[selected] =  "+bricklist[selected]+" and bricklist[idx] "+bricklist[idx]) ;
              if((selected != idx) && (bricklist[selected] == bricklist[idx]))
              {
                System.out.println("Match accepted.") ;
                remove(selected) ;
                remove(idx) ;
                selected = -1 ;
                no_bricks -= 2 ;
                hint1=hint2=-1 ;
                // if there are no more moves let the player know
                if(!next_move(false))
                  no_more_moves() ;
              }
            }

          }
          else
            System.out.println("It is blocked.") ;

          return ;
        }
      }
    }
  }


  public void remove(int idx)
  {
    for(int l=0;l<BOARD_LVLS;l++)
      for(int r=0;r<BOARD_ROWS;r++)
      for(int c=0;c<BOARD_COLS;c++)
      if(board[r][c][l] == idx)
      board[r][c][l] = -1 ;
      }


  public boolean is_free(int col,int row,int level)
  {
    int idx ;
    // look from top level and down.
    //for(int level=BOARD_LVLS-1;level>=0;level--)
    {
      if((idx = board[row][col][level]) != -1)
      {
        // we've found a brick

        // first find out if it is blocked at front or back
        if((!front_blocked(col,row,level,idx)) && (!back_blocked(col,row,level,idx)))
          return false ;

        // to find out whether it is covered from above search a 9 slot radius
        // around the current slot
        for(int r=row-1;r<=row+1;r++)
          for(int c=col-1;c<=col+1;c++)
          // make sure we are within bounds
          if((r >= 0) && (r < BOARD_ROWS))
          if((c >= 0) && (c < BOARD_COLS))
          if(board[r][c][level] == idx)
          // check if it covered a level above
          // first make sure we're within bounds
          if(level+1 < BOARD_LVLS)
          if(board[r][c][level+1] != -1)
          return false ;


          return true ;

          }
    }
    return false ;
  }

  public boolean front_blocked(int col, int row, int level, int idx)
  {
    //System.out.println("front blocked called with "+col+", "+row+", "+level+", "+idx) ;
    if(col-2 >= 0)
    {
      if(board[row][col-2][level] != -1)
        return false ;

      if(row-1 >= 0)
        if(board[row-1][col][level] == idx)
        if(board[row-1][col-2][level] != -1)
        return false ;

        if(row+1 < BOARD_ROWS)
        if(board[row+1][col][level] == idx)
        if(board[row+1][col-2][level] != -1)
        return false ;
        }
    //System.out.println("front_blocked returning true") ;
    return true ;
  }

  public boolean back_blocked(int col, int row, int level, int idx)
  {
    if(col+2 < BOARD_COLS)
    {
      if(board[row][col+2][level] != -1)
        return false ;

      if(row-1 >= 0)
        if(board[row-1][col][level] == idx)
        if(board[row-1][col+2][level] != -1)
        return false ;

        if(row+1 < BOARD_ROWS)
        if(board[row+1][col][level] == idx)
        if(board[row+1][col+2][level] != -1)
        return false ;

        }
    //System.out.println("back_blocked returning true") ;
    return true ;
  }

  public void no_more_moves()
  {
    // display a dialog or something saying that there are no more move and would
    // you like to : quit or try again

    int xpos = 150 ;
    int ypos = 150 ;
    QuitRetryDlg dlog = new QuitRetryDlg(this,"Teohaj",this) ;
    dlog.setVisible(true);
    System.out.println("I don't wait for nobody.") ;
  }

  public void restart()
  {
    // initialize the board to -1
    for(int l=0;l<BOARD_LVLS;l++)
      for(int r=0;r<BOARD_ROWS;r++)
      for(int c=0;c<BOARD_COLS;c++)
      board[r][c][l] = -1 ;

      for(int p=0;p<BRICK_TYPES;p++)
      placed[p] = 0 ;

      no_bricks = generate_bricks() ;
      assign_tiles() ;
    repaint() ;
  }

  public void quit()
  {
    //System.out.println("quit") ;
    parent.running = false ;
    parent.repaint() ;
    dispose() ;
  }
  public void setup_gbc(GridBagConstraints gbc,int gx,int gy,int gw,int gh,int wx,int wy,int fl,int an)
  {
    gbc.gridx = gx ;
    gbc.gridy = gy ;
    gbc.gridwidth = gw ;
    gbc.gridheight = gh ;
    gbc.weightx = wx ;
    gbc.weighty = wy ;
    gbc.fill = fl ;
    gbc.anchor = an ;

  }

}

