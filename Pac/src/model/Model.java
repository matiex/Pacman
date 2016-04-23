package model;

import java.util.Map;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Model gry. Zawiera wszystkie struktury danych reprezentujace elementy swiata gry i zasady
 * dotyczace rozgrywki.
 *
 */
public class Model 
{
	private final Board board;
	
	private final Ghost[] ghost;
	private final Coordinate ghostInitCords;
	/** poczatkowa sciezka na ktorej znajduja sie duchy */
	private final Path ghostInitPath;	
	private short ghostNumber;
	
	private final Pacman pacman;
	private final Coordinate pacmanInitCords;
	/** poczatkowa sciezka na ktorej znajduje sie Pacman */
	private final Path pacmanInitPath;
	/** specjalna sciezka lewa */
	private final Path specialPath1;
	/** specjalna sciezka prawa */
	private final Path specialPath2;
	private int lifes;
	
	private int pointsCounter;
	private boolean alive;
	private boolean started;
	/** Do odmierzania czasu uprzywilejowanego trybu */
	private long startTime;		
	/** Do odmierzania czasu uprzywilejowanego trybu */
	private long estimatedTime;
	/** Czy jestesmy w uprzywilejowanym trybie */
	private int flag;
	/** Jaki punkt zjedlismy: 0 - nic, 1 - zwykly, 2 - super punkt */
	private int b;
	
	public Model(final Board b)
	{
		this.board = b;
		this.ghostInitCords = new Coordinate(9*Constant.blockSize, 8*Constant.blockSize);
		this.ghostInitPath = b.getNode(new Coordinate(9*Constant.blockSize, 7*Constant.blockSize)).getPath(Constant.Direction.DOWN);
		this.ghostNumber =  20;
		this.specialPath1 = b.getNode(new Coordinate(4*Constant.blockSize, 9*Constant.blockSize)).getPath(Constant.Direction.LEFT);
		this.specialPath2 = b.getNode(new Coordinate(14*Constant.blockSize, 9*Constant.blockSize)).getPath(Constant.Direction.RIGHT);
		this.ghost = new Ghost[ghostNumber];
		for(int i = 0; i < ghostNumber; ++i)
			ghost[i] = new Ghost(ghostInitPath, ghostInitCords, specialPath1, specialPath2);
		
		this.pacmanInitCords = new Coordinate(9*Constant.blockSize, 10*Constant.blockSize);
		this.pacmanInitPath = b.getNode(new Coordinate(6*Constant.blockSize, 10*Constant.blockSize)).getPath(Constant.Direction.RIGHT);
		this.pacman = new Pacman(pacmanInitPath, pacmanInitCords, specialPath1, specialPath2);
		this.lifes = 3;
		
		this.pointsCounter = 0;
		this.alive = false;
		this.started = false;
		this.startTime = 0;
		this.estimatedTime = 0;
		this.flag = 0;
		this.b=0;
		this.board.checkPoint(new Coordinate(9*Constant.blockSize, 10*Constant.blockSize));
	}
	
	/**
	 * @return Liczba duchów na planszy
	 */
	public int getGhostNumber()
	{
		return this.ghostNumber;
	}
	
	public int getBoardWidth()
	{
		return board.getWidth();
	}
	
	public int getBoardHeight()
	{
		return board.getHeight();
	}
	
	/**
	 * @return Liczba żyć
	 */
	public int getLifes()
	{ 
		return this.lifes;
	}
	
	/**
	 * @return Liczba zdobytych punktów
	 */
	public int getPointsCounter() 
	{ 
		return this.pointsCounter; 
	}
	
	/**
	 * @return Plansza gry
	 */
	public Board getBoard() 
	{ 
		return this.board; 
	}	

	public Coordinate getPacmanCords() 
	{ 
		return this.pacman.getCords(); 
	}
	
	public Constant.Direction getPacmanDirection()
	{
		return this.pacman.getDirection();
	}
	
	public Coordinate getGhostCords(final int i) 
	{ 
		return this.ghost[i].getCords(); 
	}
	
	public Constant.Direction getGhostDirection(final int i)
	{
		return this.ghost[i].getDirection();
	}
	
	/**
	 * Metoda zwraca wektor ze wspolrzednymi wezlow sciezek, ktore sa niezbedne do poprawnego narysowania mapy
	 * gry przez widok
	 * @return wektor wspolrzednych wezlow
	 */
	public Vector<Coordinate> getBoardInfo()
	{
		TreeSet<Path> paths = board.getPaths();
		Vector<Coordinate> pathsCords = new Vector<Coordinate>();
		
		for(Path p : paths)
		{
			pathsCords.add(p.getNodeFirstCoords());
			pathsCords.add(p.getNodeSecondCoords());
		}
		
		return pathsCords;
	}
	
	/**
	 * Metoda zwraca wektor zawierajacy wspolrzedne wszystkich punktów, które nie zostały jeszcze zdobyte
	 * @return wektor wszystkich niezdobytych punktów
	 */
	public Vector<Coordinate> getPointsInfo()
	{
		TreeMap<Coordinate, Point> points = board.getPoints();
		Vector<Coordinate> pointsCords = new Vector<Coordinate>();
		
		for(Map.Entry<Coordinate, Point> entry : points.entrySet())
		{
			Point point = entry.getValue();
			
			if(!point.ifVisited())
				pointsCords.add(point.getCords());
		}	
		
		return pointsCords;
	}
	
	/**
	 * @return true - jeśli Pacman nadal ma życia
	 */
	public boolean isAlive() 
	{ 
		return this.alive; 
	}

	public boolean isFlag()
	{
		if (flag==1)
			return true;
		else
			return false;
	}
	/**
	 * @return true - jeśli zostały zebrane wszystkie punkty na mapie, false w przeciwnym razie.
	 */
	public boolean ifWin()
	{ 
		return board.noPointsLeft();
	}
	
	public void setAlive(final boolean b)
	{
		this.alive = b;
	}
	
	/**
	 * Rozpoczyna nowa gre
	 */
	public void setNewGame()
	{
		resetPacman();
		resetGhosts();
		lifes = 3;
		pointsCounter = 0;
		board.reloadPoints();
		setAlive(true);
	}
	
	/**
	 * Sprawdzenie kolizji. W zaleznosci od uprzywilejowania resetujemy duchy lub pacmana (i odejmujemy jedno zycie)
	 * 
	 * @return true jeśli liczba żyć jest większa niż 0, false w przeciwnym razie
	 */
	public boolean checkCollision()
	{
		boolean collision = false;
		if (flag == 0)
		{
			for(int i = 0; i < ghostNumber; ++i)
			{
				collision = pacman.checkCollision(ghost[i].getCords());
				if(collision)
				{
					if(--lifes == 0)
					{
						return false;
					}
					resetPacman();
					resetGhosts();
				}
			}
		}
		else if (flag == 1)
		{
			for(int i = 0; i < ghostNumber; ++i)
			{
				collision = pacman.checkCollision(ghost[i].getCords());
				if(collision)
				{
					pointsCounter = pointsCounter + 50;
					ghost[i].setDirection(Constant.Direction.UP);
					ghost[i].getCords().setCords(ghostInitCords.getX(), ghostInitCords.getY());
					ghost[i].setPath(ghostInitPath);
				}
			}
		}
		
			return true;
	}
	
	/**
	 * Sprawdza czy zostal zdobyty punkt. Jesli tak, zwieksza licznik zdobytych punktow.
	 * @return 1 jesli punkt zostal zdobyty, 2 jesli zjedlismy super punkt
	 */
	public int checkPoint()
	{		
		b = board.checkPoint(pacman.getCords());

		if(b==2) /** jezeli zjedlismy super punkt */
		{	
			++pointsCounter;
			flag=1;
			pacman.setSpeed(4);
			startTime = System.currentTimeMillis();
		}
		if (flag == 1)
		{
			estimatedTime = System.currentTimeMillis() - startTime;
			if (estimatedTime > 5000)
			{
			pacman.setSpeed(2);
			flag=0;
			startTime = 0;
			estimatedTime = 0;
			}
			
		}
		
		/** jezeli zjedlismy zwykly punkt */
		
		if(b==1) ++pointsCounter;
		
		return b;
	}
	
	/**
	 * Ruch wszystkich duchów
	 */
	public void moveGhosts()
	{
		if(pacman.getStarted())
		{
			for(int i = 0; i < ghostNumber; ++i)
				ghost[i].move();
		}
	}
	
	
	/**
	 * Ustawia duchy na pole startowe
	 */
	private void resetGhosts()
	{
		for(int i = 0; i < ghostNumber; ++i)
		{
			ghost[i].setDirection(Constant.Direction.UP);
			ghost[i].getCords().setCords(ghostInitCords.getX(), ghostInitCords.getY());
			ghost[i].setPath(ghostInitPath);
		}
	}
	
	/**
	 * Ruch Pacmana
	 */
	public void movePacman()
	{
		pacman.move();
	}
	
	/**
	 * Ustawia Pacmana na pole startowe
	 */
	private void resetPacman()
	{
		pacman.setDirection(Constant.Direction.NONE);
		pacman.getCords().setCords(pacmanInitCords.getX(), pacmanInitCords.getY());
		pacman.setPath(pacmanInitPath);
		pacman.setStarted(false);
		pacman.setSpeed(2);
	}
	
	public void setStarted(final boolean s)
	{
		this.started = s;
	}
	
	public void setPacmanNextDirection(final Constant.Direction newDirection)
	{
		pacman.setNextDirection(newDirection);
	}
	
	/**
	 * @return true jeśli gra już się rozpoczęła, false w przeciwnym razie
	 */
	public boolean ifStarted() 
	{ 
		return this.started;
	}
	
}
