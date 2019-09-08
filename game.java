import java.awt.*;
import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.*;
import javax.swing.*;
public class game extends JFrame implements Runnable {
    int recty=50;
    String msg="Press Enter";
    String hs="";
    String hsm="";
    int high;
    int y=75;
    int x=90;
    int sc=0;
    int up=3;
    int gm=0;
    int flag=0;
    int spc=5;
    int a=450;
    int tsleep=50;
    int p=1;
    Thread t = new Thread(this);;
    public static void main(String[] args) {
        new game();
    }
    public void run() {
        int a=450;  
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY); 
        while (true){
                if (sc>0) {    //speed increment    
                    if (sc%spc==0) {
                        if (flag==0) {
                            if(tsleep>4){
                                tsleep-=2;
                            }    
                            flag=1;
                            if(sc%(spc*7)==0){
                                if(up<0)
                                    up+=-1;
                                else
                                    up+=1;
                            }
                        }     
                    }
                }
                if (x+20>450) {
                    x=430;
                }else if (x-20<0) {
                    x=20;
                }
                if(y+20>=350){
                    up*=-1;//up of ball
                }else if(y-1<=50){
                    up*=-1;//down of ball
                }
                y+=up;
                if(x+20<a){//ball movement to right
                    x+=10;
                }
                else if(x+20>=450){//checking ball touch right end 
                    if(recty-65<y && recty+55>y){ 
                        x-=10;
                        a=10;
                        sc++;
                        if (sc%spc==0) {
                            flag=0;
                        }
                    }else{
                        //game over
                        gm=1;
                        up=0;
                        msg="Game over";
                        this.highscore();
                        repaint();
                        t.suspend();
                    }
                }else if(x-20<=0){//checking ball touch left end
                    x=20;
                    if(recty-65<y && recty+55>y){  
                        x+=10;
                        sc++;
                        if (sc%spc==0) {
                            flag=0;
                        }
                        a=450;
                    }else{
                        //game over
                        gm=1;
                        up=0;
                        msg="Game over";
                        this.highscore();
                        repaint();
                         t.suspend();
                    }
                }else
                    x-=10;//backward move of ball
                    if (gm==1) {
                        x=217;
                        y=190;
                    }
            repaint();
            try {
                Thread.sleep(tsleep);
            }
            catch (InterruptedException e) {
                //Do Nothing
            }
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY); 
        }
         
    }

public game() {
    super("Stick and Ball");                          
    setSize(480, 400); 
    addKeyListener(new skeylistener());
    addMouseMotionListener(new mymouselistener());
    setLayout(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); 
}

public void paint(Graphics g) {
    super.paint(g);
    g.setColor(Color.green);
        g.fillRect(10,50,450,300); //green ground
        g.setColor(Color.orange);
        g.fillOval(150,125,155,150);//orange circle 
        g.setColor(Color.white);
        g.fillOval(x,y,20,20);//ball
        g.drawRect(20,50,430,300);//ground layout
        g.drawLine(225,50,225,350);//center vertical line
        g.drawOval(150,125,155,150);//round in center
        g.setColor(Color.black); 
        g.fillRect(10,recty-50,10,100);//left bat
        g.fillRect(451,recty-50,10,100);//right bat
        g.setColor(Color.red);
        Font myFont = new Font ("Courier New", 1, 50);
        g.setFont (myFont);
        g.drawString(msg,100,190);//game over!!!  
       
        if (gm==1) {
            g.drawString("Score:"+sc,120,250);//score
            myFont = new Font ("Courier New", 1, 20);
            g.setFont(myFont);
            g.drawString("press any key to restart",100,300);
            g.drawString(""+hsm,50,50);
            g.drawString(""+hsm,350,50);
            g.drawString("Highscore:"+high,300,360);//score 
        }else{
            myFont = new Font ("Courier New", 1, 20);
            g.setFont (myFont);
            g.drawString("Score:"+sc,300,360);//score 
             g.drawString("move your mouse to move sticks",50,385);
        }
   
}
     public void highscore(){
        try{     
           File file=new File("hs.txt");
           if(file.createNewFile())
           {
               FileWriter writ=new FileWriter(file);
                writ.write(""+sc);
                writ.close();
               high=sc;
           }else{
               FileReader fr=new FileReader(file);
               int i;
               while((i=fr.read())!=-1)
                   hs+=((char)i);
               fr.close();
               high=Integer.parseInt(hs);
               if(high<sc){
                    FileWriter writ=new FileWriter(file);
                    writ.write(""+sc);
                    writ.close();
                    high=sc;
                    hsm="new best";
               }
           }
           
       }catch(Exception e){
            System.out.println(""+e.toString());
       }
    }
 public class mymouselistener extends MouseAdapter
 {
  public void mouseMoved(MouseEvent e)
  {//bat movement
      if(p==1){//checking game paused or not
  if (e.getY()-50<60) {
      recty=100;
  }else if (e.getY()+50>350) {
      recty=300;
  }
  else //if game paused
   recty = e.getY();
  repaint();
  }}
 }
    public class skeylistener extends KeyAdapter{
    public void keyPressed(KeyEvent e)
    {
        
        if(gm==1){//restarting the game
            up=3;
            x=200;
            y=90;
            tsleep=50;
            gm=0;
            sc=0;
            msg="";
            hs="";
            t.resume();
        }else if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
       {//game pause and resume by escape key
           if(p==1){
               t.suspend();
               p=0;
               msg="Game Paused";
           }else{
                t.resume();
               p=1;
               msg="";
           }
       }
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            //starting of game by pressing ENTER
            t.start();
            msg="";
        }
        repaint();
    }
    }
} 