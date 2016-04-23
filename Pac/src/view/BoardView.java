package view;

import model.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.util.Vector;

/**
 * Mapa gry
 */

public class BoardView extends JPanel 
{
	private static final long serialVersionUID = -7729510720848698723L;
	private final Model model;
	/** rozmiar jednego pola na planszy */
	private final int blockSize;		
	/** szerokość planszy */
	private final int width;
	/** wysokość planszy */
	private final int height;
	/** promień punktu */
	private static final int radius = 6;
	/** szerokość krawędzi planszy */
	private final int fWidth;	
	/** rozmiar ikony Pacmana */
	private final int pacmanIconSize;
	/** Vector przechowujacy informacje o wspolrzednych niezbednych do rysowania mapy gry*/
	private final Vector<Coordinate> pathsCords;

	private final Image ghostUp;
	private final Image ghostDown;
	private final Image ghostLeft;
	private final Image ghostRight;
	
	private final Image uUp;
	private final Image uDown;
	private final Image uLeft;
	private final Image uRight;
	
	private final Image pacmanIcon;
	private final Image pacmanUp;
	private final Image pacmanDown;
	private final Image pacmanLeft;
	private final Image pacmanRight;
	
	public BoardView(final Model m, final int blockSize, final int fWidth)
	{
		this.model = m;
		this.blockSize = blockSize;
		this.width = model.getBoardWidth();
		this.height = model.getBoardHeight();
		this.fWidth = fWidth;
		this.pathsCords = model.getBoardInfo();
		
		this.ghostUp = new ImageIcon("src/materials/gup.png").getImage();
		this.ghostDown = new ImageIcon("src/materials/gdown.png").getImage();
		this.ghostLeft = new ImageIcon("src/materials/gleft.png").getImage();
		this.ghostRight = new ImageIcon("src/materials/gright.png").getImage();
		
		this.uUp = new ImageIcon("src/materials/uup.png").getImage();
		this.uDown = new ImageIcon("src/materials/udown.png").getImage();
		this.uLeft = new ImageIcon("src/materials/uleft.png").getImage();
		this.uRight = new ImageIcon("src/materials/uright.png").getImage();
		
		this.pacmanIcon = new ImageIcon("src/materials/pac.png").getImage();
		this.pacmanUp = new ImageIcon("src/materials/pup.gif").getImage();
		this.pacmanDown = new ImageIcon("src/materials/pdown.gif").getImage();
		this.pacmanLeft = new ImageIcon("src/materials/pleft.gif").getImage();
		this.pacmanRight = new ImageIcon("src/materials/pright.gif").getImage();
		this.pacmanIconSize = pacmanIcon.getHeight(this);
		
		this.setPreferredSize(new Dimension(2*fWidth + width, 2*fWidth + height));
	}
	
	@Override
	public void paintComponent(Graphics g)
	{    
		drawBoard(g);
		if(!model.isAlive())
			showCommunique(g, 18, "NACISNIJ enter ABY ROZPOCZAC");
		else if(model.ifWin())
			showCommunique(g, 18, "WYGRANA! NACISNIJ enter ABY ROZPOCZAC.");
		else if(model.getLifes() == 0)
			showCommunique(g, 18, "PRZEGRANA! NACISNIJ enter ABY ROZPOCZAC");	
	}

	/**
	 *  Metoda rysujaca mape gry i znajdujace sie na niej punkty
	 */
	private void drawBoard(final Graphics g)
	{
		int widthRec, heightRec;
		
		Vector<Coordinate> points = model.getPointsInfo();
		int limit = pathsCords.size(); 
		
		/**
		 *  Rysowanie scian 
		 */
		g.setColor(new Color(50, 35, 255));
		g.fillRect(0, 0, width + 2*fWidth, height + 2*fWidth + 40);
		g.setColor(Color.black);
		
		for(int i = 0; i < limit;)
		{
			Coordinate cords1 = pathsCords.get(i++);
			Coordinate cords2 = pathsCords.get(i++);
			
			if ( cords1.equals(new Coordinate(1*Constant.blockSize, 9*Constant.blockSize)) )
			{
				widthRec = cords2.getX() - cords1.getX() + blockSize + 8;
				heightRec = cords2.getY() - cords1.getY() + blockSize;
				g.fillRect(cords1.getX() - blockSize, fWidth + cords1.getY() - blockSize, widthRec, heightRec);
				continue;
			}
			else if ( cords2.equals(new Coordinate(17*Constant.blockSize, 9*Constant.blockSize)) )
			{
				widthRec = cords2.getX() - cords1.getX() + blockSize + 8;
			}
			else
			{
				widthRec = cords2.getX() - cords1.getX() + blockSize;
			}
			
			heightRec = cords2.getY() - cords1.getY() + blockSize;

			g.fillRect(fWidth + cords1.getX() - blockSize, fWidth + cords1.getY() - blockSize, widthRec, heightRec);
		}
		g.setColor(new Color(25, 25, 25));
		
		g.fillRect(fWidth + 288,fWidth + 252, blockSize, blockSize);
		
		g.setColor(Color.yellow);
		
		for(Coordinate cords : points)
		{
			/** jesli super punkty - wieksze */
			if ( ((cords.getX() == 1*blockSize) && (cords.getY()==2*blockSize)) || ((cords.getX() == 1*blockSize) && (cords.getY()==14*blockSize)) || ((cords.getX() == 17*blockSize) && (cords.getY()==2*blockSize)) || ((cords.getX() == 17*blockSize) && (cords.getY()==14*blockSize)) )
			{
				g.fillOval(fWidth + cords.getX() - (blockSize + 3*radius)/2, fWidth + cords.getY() - (blockSize + 3*radius)/2 , 3*radius, 3*radius);
			}
			
			g.fillOval(fWidth + cords.getX() - (blockSize + radius)/2, fWidth + cords.getY() - (blockSize + radius)/2 , radius, radius);
		}
		
		drawPacman(g, model.getPacmanCords(), model.getPacmanDirection());
		
		for(int i = 0; i < model.getGhostNumber(); ++i)
			drawGhost(g, model.getGhostCords(i), model.getGhostDirection(i));
	}		
	
	/**
	 * Rysowanie na mapie ghosta, w zależności od tego, w jakim kierunku się porusza
	 */
	private void drawGhost(final Graphics g, final Coordinate cords, final Constant.Direction direction)
	{
		Image ghost;
		
		if (model.isFlag() == false)
		{
			switch(direction)
			{
			case NONE:
				ghost = ghostLeft;
				break;
			case UP:
				ghost = ghostUp;
				break;
			case DOWN:
				ghost = ghostDown;
				break;
			case LEFT:
				ghost = ghostLeft;
				break;
			case RIGHT:
				ghost = ghostRight;
				break;
			default:
				ghost = ghostLeft;
				break;
			}
		}
		else 
		{
			switch(direction)
			{
			case NONE:
				ghost = uLeft;
				break;
			case UP:
				ghost = uUp;
				break;
			case DOWN:
				ghost = uDown;
				break;
			case LEFT:
				ghost = uLeft;
				break;
			case RIGHT:
				ghost = uRight;
				break;
			default:
				ghost = uLeft;
				break;
			}
			
		}
		
		g.drawImage(ghost, fWidth + cords.getX() - blockSize + (blockSize - pacmanIconSize)/2, fWidth + cords.getY() - blockSize + (blockSize - pacmanIconSize)/2, this);
	}
	
	/**
	 * Rysowanie na mapie pacmana (w zaleznosci od kierunku w ktorym sie porusza)
	 */
	private void drawPacman(final Graphics g, final Coordinate cords, final Constant.Direction direction)
	{
		Image pacman;
		
		switch(direction)
		{
		case NONE:
			pacman = pacmanIcon;
			break;
		case UP:
			pacman = pacmanUp;
			break;
		case DOWN:
			pacman = pacmanDown;
			break;
		case LEFT:
			pacman = pacmanLeft;
			break;
		case RIGHT:
			pacman = pacmanRight;
			break;
		default:
			pacman = pacmanIcon;
			break;
		}
		
		g.drawImage(pacman, fWidth + cords.getX() - blockSize + (blockSize - pacmanIconSize)/2, fWidth + cords.getY() - blockSize + (blockSize - pacmanIconSize)/2, this);
	}

	/**
	 * Funkcja wyswietlajaca na mapie gry komunikaty
	 * fontSize - rozmiar czcionki
	 * message - tresc komunikatu
	 */
	 public void showCommunique (final Graphics g, final int fontSize, final String message)
	 {			
	    g.setColor(new Color(55, 170, 0));
	    g.fillRect(40, height / 2 - 200, width - 100, 50);
	    g.setColor(Color.yellow);
		g.drawRect(40, height / 2 - 200, width - 100, 50);
	    
	    Font f = new Font("Arial", Font.BOLD, fontSize);
	    FontMetrics fm = this.getFontMetrics(f);
	    
	    g.setColor(Color.white);
	    g.setFont(f);
	    g.drawString(message, (width - fm.stringWidth(message)) / 2, height / 2 - 170 );
	}
}
