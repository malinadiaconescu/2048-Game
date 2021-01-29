package wow;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;
 
public class Game2048 extends JPanel {
	 
    enum State {
        start, won, running, over //stari ale jocului
    }
 
    final Color[] colorTable = {
        new Color(0x846B8A),
        new Color(0xC98BB9), //2
        new Color(0xD4AFBC) ,//4
        new Color(0xB06D85),//8
        new Color(0xC98BB9), //16
        new Color(0xA64E90),//32
        new Color(0xD0C5D3), //64
        new Color(0xE7948e), //128
        new Color(0xC2CBFF),//256
        new Color(0xF9B3D5), //512
        new Color(0xA20144), //1024
        new Color(0x9D4CA9)};//2048
 
    final static int target = 2048;//valoarea la care jucatorul trebuie sa ajunga
    static int highest;//cel mai mare scor al jucatorului
    static int score;//scorul curent
    static int highscore;
    private Color gridColor = new Color(0x846B8A);
    private Color emptyColor = new Color(0xFAE3E3);
    private Color startColor = new Color(0xD4AFBC);
    private Random rand = new Random();
    private Tile[][] tiles;//tabela jocului
    private int side = 4;//jocul este pe 4*4
    private State gamestate = State.start;//starea jocului
    private boolean checkingAvailableMoves;//miscari posibile pt a detecta cand jocul a fost pierdut
    private int terminari=0;
    
    public Game2048() { //the welcome page
    	//design for welcome page
    	
    	try
    	{FileInputStream myObj2= new FileInputStream("wow\\tabla.ser");
    		ObjectInputStream in=new ObjectInputStream(myObj2);
    		File myObj=new File("wow\\scoruri.txt");
		Scanner reader=new Scanner(myObj);
    		if(reader.hasNextInt())
    			{if(reader.nextInt()==1)
    		{JoculCurent joc=(JoculCurent)in.readObject();
    		score = joc.score;
             highscore=joc.highscore;
             highest=joc.highest;
             gamestate = State.running;
             tiles=joc.tiles;
    		}}
    		else
    		{
    			score = 0;
    	         highest = 0;
    	         gamestate = State.running;
    	         tiles = new Tile[side][side];
    	        //se adauga doua noi tiles
    	          addRandomTile();
    	          addRandomTile();
    		}
    	}catch(FileNotFoundException e)
    	{
    		System.out.println("An error occurred");
    		e.printStackTrace();
    	}
    	catch(IOException i)
		{
			System.out.println("An error occured");
			i.printStackTrace();
		}catch(ClassNotFoundException c)
			{
			System.out.println("Class not found");
			c.printStackTrace();
			}
    	
        setPreferredSize(new Dimension(900, 700));
        setBackground(new Color(0xFAE3E3));
        setFont(new Font("SansSerif", Font.BOLD, 48));
        setFocusable(true);
        
        addMouseListener(new MouseAdapter() {//cand da click, jocul incepe
            @Override
            public void mousePressed(MouseEvent e) {
                startGame();//jocul incepe
                repaint();
            }
        });
        //in timpul jocului, se pot intampla 4 chestii : jucatorul ori muta in sus, in jos, la stanga sau la dreapta
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: //cazul in sus
                        moveUp();
                        break;
                    case KeyEvent.VK_DOWN: //cazul in jos
                        moveDown();
                        break;
                    case KeyEvent.VK_LEFT: //cazul in stanga
                        moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT: //dreapta
                        moveRight();
                        break;
                    case KeyEvent.VK_Q:
                    	try {
                    	      FileOutputStream myObj2 = new FileOutputStream("wow\\tabla.ser");
                     	     JoculCurent joc=new JoculCurent(highscore,score,highest,tiles);
                    	     // for(int i=0;i<4;i++)
                    	    	//  for(int j=0;j<4;j++)
                    	    		//  writer.write(tiles[i][j]);
                    	      ObjectOutputStream out=new ObjectOutputStream(myObj2);
              					out.writeObject(joc);
              					myObj2.close();
              					File myObj=new File("wow\\scoruri.txt");
              					FileWriter reader=new FileWriter(myObj);
              					reader.write("1");
              					reader.close();
              					System.out.println("AAAAAAAA");
                    	    } catch (IOException e1) {
                    	      System.out.println("An error occurred.");
                    	      e1.printStackTrace();
                    	    }
                    	System.out.println(terminari);
                    	System.exit(0);
                    	break;
                }
                repaint();
            }
        });
    }
 
    
    @Override
    //componenta de colorare
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //se coloreaza tabla
        drawGrid(g);
    }
 
    
    
    
    void startGame() {
    	/*try
    	{FileInputStream myObj2= new FileInputStream("C:\\\\Users\\\\malin\\\\eclipse-workspace\\\\wow\\\\src\\\\wow\\\\tabla.ser");
    		ObjectInputStream in=new ObjectInputStream(myObj2);
    		File myObj=new File("C:\\\\Users\\\\malin\\\\eclipse-workspace\\\\wow\\\\src\\\\wow\\\\scoruri.txt");
		Scanner reader=new Scanner(myObj);
    		if(reader.hasNextInt())
    			{if(reader.nextInt()==1)
    		{JoculCurent joc=(JoculCurent)in.readObject();
    		score = joc.score;
             highscore=joc.highscore;
             highest=joc.highest;
             gamestate = State.running;
             tiles=joc.tiles;
    		}}
    		else
    		{
    			score = 0;
    	         highest = 0;
    	         gamestate = State.running;
    	         tiles = new Tile[side][side];
    	        //se adauga doua noi tiles
    	          addRandomTile();
    	          addRandomTile();
    		}
    	}catch(FileNotFoundException e)
    	{
    		System.out.println("An error occurred");
    		e.printStackTrace();
    	}
    	catch(IOException i)
		{
			System.out.println("An error occured");
			i.printStackTrace();
		}catch(ClassNotFoundException c)
			{
			System.out.println("Class not found");
			c.printStackTrace();
			}*/
    	//functie apelata cand jucatorul da start game
        if (gamestate != State.running) {
            score = 0;
            highest = 0;
            gamestate = State.running;
            tiles = new Tile[side][side];
           //se adauga doua noi tiles
            addRandomTile();
            addRandomTile();
        }
    }
    
    
    
    
 //colorarea tablei de joc
    void drawGrid(Graphics2D g) {
        g.setColor(gridColor);
        g.fillRoundRect(200, 100, 499, 499, 15, 15);//tabla mov de sub
        if (gamestate == State.running) {
        	if(score>highscore) highscore=score;
        	//cum trebuie sa arate tabla in starea running
        	g.fillRoundRect(25, 100, 150, 80, 15, 15);g.fillRoundRect(25, 250, 150, 80, 15, 15);g.setFont(new Font("SansSerif", Font.BOLD, 30));
        	
        	g.drawString("Score: ",25 , 100);
        	g.drawString("Highscore: ", 25, 250);
        	g.setColor(emptyColor);
        	g.drawString(String.valueOf(score), 100, 150);
        	g.drawString(String.valueOf(highscore), 100, 300);
        	g.setColor(gridColor);
        	g.setFont(new Font("SansSerif", Font.BOLD, 50));
            for (int r = 0; r < side; r++) {
                for (int c = 0; c < side; c++) {
                    if (tiles[r][c] == null) {
                        g.setColor(emptyColor);
                        g.fillRoundRect(215 + c * 121, 115 + r * 121, 106, 106, 7, 7);//coloreaza tabla
                    } else {
                        drawTile(g, r, c);//se coloreaza tile urile
                    }
                }
            }
        } else {
        	//cand se termina jocul
            g.setColor(startColor);
            
            g.fillRoundRect(215, 115, 469, 469, 7, 7);
 
            g.setColor(gridColor.darker());
            g.setFont(new Font("SansSerif", Font.BOLD, 128));
            g.drawString("2048", 310, 270);
 
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
 //mesaje pt jucator
            if (gamestate == State.won) {
            	terminari++;
                g.drawString("you made it!", 390, 350);
 
            } else if (gamestate == State.over)
            	{terminari++;
            g.drawString("game over", 400, 350);}
 //reporneste
            g.setColor(gridColor);
            g.drawString("click to start a new game", 330, 470);
            g.drawString("(use arrow keys to move tiles)", 310, 530);
        }
    }
 
    void drawTile(Graphics2D g, int r, int c) {//deseneaza un tile
        int value = tiles[r][c].getValue();
 
        g.setColor(colorTable[(int) (Math.log(value) / Math.log(2)) + 1]);//aflam valoarea corespunzatoare, 2 la ce putere e value - aceea va fi culoarea corespunzatoare in vectorul de culori
        g.fillRoundRect(215 + c * 121, 115 + r * 121, 106, 106, 7, 7);
        String s = String.valueOf(value);
        
        
        
        
        //se coloreaza scrisul
        g.setColor(colorTable[0]);
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int dec = fm.getDescent();
 
        int x = 215 + c * 121 + (106 - fm.stringWidth(s)) / 2;
        int y = 115 + r * 121 + (asc + (106 - (asc + dec)) / 2);
 
        g.drawString(s, x, y);
    }
 
    
    
    
    
    
 //adauga un nou tile
    private void addRandomTile() {
        int pos = rand.nextInt(side * side);//generam un nr random intre 0 si 15 la care vom adauga unu
        int row, col;
        do {
        	//calculam pozitia acestuia
            pos = (pos + 1) % (side * side);
            row = pos / side;
            col = pos % side;
        } while (tiles[row][col] != null);
 
        int val = rand.nextInt(10) == 0 ? 4 : 2;
        tiles[row][col] = new Tile(val);
    }
    
    
    
    
    
 //miscam tiles urile
    private boolean move(int countDownFrom, int yIncr, int xIncr) {
        boolean moved = false;
        if(score>highscore) highscore=score;
 //le luam pe toate pe rand
        for (int i = 0; i < side * side; i++) {
            int j = Math.abs(countDownFrom - i);
 
            int r = j / side;
            int c = j % side;
 
            if (tiles[r][c] == null)//daca n au valoare, nu avem ce misca
                continue;
 
            int nextR = r + yIncr;
            int nextC = c + xIncr;
 
            while (nextR >= 0 && nextR < side && nextC >= 0 && nextC < side) {
 
                Tile next = tiles[nextR][nextC];
                Tile curr = tiles[r][c];
 
                if (next == null) {//daca este liber, doar mutam tile ul
                		//verificam daca jocul s a terminat
                    if (checkingAvailableMoves)
                        return true;
 
                    tiles[nextR][nextC] = curr;
                    tiles[r][c] = null;
                    r = nextR;
                    c = nextC;
                    nextR += yIncr;
                    nextC += xIncr;
                    moved = true;
 
                } else if (next.canMergeWith(curr)) {//in cazul in care avem valori de merge uit
                	//verificam daca jocul s a terminat
                    if (checkingAvailableMoves)
                        return true;
                    int value = next.mergeWith(curr);
                    if (value > highest)
                        highest = value;
                    score += value;
                    tiles[r][c] = null;
                    moved = true;
                    break;
                } else
                    break;
            }
        }
        //dupa ce s a mutat, recalculam scorurile
        if (moved) {
            if (highest < target) {
                clearMerged();
                addRandomTile();
                if (!movesAvailable()) {
                    gamestate = State.over;
                }
            } else if (highest == target)
                gamestate = State.won;
        }
 
        return moved;
    }
 //miscarile posibile
    boolean moveUp() {
        return move(0, -1, 0);
    }
 
    boolean moveDown() {
        return move(side * side - 1, 1, 0);
    }
 
    boolean moveLeft() {
        return move(0, 0, -1);
    }
 
    boolean moveRight() {
        return move(side * side - 1, 0, 1);
    }
 
    void clearMerged() {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                if (tile != null)
                    tile.setMerged(false);
    }
 //functia care ne cauta daca jocul mai are miscari posibile sau nu
    boolean movesAvailable() {
        checkingAvailableMoves = true;
        //ia toate optiunile in considerare - daca am muta in oricare dintre cele 4 
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingAvailableMoves = false;
        return hasMoves;
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("2048");
            f.setResizable(true);
            f.add(new Game2048(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}