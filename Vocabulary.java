/*
 Danny Amirtharaj
 5/2/2021
 Vocabulary.java
*/


import javax.swing.JFrame;	
import javax.swing.JPanel;
import javax.swing.BorderFactory;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Image;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.imageio.ImageIO;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;

import java.util.Random;

public class Vocabulary
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("");
		frame.setLayout(new BorderLayout());
		GamePanel panel = new GamePanel();
		frame.add(panel);
		frame.setSize(1216, 800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class GamePanel extends JPanel
{
	private String[] spanishWord; // words to translate
	private String[] englishWord;
	private int numLines;
	private CardLayout listOfCards; // cardlayout
	private Scanner input;
	private int[] allScores;
	private int numHighScores;
	
	public GamePanel()
	{
		input = null;
		
		spanishWord = new String[15];
		englishWord = new String[15];
		getWords();
		
		
		setBackground(Color.DARK_GRAY);
		
		listOfCards = new CardLayout();
		setLayout(listOfCards);
		
		HomePage hp = new HomePage(listOfCards, this, spanishWord, englishWord, numLines);
		this.add(hp, "1");
	}
	
	
	
	private void getWords()
	{
		String fileName = new String("vocab.txt");
		File file = new File(fileName);
		try
		{
			input = new Scanner(file);
		}
		catch(FileNotFoundException e)
		{
			System.err.println("file not found");
		}
		String line = "";
		while(input.hasNext())
		{
			line = input.nextLine();
			spanishWord[numLines] = line.substring(0, line.indexOf(" "));
			englishWord[numLines] = line.substring(line.indexOf(" ") + 1, line.length());
			numLines++;
		}
	}
}

class HomePage extends JPanel
{
	private CardLayout listOfCards; // cardlayout
	private GamePanel gp; // gamepanel instance
	private JLabel welcome; // welcome label
	private JTextArea textArea; // text area for instructions
	private JScrollPane scrollPane; // scroll pane for instructions
	private JRadioButton easy; // button for easy difficulty
	private JRadioButton medium; // button for medium difficulty
	private JLabel start; // label to start
	private JRadioButton hard; // button for hard difficulty
	private ButtonGroup bg; // buttongroup
	private String[] spanishWord; // words to translate
	private String[] englishWord; // correct answer in english
	private int numLives; // number of players lives
	private HomePageHandler hph; // handler class for homepage
	private int numLines;
	private int score;
	private int[] randOrder;
	private int count;
	private JButton instructionImages;
	private ImagesOfGame iog;
	private int highScore;
	private Scanner input;
	private JLabel highScoreLabel;
	int[] allScores;
	private int numHighScores;
	
	
	public HomePage(CardLayout c, GamePanel g, String[] s, String[] e, int n)
	{
		
		count = 0;
		
		numLives = 0;
		score = 30;
		hph = new HomePageHandler();
		
		gp = g;
		listOfCards = c;
		spanishWord = s;
		englishWord = e;
		numLines = n;
		count = 0;
		
		randOrder = new int[numLines];
		randOrder = getRandomizedNumbers();
		
		setBackground(Color.CYAN);
		setLayout(null);
		
		getHighScore();
		findHighScore();
		
		// make welcome label
		welcome = new JLabel("Welcome to El Juego de Vocabulario!");
		welcome.setFont(new Font("Serif", Font.BOLD, 50));
		welcome.setBounds(200, 50, 900, 100);
		add(welcome);
		
		// System.out.println(highScore);
		
		highScoreLabel = new JLabel("The high score is " + allScores[0] + "!");
		highScoreLabel.setBounds(450, 400, 300, 400);
		highScoreLabel.setFont(new Font("Serif", Font.BOLD, 25));
		add(highScoreLabel);
		
		// make text area in scroll pane
		textArea = new JTextArea("instructions:\nchoose a dificulty and press ok\nto start the game. There will\nbe obstacles all across the screen.\nThe goal is to get to the other\nside and choose the correct\ntranslation of the word. If you\nget it incorrect, you will have\nto retype the word and you will\nlose a live. You get 3 lives\nuntil you are dead.", 20, 20);
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(50, 300, 200, 200);
		add(scrollPane);
		
		instructionImages = new JButton("Images of Game");
		instructionImages.setBounds(50, 500, 200, 50);
		instructionImages.addActionListener(hph);
		add(instructionImages);
		
		
		// make label for radio buttons
		start = new JLabel("Choose dificulty");
		start.setFont(new Font("Serif", Font.BOLD, 20));
		start.setBounds(925, 150, 200, 100);
		add(start);
		
		// make radio buttons
		bg = new ButtonGroup();
		easy = new JRadioButton("easy");
		medium = new JRadioButton("medium");
		hard = new JRadioButton("hard");
		easy.setBounds(900, 250, 200, 100);
		medium.setBounds(900, 350, 200, 100);
		hard.setBounds(900, 450, 200, 100);
		easy.addActionListener(hph);
		medium.addActionListener(hph);
		hard.addActionListener(hph);
		bg.add(easy);
		bg.add(medium);
		bg.add(hard);
		add(easy);
		add(medium);
		add(hard);
		
	}
	
	private void findHighScore()
	{
		int temp = 0;
		
		for(int i = 0; i < numHighScores; i++)
		{
			for(int j = i + 1; j < numHighScores; j++)
			{
				if(allScores[i] < allScores[j])
				{
					temp = allScores[i];
					allScores[i] = allScores[j];
					allScores[j] = temp;
				}
			}
		}
	}
	
	private void getHighScore()
	{
		
		int line = 0;
		String fileName = new String("highscore.txt");
		File file = new File(fileName);
		try
		{
			input = new Scanner(file);
		}
		catch(FileNotFoundException e)
		{
			System.err.println("file not found");
		}
		
		
		while(input.hasNext())
		{
			numHighScores++;
			input.nextLine();
		}
		
		allScores = new int[numHighScores];
		
		try
		{
			input = new Scanner(file);
		}
		catch(FileNotFoundException e)
		{
			System.err.println("file not found");
		}
		
		int i = 0;
		while(input.hasNext())
		{
			line = input.nextInt();
			allScores[i] = line;
			i++;
			//System.out.println(allScores[i]);
		}
		// return allScores;
	}
	
	private int[] getRandomizedNumbers()
	{
		int[] array = new int[numLines];
		
		for(int k = 0; k < numLines; k++)
		{
			array[k] = k;
		}
		
		Random rand = new Random();
		
		for(int i = 0; i < numLines; i++)
		{
			int index = rand.nextInt(numLines);
			int temp = array[index];
			array[index] = array[i];
			array[i] = temp;
		}
		
		return array;
	}
	
	
	private class HomePageHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			String command = evt.getActionCommand();
			if(command != "Images of Game")
			{
				int ok = 1;
				ok = JOptionPane.showConfirmDialog(null, "press yes to continue", "Continue?", JOptionPane.YES_NO_OPTION);
				if(ok != 1)
				{
					GameScreen gs = new GameScreen(listOfCards, gp, numLives, command, spanishWord, englishWord, numLines, score, randOrder, count, allScores);
					gp.add(gs, "3");
					listOfCards.next(gp);
				}
				else
				{
					bg.clearSelection();
				}
			}
			if(command == "Images of Game")
			{
				ImagesOfGame iog = new ImagesOfGame(listOfCards, gp, spanishWord, englishWord, numLines, allScores);
				gp.add(iog, "2");
				listOfCards.next(gp);
			}
				
			
		}
	}
}

class ImagesOfGame extends JPanel implements ActionListener
{
	private String firstPictName;
	private String secondPictName;
	private Image firstImage;
	private Image secondImage;
	private CardLayout listOfCards;
	private JLabel firstPicInstructions;
	private JLabel secondPicInstructions;
	private JLabel space;
	private JButton back;
	private GamePanel gp;
	private String[] spanishWord;
	private String[] englishWord;
	private int numLines;
	private int[] allScores;
	
	public ImagesOfGame(CardLayout c, GamePanel g, String[] s, String[] e, int n, int[] as)
	{
		
		listOfCards = c;
		gp = g;
		spanishWord = s;
		englishWord = e;
		numLines = n;
		allScores = as;
		
		setLayout(null);
		setBackground(Color.CYAN);
		
		space = new JLabel(" ");
		space.setFont(new Font("Serif", Font.BOLD, 400));
		add(space);
		
		firstPicInstructions = new JLabel("This is the game screen. The goal is to get the player icon to the matching word");
		firstPicInstructions.setBounds(370, 250, 500, 50);
		add(firstPicInstructions);
		
		secondPicInstructions = new JLabel("When you get an answer wrong, rewrite the correct answer in the area given");
		secondPicInstructions.setBounds(375, 600, 500, 50);
		add(secondPicInstructions);
		
		back = new JButton("Back");
		back.setBounds(530, 650, 100, 100);
		back.addActionListener(this);
		add(back);
		
		getImage();
	}
	
	private void getImage()
	{
		firstPictName = "firstImage.png";
		secondPictName = "secondImage.png";
		File firstPictFile = new File(firstPictName);
		File secondPictFile = new File(secondPictName);
		try
		{
			firstImage = ImageIO.read(firstPictFile);
			secondImage = ImageIO.read(secondPictFile);
		}
		catch(IOException e)
		{
			System.err.println("Cannot open file");
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(firstImage, 400, 0, 400, 254, this);
		g.drawImage(secondImage, 400, 350, 400, 254, this);
	}
	
	public void actionPerformed(ActionEvent evt)
	{
		String command = evt.getActionCommand();
		if(command.equals("Back"))
		{
			HomePage hp = new HomePage(listOfCards, gp, spanishWord, englishWord, numLines);
			gp.add(hp, "1");
			listOfCards.next(gp);
		}
	}
}


class GameScreen extends JPanel
{
	private CardLayout listOfCards; // cardlayout
	private GamePanel gp; // instance of gamepanel
	private JButton deathScreen; // button to go to deathscreen
	private JButton back; // button to go back to homepage
	private JLabel translationWord; // label of the word to translate
	private int numLives; // number of players lives
	private String[] spanishWord; // word to translate
	private String[] englishWord;
	private int x1, x2, x3, x4; // x coordinates of obstacles
	private int y1, y2, y3, y4; // y coordinates of obstacles
	private int xIcon, yIcon; // x and y coordinates of the player icon
	private Timer timer; // timer
	private GameScreenHandler gsh; // hander class for the game screen
	private String difficulty; // difficulty
	private boolean left1, left2; // determines the direction of the obstacles
	private int numLines;
	private String[] randSpanishWord;
	private String[] randEnglishWord;
	private JLabel firstWord;
	private JLabel secondWord;
	private JLabel thirdWord;
	private int xAnswer1, xAnswer2, xAnswer3;
	private int yAnswer;
	private int randomNumber;
	private boolean initialize;
	private int[] nums;
	private int score;
	private int[] randOrder;
	private int count;
	private int[] randWrongNumber;
	private boolean random;
	private int[] allScores;
	
	public GameScreen(CardLayout c, GamePanel g, int n, String cmd, String[] s, String[] e, int l, int sc, int[] o, int u, int[] as)
	{
		//initialize = true;
		
		xIcon = 550;
		yIcon = 700;
		
		xAnswer1 = 100;
		xAnswer2 = 500;
		xAnswer3 = 900;
		
		yAnswer = 50;
		
		x1 = 0;
		x2 = 300;
		x3 = 900;
		x4 = 600;
		
		y1 = 150;
		y2 = 275;
		y3 = 400;
		y4 = 525;
		
		listOfCards = c;
		gp = g;
		numLives = n;
		difficulty = cmd;
		spanishWord = s;
		englishWord = e;
		numLines = l;
		score = sc;
		randOrder = o;
		count = u;
		allScores = as;
		
		setBackground(Color.RED);
		setLayout(null);
		
		
		
		
		translationWord = new JLabel("Translate: " + spanishWord[randOrder[count]]);
		translationWord.setBounds(550, 0, 400, 50);
		translationWord.setFont(new Font("Serif", Font.BOLD, 40));
		add(translationWord);
			
		randWrongNumber = new int[2];
		randomNumber = (int)(Math.random()*3);
			
		random = false;
		
		if(randomNumber == 0)
		{
			
			firstWord = new JLabel(englishWord[randOrder[count]]);
			firstWord.setBounds(100, 50, 100, 50);
			firstWord.setFont(new Font("Serif", Font.BOLD, 20));
			add(firstWord);
			
			while(random == false)
			{
				randNum();
			}
			
			
			secondWord = new JLabel(englishWord[randOrder[randWrongNumber[0]]]);
			secondWord.setBounds(500, 50, 100, 50);
			secondWord.setFont(new Font("Serif", Font.BOLD, 20));
			add(secondWord);
					
			thirdWord = new JLabel(englishWord[randOrder[randWrongNumber[1]]]);
			thirdWord.setBounds(900, 50, 100, 50);
			thirdWord.setFont(new Font("Serif", Font.BOLD, 20));
			add(thirdWord);
					
		}
		if(randomNumber == 1)
		{
			
			secondWord = new JLabel(englishWord[randOrder[count]]);
			secondWord.setBounds(500, 50, 100, 50);
			secondWord.setFont(new Font("Serif", Font.BOLD, 20));
			add(secondWord);
			
			
			while(random == false)
			{
				randNum();
			}
		
			
			firstWord = new JLabel(englishWord[randOrder[randWrongNumber[0]]]);
			firstWord.setBounds(100, 50, 100, 50);
			firstWord.setFont(new Font("Serif", Font.BOLD, 20));
			add(firstWord);
					
			thirdWord = new JLabel(englishWord[randOrder[randWrongNumber[1]]]);
			thirdWord.setBounds(900, 50, 100, 50);
			thirdWord.setFont(new Font("Serif", Font.BOLD, 20));
			add(thirdWord);
		}
		if(randomNumber == 2)
		{
			
			thirdWord = new JLabel(englishWord[randOrder[count]]);
			thirdWord.setBounds(900, 50, 100, 50);
			thirdWord.setFont(new Font("Serif", Font.BOLD, 20));
			add(thirdWord);
			
			
			while(random == false)
			{
				randNum();
			}
			
				
			firstWord = new JLabel(englishWord[randOrder[randWrongNumber[0]]]);
			firstWord.setBounds(100, 50, 100, 50);
			firstWord.setFont(new Font("Serif", Font.BOLD, 20));
			add(firstWord);
						
			secondWord = new JLabel(englishWord[randOrder[randWrongNumber[1]]]);
			secondWord.setBounds(500, 50, 100, 50);
			secondWord.setFont(new Font("Serif", Font.BOLD, 20));
			add(secondWord);
		}
		
		
		// deathScreen
		deathScreen = new JButton("you died");
		deathScreen.setBounds(700, 300, 300, 300);
		//deathScreen.addActionListener(gsh);
		//add(deathScreen);
		
		// back button
		
		left1 = left2 = false;
		
		gsh = new GameScreenHandler(this);
		back = new JButton("go back");
		back.setBounds(1100, 650, 100, 100);
		back.addActionListener(gsh);
		add(back);
		
		timer = new Timer(0, gsh);
	}
	
	private void randNum()
	{
		for(int i = 0; i < 2; i++)
		{
			randWrongNumber[i] = (int)(Math.random()*numLines);
		}
		
		if(randOrder[randWrongNumber[0]] != randOrder[count] && randOrder[randWrongNumber[1]] != randOrder[count] && randWrongNumber[0] != randWrongNumber[1])
		{
			random = true;
		}
		else
		{
			random = false;
		}
	}
	
	private class GameScreenHandler implements ActionListener, KeyListener
	{
		
		private GameScreenHandler(GameScreen g)
		{
			addKeyListener(this);
		}
	
		public void actionPerformed(ActionEvent evt)
		{
			if(difficulty == "easy")
			{
				timer.setDelay(50);
			}
			if(difficulty == "medium")
			{
				timer.setDelay(25);
			}
			if(difficulty == "hard")
			{
				timer.setDelay(5);
			}
			
			String command = evt.getActionCommand();
			requestFocusInWindow();
			
			repaint();
			if(command == "go back")
			{
				System.out.println(command);
				HomePage hp = new HomePage(listOfCards, gp, spanishWord, englishWord, numLines);
				gp.add(hp, "1");
				listOfCards.next(gp);
			}
			
		}
		
		public void keyPressed(KeyEvent evt)
		{
			int code = evt.getKeyCode();
			if(code == KeyEvent.VK_LEFT)
			{
				xIcon -= 20;
				repaint();
			}
			if(code == KeyEvent.VK_RIGHT)
			{
				xIcon += 20;
				repaint();
			}
			if(code == KeyEvent.VK_UP)
			{
				yIcon -= 20;
				repaint();
			}
			if(code == KeyEvent.VK_DOWN)
			{
				yIcon += 20;
				repaint();
			}
		}
		public void keyReleased(KeyEvent evt) {}
		public void keyTyped(KeyEvent evt) {}
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		timer.start();
		g.setColor(Color.CYAN);
		g.fillRect(x1, y1, 300, 50);
		g.fillRect(x2, y2, 300, 50);
		g.fillRect(x3, y3, 300, 50);
		g.fillRect(x4, y4, 300, 50);
		g.setColor(Color.BLACK);
		g.fillOval(xIcon, yIcon, 50, 50);
		
		g.setColor(Color.WHITE);
		g.fillRect(xAnswer1, yAnswer, 200, 50);
		g.fillRect(xAnswer2, yAnswer, 200, 50);
		g.fillRect(xAnswer3, yAnswer, 200, 50);
		
		if (!left1 && x1 < 900)
		{
			x1+=2;
			x3-=2;
		}
		else
		{ 
			left1 = true;
			x1-=2;
			x3+=2;
		}
		
		if (left1 && x1 > 0)
		{
			x1-=2;
			x3+=2;
		}
		else 
		{
			left1 = false; 
			x1+=2;
			x3-=2; 
		}
		
		if (!left2 && x4 < 900)
		{
			x2-=2;
			x4+=2;
		}
		else
		{ 
			left2 = true;
			x2+=2;
			x4-=2;
		}
		
		if (left2 && x4 > 0)
		{
			x2+=2;
			x4-=2;
		}
		else 
		{
			left2 = false; 
			x2-=2;
			x4+=2; 
		}
		
		if(xIcon > x1 && xIcon < (x1 + 300) && yIcon > y1 && yIcon < (y1 + 50) || xIcon + 25 > x1 && xIcon + 25 < (x1 + 300) && yIcon + 25> y1 && yIcon + 25< (y1 + 50))
		{
			numLives++;
			score -= 10;
			
			if(numLives != 3)
			{
				WrongAnswer wa = new WrongAnswer(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, spanishWord[randOrder[count]], englishWord[randOrder[count]], count, allScores);
				gp.add(wa, "4");
				listOfCards.next(gp);
			}
			else if(numLives == 3)
			{
				DeathScreen ds = new DeathScreen(listOfCards, gp, score, allScores, spanishWord, englishWord, numLines);
				gp.add(ds, "5");
				listOfCards.next(gp);
			}
		}
		if(xIcon > x2 && xIcon < (x2 + 300) && yIcon > y2 && yIcon < (y2 + 50) || xIcon + 25 > x2 && xIcon + 25 < (x2 + 300) && yIcon + 25> y2 && yIcon + 25< (y2 + 50))
		{
			numLives++;
			score -= 10;
			if(numLives != 3)
			{
				WrongAnswer wa = new WrongAnswer(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, spanishWord[randOrder[count]], englishWord[randOrder[count]], count, allScores);
				gp.add(wa, "4");
				listOfCards.next(gp);
			}
			else if(numLives == 3)
			{
				DeathScreen ds = new DeathScreen(listOfCards, gp, score, allScores, spanishWord, englishWord, numLines);
				gp.add(ds, "5");
				listOfCards.next(gp);
			}
		}
		if(xIcon > x3 && xIcon < (x3 + 300) && yIcon > y3 && yIcon < (y3 + 50) || xIcon + 25 > x3 && xIcon + 25 < (x3 + 300) && yIcon + 25> y3 && yIcon + 25< (y3 + 50))
		{
			numLives++;
			score -= 10;
			if(numLives != 3)
			{
				WrongAnswer wa = new WrongAnswer(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, spanishWord[randOrder[count]], englishWord[randOrder[count]], count, allScores);
				gp.add(wa, "4");
				listOfCards.next(gp);
			}
			else if(numLives == 3)
			{
				DeathScreen ds = new DeathScreen(listOfCards, gp, score, allScores, spanishWord, englishWord, numLines);
				gp.add(ds, "5");
				listOfCards.next(gp);
			}
		}
		if(xIcon > x4 && xIcon < (x4 + 300) && yIcon > y4 && yIcon < (y4 + 50) || xIcon + 25 > x4 && xIcon + 25 < (x4 + 300) && yIcon + 25> y4 && yIcon + 25< (y4 + 50))
		{
			numLives++;
			score -= 10;
			if(numLives != 3)
			{
				WrongAnswer wa = new WrongAnswer(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, spanishWord[randOrder[count]], englishWord[randOrder[count]], count, allScores);
				gp.add(wa, "4");
				listOfCards.next(gp);
			}
			else if(numLives == 3)
			{
				DeathScreen ds = new DeathScreen(listOfCards, gp, score, allScores, spanishWord, englishWord, numLines);
				gp.add(ds, "5");
				listOfCards.next(gp);
			}
		}
		
		if(xIcon > xAnswer1 && xIcon < (xAnswer1 + 200) && yIcon > yAnswer && yIcon < (yAnswer + 50) || xIcon + 25 > xAnswer1 && xIcon + 25 < (xAnswer1 + 200) && yIcon + 25> yAnswer && yIcon + 25< (yAnswer + 50))
		{
			if(randomNumber == 0)
			{
				count++;
				score += 10;
				if(count == numLines)
				{
					WinScreen ws = new WinScreen(listOfCards, gp, spanishWord, englishWord, numLines, score, allScores);
					gp.add(ws, "6");
					listOfCards.next(gp);
				}
				else
				{
					GameScreen gs = new GameScreen(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, count, allScores);
					gp.add(gs, "3");
					listOfCards.next(gp);
				}
			}
			else
			{
				numLives++;
				score -= 10;
				if(numLives != 3)
				{
					WrongAnswer wa = new WrongAnswer(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, spanishWord[randOrder[count]], englishWord[randOrder[count]], count, allScores);
					gp.add(wa, "4");
					listOfCards.next(gp);
				}
				else if(numLives == 3)
				{
					DeathScreen ds = new DeathScreen(listOfCards, gp, score, allScores, spanishWord, englishWord, numLines);
					gp.add(ds, "5");
					listOfCards.next(gp);
				}
			}
		}
		if(xIcon > xAnswer2 && xIcon < (xAnswer2 + 200) && yIcon > yAnswer && yIcon < (yAnswer + 50) || xIcon + 25 > xAnswer2 && xIcon + 25 < (xAnswer2 + 200) && yIcon + 25> yAnswer && yIcon + 25< (yAnswer + 50))
		{
			if(randomNumber == 1)
			{
				count++;
				score += 10;
				if(count == numLines)
				{
					WinScreen ws = new WinScreen(listOfCards, gp, spanishWord, englishWord, numLines, score, allScores);
					gp.add(ws, "6");
					listOfCards.next(gp);
				}
				else
				{
					GameScreen gs = new GameScreen(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, count, allScores);
					gp.add(gs, "3");
					listOfCards.next(gp);
				}
			}
			else
			{
				numLives++;
				score -= 10;
				if(numLives != 3)
				{
					WrongAnswer wa = new WrongAnswer(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, spanishWord[randOrder[count]], englishWord[randOrder[count]], count, allScores);
					gp.add(wa, "4");
					listOfCards.next(gp);
				}
				else if(numLives == 3)
				{
					DeathScreen ds = new DeathScreen(listOfCards, gp, score, allScores, spanishWord, englishWord, numLines);
					gp.add(ds, "5");
					listOfCards.next(gp);
				}
			}
		}
		if(xIcon > xAnswer3 && xIcon < (xAnswer3 + 200) && yIcon > yAnswer && yIcon < (yAnswer + 50) || xIcon + 25 > xAnswer3 && xIcon + 25 < (xAnswer3 + 200) && yIcon + 25> yAnswer && yIcon + 25< (yAnswer + 50))
		{
			if(randomNumber == 2)
			{
				count++;
				score += 10;
				if(count == numLines)
				{
					WinScreen ws = new WinScreen(listOfCards, gp, spanishWord, englishWord, numLines, score, allScores);
					gp.add(ws, "6");
					listOfCards.next(gp);
				}
				else
				{
					GameScreen gs = new GameScreen(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, count, allScores);
					gp.add(gs, "3");
					listOfCards.next(gp);
				}
			}
			else
			{
				numLives++;
				score -= 10;
				if(numLives != 3)
				{
					WrongAnswer wa = new WrongAnswer(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, spanishWord[randOrder[count]], englishWord[randOrder[count]], count, allScores);
					gp.add(wa, "4");
					listOfCards.next(gp);
				}
				else if(numLives == 3)
				{
					DeathScreen ds = new DeathScreen(listOfCards, gp, score, allScores, spanishWord, englishWord, numLines);
					gp.add(ds, "5");
					listOfCards.next(gp);
				}
			}
		}
	}
}

class WrongAnswer extends JPanel
{
	private CardLayout listOfCards; // cardlayout
	private GamePanel gp; // instance of gamepanel
	private JButton back; // button to go back
	private int numLives; // number of players lives
	private String difficulty; // difficulty of game
	private String[] spanishWord;
	private String[] englishWord;
	private int numLines;
	private int score;
	private int[] randOrder;
	private int count;
	private JLabel title;
	private JTextField userText;
	private JLabel translationWord;
	private String wordToTranslate;
	private String englishWordToTranslate;
	private Center center;
	private int[] allScores;
	
	public WrongAnswer(CardLayout c, GamePanel g, int n, String cmd, String[] s, String[] e, int l, int sc, int[] ro, String wtt, String ewtt, int cou, int[] as)
	{
		
		wordToTranslate = wtt;
		englishWordToTranslate = ewtt;
		listOfCards = c;
		gp = g;
		numLives = n;
		difficulty = cmd;
		spanishWord = s;
		englishWord = e;
		numLines = l;
		score = sc;
		randOrder = ro;
		count = cou;
		allScores = as;
		
		setBackground(Color.YELLOW);
		setLayout(new BorderLayout());
		
		title = new JLabel("You Got Hit!");
		title.setFont(new Font("Serif", Font.BOLD, 50));
		title.setHorizontalAlignment(JLabel.CENTER);
		add(title, BorderLayout.NORTH);
		
		center = new Center(this);
		add(center, BorderLayout.CENTER);
		
	}
	
	class Center extends JPanel
	{
		private JButton enterButton;
		private JLabel label;
		private JLabel space;
		private CenterListener cl;
		private String answer;
		
		private Center(WrongAnswer wa)
		{
			//System.out.println("asejkfghaslkdfhasikldfasdf");
			cl = new CenterListener();
			
			setBackground(Color.RED);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setAlignmentX(Component.CENTER_ALIGNMENT);
			
			translationWord = new JLabel(wordToTranslate + " means " + englishWordToTranslate);
			translationWord.setFont(new Font("Serif", Font.BOLD, 40));
			add(translationWord);
			
			label = new JLabel("write the correct translation below");
			label.setFont(new Font("Serif", Font.BOLD, 25));
			add(label);
			
			space = new JLabel("  ");
			space.setFont(new Font("Serif", Font.BOLD, 100));
			add(space);
			
			userText = new JTextField();
			userText.setPreferredSize(new Dimension(100, 100));
			userText.addActionListener(cl);
			add(userText);
			
			space = new JLabel("  ");
			space.setFont(new Font("Serif", Font.BOLD, 100));
			add(space);
			
			enterButton = new JButton("enter");
			enterButton.addActionListener(cl);
			add(enterButton);
			
			space = new JLabel("  ");
			space.setFont(new Font("Serif", Font.BOLD, 100));
			add(space);
		}
		
		private class CenterListener implements ActionListener
		{
			public void actionPerformed(ActionEvent evt)
			{
				answer = userText.getText();
				String command = evt.getActionCommand();
				
				if(answer.equals(englishWordToTranslate))
				{
					if(command == "enter" && count == numLines)
					{
						WinScreen ws = new WinScreen(listOfCards, gp, spanishWord, englishWord, numLines, score, allScores);
						gp.add(ws, "6");
						listOfCards.next(gp);
					}
					count++;
					//System.out.println("count" + count);
					GameScreen gs = new GameScreen(listOfCards, gp, numLives, difficulty, spanishWord, englishWord, numLines, score, randOrder, count, allScores);
					gp.add(gs, "3");
					listOfCards.next(gp);
				}
			}
		}
	}
}

class DeathScreen extends JPanel implements ActionListener
{
	private CardLayout listOfCards; // cardlayout
	private GamePanel gp; // instance of the gamepanel class
	private JButton home; // button to go to the homescreen
	private JButton back; // button to go back
	private JLabel lose; // label saying "you lose"
	private String[] spanishWord; // words to translate
	private String[] englishWord;
	private int numLines;
	private int score;
	private PrintWriter output;
	private JLabel scoreLabel;
	private int[] allScores;
	
	public DeathScreen(CardLayout c, GamePanel g, int sc, int[] as, String[] sw, String[] ew, int nl)
	{
		output = null;
		listOfCards = c;
		gp = g;
		score = sc;
		allScores = as;
		spanishWord = sw;
		englishWord = ew;
		numLines = nl;
		
		setBackground(Color.BLUE);
		setLayout(new BorderLayout());
		
		highScore();
		
		// add death label
		lose = new JLabel("you lost");
		lose.setForeground(Color.RED);
		lose.setFont(new Font("Serif", Font.BOLD, 50));
		lose.setHorizontalAlignment(JLabel.CENTER);
		add(lose, BorderLayout.NORTH);
		
		scoreLabel = new JLabel("your score was " + score);
		scoreLabel.setFont(new Font("Serif", Font.BOLD, 50));
		scoreLabel.setForeground(Color.RED);
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		add(scoreLabel, BorderLayout.CENTER);
		
		// add back to home button
		home = new JButton("back to home");
		home.addActionListener(this);
		home.setPreferredSize(new Dimension(100, 100));
		add(home, BorderLayout.SOUTH);
		
	}
	
	public void actionPerformed(ActionEvent evt)
	{
		String command = evt.getActionCommand();
		if(command == "back to home")
		{
			HomePage hp = new HomePage(listOfCards, gp, spanishWord, englishWord, numLines);
			gp.add(hp, "1");
			listOfCards.next(gp);
		}
	}
	
	private void highScore()
	{
		String fileName = new String("highscore.txt");
		File outFile = new File(fileName);
		try
		{
			output = new PrintWriter(outFile);
		}
		catch(IOException e)
		{
			System.err.println("File not found");
		}
		output.println(score);
		// System.out.println(score);
		output.close();
	}
}

class WinScreen extends JPanel implements ActionListener
{
	
	private JLabel win;
	private JButton backToHome;
	private CardLayout listOfCards;
	private GamePanel gp;
	private String[] spanishWord;
	private String[] englishWord;
	private int numLines;
	private int score;
	private JLabel scoreLabel;
	private PrintWriter output;
	private int[] allScores;
	
	
	public WinScreen(CardLayout loc, GamePanel g, String[] sw, String[] ew, int n, int s, int[] as)
	{
		
		score = s;
		listOfCards = loc;
		gp = g;
		spanishWord = sw;
		englishWord = ew;
		numLines = n;
		allScores = as;
		
		
		setLayout(null);
		setBackground(Color.GREEN);
		
		win = new JLabel("You Win!");
		win.setFont(new Font("Serif", Font.BOLD, 25));
		win.setBounds(550, 50, 400, 50);
		add(win);
		
		scoreLabel = new JLabel("Score: " + score);
		scoreLabel.setFont(new Font("Serif", Font.BOLD, 25));
		scoreLabel.setBounds(550, 100, 300, 100);
		add(scoreLabel);
		
		highScore();
		
		backToHome = new JButton("back to home");
		backToHome.setBounds(500, 500, 200, 100);
		backToHome.addActionListener(this);
		add(backToHome);
	}
	
	private void highScore()
	{
		String fileName = new String("highscore.txt");
		File outFile = new File(fileName);
		try
		{
			output = new PrintWriter(outFile);
		}
		catch(IOException e)
		{
			System.err.println("File not found");
		}
		
		//System.out.println("score" + score);
		
		int[] highScoreNew = new int[allScores.length + 1];
		
		for(int i = 0; i < allScores.length; i++)
		{
			highScoreNew[i] = allScores[i];
		}
		
		System.out.println(allScores.length);
		// highScoreNew[(allScores.length + 1)] = score;
		
		for(int i = 0; i < allScores.length; i++)
		{
			output.println(allScores[i]);
			System.out.println(allScores[i]);
		}
		output.println(score);
		output.close();
			
	}
	
	public void actionPerformed(ActionEvent evt)
	{
		String command = evt.getActionCommand();
		if(command == "back to home")
		{
			HomePage hp = new HomePage(listOfCards, gp, spanishWord, englishWord, numLines);
			gp.add(hp, "1");
			listOfCards.next(gp);
		}
	}
}
	
