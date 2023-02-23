import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Game extends JPanel implements Runnable, KeyListener, MouseListener {
    private Grid grid;
    private PlayerStats playerStats;
    private BufferedReader bufferedReader;

    private Image gameOverImg=null;

    private String scoreDisplay;
    private String levelDisplay;
    private String highScoreDisplay;

    private int highScore;
    private int initialHighScore;

    private Color buttonColor;
    private boolean buttonIsPressed;
    private boolean restart;


    Game(){

        playerStats = new PlayerStats();
        grid= new Grid(playerStats);
        Thread thread = new Thread(this);
        thread.start();
        addKeyListener(this);
        addMouseListener(this);

        scoreDisplay="Score: "+ playerStats.score;
        levelDisplay="Level: 1";
        restart=false;


        InputStream is=getClass().getResourceAsStream("highscore.txt");
        InputStreamReader isr= new InputStreamReader(is);
        bufferedReader= new BufferedReader(isr);

        BufferedImage gameOver;
        try {
            gameOver =ImageIO.read(getClass().getResourceAsStream("gameOver.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        buttonColor= Color.GRAY;
        buttonIsPressed=false;

        gameOverImg= gameOver.getScaledInstance(100,50,Image.SCALE_DEFAULT);

        try {
            initialHighScore=Integer.parseInt(bufferedReader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        highScore=initialHighScore;
        highScoreDisplay="High Score: "+highScore;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //Draws grid with blocks
        for(int i=2;i<22;i++)
        {
            for(int j=0; j<10;j++)
            {
                g.setColor(Color.BLACK);
                g.draw3DRect(j*Utils.squareSize, (i-2)*Utils.squareSize,Utils.squareSize,Utils.squareSize,true);
                if(grid.cells[i][j].x==1 ||grid.cells[i][j].x==2  ){
                    g.setColor(Utils.getColor(grid.cells[i][j].y));
                }
                else
                    g.setColor(Color.WHITE);

                g.fill3DRect(j*Utils.squareSize, (i-2)*Utils.squareSize,Utils.squareSize,Utils.squareSize,true);
            }

        }

        //Draws displays
        g.setColor(Color.BLACK);
        g.drawString(scoreDisplay,430,20);
        g.drawString(levelDisplay,430,40);
        g.drawString(highScoreDisplay,430, 60);
        g.drawString("Next Block:", 430,80);

        //Draws next block
        for(int i=0;i<2;i++){
            for(int j=0; j<4;j++){
                g.setColor(Utils.getColor(grid.getNextBlock().blockType));
                if(grid.getNextBlock().squareArrangement[i][j]==1){
                    g.fillRect(j*(Utils.squareSize)+430,i*(Utils.squareSize)+100,Utils.squareSize,Utils.squareSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j*(Utils.squareSize)+430,i*(Utils.squareSize)+100,Utils.squareSize,Utils.squareSize);
                }
            }
        }

        //Draws button
        g.setColor(buttonColor);
        g.fillRoundRect(450,220,80,40,10,10);
        g.setColor(Color.BLACK);
        g.drawRoundRect(450,220,80,40,10,10);
        g.drawString("Restart", 465, 245);


        if(grid.isGameOver()){
            g.drawImage(gameOverImg,150,20,this);
        }
    }

    @Override
    public void run() {
        grid.addBlock();

        while(true){
            repaint();
            grid.moveDown();

            synchronized (this) {
                try {
                    wait(playerStats.speedInMilli);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            if(grid.blockStopped){
                grid.blockTouchedBottom();
                setScores();
            }

            if(restart)
                restart();

            if(grid.isGameOver()){
                endOfGame();
                synchronized (this){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }

    }

    private void endOfGame() {
        repaint();
        if(initialHighScore< playerStats.score){
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("files/highscore.txt"));
                bufferedWriter.write(String.valueOf(playerStats.score));
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private synchronized void restart() {
        playerStats = new PlayerStats();
        scoreDisplay="Score: "+playerStats.score;
        grid= new Grid(playerStats);
        restart=false;
        grid.addBlock();
        notifyAll();

        try {
            bufferedReader= new BufferedReader(new FileReader("highscore.txt"));
            initialHighScore=Integer.parseInt(bufferedReader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        highScore=initialHighScore;
        highScoreDisplay="High Score: "+highScore;

    }
    private void setScores() {
        scoreDisplay="Score: "+ playerStats.score;
        levelDisplay="Level: "+ playerStats.level;
        highScoreDisplay="High Score:"+ highScore;

        if(initialHighScore< playerStats.score){
            highScore= playerStats.score;
        }
    }

    //------------------------EVENTS----------------------------//
    @Override
    public synchronized void keyReleased(KeyEvent e) {
        if(!grid.isGameOver()){
            if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                grid.moveRight();
                repaint();
            }
            else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                grid.moveLeft();
                repaint();
            }
            else if(e.getKeyCode()==KeyEvent.VK_DOWN){
                grid.moveDown();
                repaint();
            }
            else if(e.getKeyCode()==KeyEvent.VK_UP){
                grid.rotateBlock();
                repaint();
            }
            else if(e.getKeyCode()==KeyEvent.VK_SPACE){
                while(!grid.blockStopped){
                    grid.moveDown();
                }
                repaint();
            }

            if(grid.blockStopped){
                grid.blockTouchedBottom();
                scoreDisplay="Score: "+ playerStats.score;
                levelDisplay="Level: "+ playerStats.level;
                if(highScore< playerStats.score){
                    highScoreDisplay="High Score: "+ playerStats.score;
                }
                repaint();
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getX()>=450 && e.getX()<=530 && e.getY()>=220 && e.getY()<=260){
            buttonColor= Color.GRAY.darker();
            buttonIsPressed=true;
            repaint();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getX()>=450 && e.getX()<=530 && e.getY()>=220 && e.getY()<=260){
            buttonColor= Color.GRAY;
            buttonIsPressed=false;
            restart=true;
            if(grid.isGameOver()){
                restart();
            }
        }

    }

    //-----------------------UNUSED--------------------------------//
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
