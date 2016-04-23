package model;

/**
 * Klasa reprezentujaca Pacmana; odpowiada za jego ruch
 *  
 */
public class Pacman 
{
	private Coordinate cords; /** wspolrzedne Pacmana */
	private Constant.Direction currentDirection;
	private Constant.Direction nextDirection;
	private Path path;
	private Path path1; /** sciezka do skrajnego prawego wezla */
	private Path path2; /** sciezka do skrajnego lewego wezla */
	private int speed;
	private boolean started;

	/**
	 * Konstrktor, ustawia poczatkowa sciezke(p) i wspolrzedne
	 */
	public Pacman(final Path p, final Coordinate cords, final Path p1, final Path p2)
	{
		this.cords = new Coordinate(cords.getX(), cords.getY());
		this.currentDirection = Constant.Direction.NONE;
		this.nextDirection = Constant.Direction.NONE;
		this.path = p;
		this.speed = Constant.blockSize/18;
		this.started = false;
		this.path1 = p1;
		this.path2 = p2;
	}
	
	
	/**
	 * Zwraca wspolrzedne Pacmana
	 */
	public Coordinate getCords() 
	{ 
		return cords; 
	}
	
	public boolean getStarted()
	{
		return started;
	}
	
	/**	
	 * Funkcja, ktora zwraca kierunek, w ktorym porusza sie Pacman
	 */
	public Constant.Direction getDirection() 
	{ 
		return currentDirection; 
	}
	
	/**
	 * Funkcja, ktora zwraca kierunek w który Pacman chce skręcić przy najbliższej okazji
	 */
	public Constant.Direction getNextDirection()
	{ 
		return nextDirection; 
	}
	
	/**
	 * @return wezel do ktorego zmierza Pacman
	 */
	public Node getTarget()
	{
		if(currentDirection == Constant.Direction.UP || currentDirection == Constant.Direction.LEFT)
			return path.getNodeFirst();
		else if(currentDirection == Constant.Direction.DOWN || currentDirection == Constant.Direction.RIGHT)
			return path.getNodeSecond();
		else
		{
			if(path.getNodeFirst().getCords().equals(cords))
				return path.getNodeFirst();
			else 
				return path.getNodeSecond();
		}
	}
	
	/**
	 * @return wezel ktory ostatnio odwiedzilismy
	 */
	
	public Node getLastNode()
	{
		if(currentDirection == Constant.Direction.UP || currentDirection == Constant.Direction.LEFT)
			return path.getNodeSecond();
		else if(currentDirection == Constant.Direction.DOWN || currentDirection == Constant.Direction.RIGHT)
			return path.getNodeFirst();
		else
		{
			if(path.getNodeFirst().getCords().equals(cords))
				return path.getNodeSecond();
			else 
				return path.getNodeFirst();
		}
		
	}
	
	/**
	 *	Funkcja ustawiajaca predkosc Pacman'a 
	 */
	
	public void setSpeed(int i)
	{
		this.speed=i;
		return;
	}
	
	
	/**
	 * Ustawienie nowego następnego kierunku
	 * @param newDirection - nowy następny kierunek
	 */
	public void setNextDirection(final Constant.Direction newDirection)
	{
		nextDirection = newDirection;
	}
	
	/**
	 * Zmiana ścieżki którą podąża Pacman
	 * @param newPath - Nowa ścieżka, którą podąża Pacman
	 */
	public void setPath(final Path newPath)
	{
		path = newPath;
	}
	
	public void setStarted(final boolean s)
	{
		started = s;
	}
	
	/**
	 * Zmiana kierunku Pacmana
	 * @param newDirection - nowy kierunek
	 */
	public void setDirection(final Constant.Direction newDirection)
	{
		currentDirection = newDirection;
	}
	
	/**
	 *	Ruch Pacmana, sprawdzam, czy zapamietac nastepny kierunek, i czy mozna wykonac skret w zadanym kierunku; skret na zakretach odbywa sie automatycznie
	 */
	public void move()
	{
		 /** wezel, do ktorego zmierza Pacman */
		 Node targetNode = getTarget();
		 /** wezel, ktory ostatnio odwiedzilismy */
		 Node lastNode = getLastNode();
		 /** wspolrzedne ostatnio odwiedzonego punktu */
		 Coordinate lastCord = lastNode.getCords();
		 
		 /** wspolrzedne do zabraniania skretu w pole startowe */
		 Coordinate cord = new Coordinate (9*Constant.blockSize, 7*Constant.blockSize);
		 /** wspolrzedne do 2 skrajnych wezlow */	
		 Coordinate coordRight = new Coordinate (17*Constant.blockSize, 9*Constant.blockSize);
		 Coordinate coordLeft = new Coordinate (1*Constant.blockSize, 9*Constant.blockSize);
		  
		 if(!started)
		 {
			 if((nextDirection == Constant.Direction.LEFT) || (nextDirection == Constant.Direction.RIGHT))
			 {
				 currentDirection = nextDirection;
				 started = true;
				 nextDirection = Constant.Direction.NONE;
			 }
		 }
		 else if(isNearLastNode(lastNode) == false)
		 {
			if(wantsTurnBack())
			{
				currentDirection = nextDirection;
				nextDirection = Constant.Direction.NONE;
			}
			else if(isNodeNearEnough(targetNode))
			{
				
				if(cords.equals(targetNode.getCords())) //Jesli Pacman znajduje sie na wezle
				{
					/** Zablokowanie skretu do pola startowego duchow */
					if ( cord.equals(cords) && nextDirection == Constant.Direction.DOWN)
					{
							path = targetNode.getPath(getDirection());
					}
					else if ( coordRight.equals(cords) && currentDirection == Constant.Direction.RIGHT)
					{/** Skrajne przejscie w prawo*/
						
							currentDirection = Constant.Direction.RIGHT;
							nextDirection = Constant.Direction.NONE;
							
							path = path1;
							
							cords = new Coordinate(1*Constant.blockSize - 4, 9*Constant.blockSize);
					}
					else if (coordLeft.equals(cords) && currentDirection == Constant.Direction.LEFT)
					{ /** Skrajne przejscie w lewo*/
						
							currentDirection = Constant.Direction.LEFT;
							nextDirection = Constant.Direction.NONE;
							
							path = path2;
							
							cords = new Coordinate(17*Constant.blockSize + 4, 9*Constant.blockSize);
					}
					else if(targetNode.getPathNumber() == 2) //Jesli zakret
					{
						if(currentDirection.opposed(targetNode.getNewDirection(0)))
							currentDirection = targetNode.getNewDirection(1);
						else
							currentDirection = targetNode.getNewDirection(0);
						
						nextDirection = Constant.Direction.NONE;
						path = targetNode.getPath(currentDirection);
					}
					else if(nextDirection == Constant.Direction.NONE) //Jesli Pacman nie chce zmienic kierunku
					{
						if(ifMeetWall()) //Jesli nie moze isc w obecnym kierunku, to stop
						{
							currentDirection = Constant.Direction.NONE;
						}
						else //Kontynuuj ruch w obecnym kierunku
							path = targetNode.getPath(getDirection());
					}
					else //Jesli Pacman chce zmienic kierunek
					{
						if(targetNode.getPath(nextDirection) != null) //Jesli istnieje sciezka w tym kierunku
						{
							path = targetNode.getPath(nextDirection);
							currentDirection = nextDirection;
							nextDirection = Constant.Direction.NONE;
						}
						else //Jesli sciezka w zadanym kierunku nie istnieje, zignoruj zadanie.
						{	
							nextDirection = Constant.Direction.NONE;
							
							if(ifMeetWall()) 
							{
								currentDirection = Constant.Direction.NONE;
							}	
							else
							{	
								path = targetNode.getPath(currentDirection);
							}
						}
					}
				}
					
			}
			else
			{
				nextDirection = Constant.Direction.NONE;
			}
		 }
		 
		 /******************************* COFANIE PACMANA ************************************************/
		 
		 if (isNearLastNode(lastNode) && nextDirection != Constant.Direction.NONE) /** czy cofnac Pacmana */
		 {
			 	if(lastNode.getPath(nextDirection) != null && lastCord.equals(cord)==false && lastNode.getPathNumber() != 2) //Jesli istnieje sciezka w tym kierunku
				{
					cords = new Coordinate(lastCord);
					path = lastNode.getPath(nextDirection);
					currentDirection = nextDirection;
					nextDirection = Constant.Direction.NONE;
				}
		 }
			
		 switch(currentDirection)
		 {
		 case UP: 
			cords.addValue(0, -speed); 
		 	break;
		 case DOWN:
			cords.addValue(0, speed);
		 	break;
		 case LEFT:
			cords.addValue(-speed, 0);
		 	break;
		 case RIGHT:
			cords.addValue(speed, 0);
		 	break;
		 case NONE:
		 	break;
		 }
	 }
				
	/**
	 * Sprawdzenie czy Pacman jest w kolizji; return true jesli Pacman jest w kolizji
	 */
	public boolean checkCollision(final Coordinate cords)
	{
		if((cords.getY() == this.cords.getY()) && Math.abs(cords.getX() - this.cords.getX()) < Constant.blockSize/2) 
			return true;
		
		if((cords.getX() == this.cords.getX()) && Math.abs(cords.getY() - this.cords.getY()) < Constant.blockSize/2) 
			return true;
		
		if((Math.abs(cords.getX() - this.cords.getX()) < Constant.blockSize/2) && (Math.abs(cords.getY() - this.cords.getY()) < Constant.blockSize/2))
			return true;
		
		return false;
	}
	
	/** 
	 * Sprawdzenie czy Pacman chce zawrocic; return true jesli chce zawrocic
	 */
	public boolean wantsTurnBack()
	{
		switch(currentDirection)
		{
		case LEFT:
			if(nextDirection == Constant.Direction.RIGHT)
				return true;
			break;
		case RIGHT:
			if(nextDirection == Constant.Direction.LEFT)
				return true;
			break;
		case UP:
			if(nextDirection == Constant.Direction.DOWN)
				return true;
			break;
		case DOWN:
			if(nextDirection == Constant.Direction.UP)
				return true;
			break;
		case NONE:
			return false;
		}
		
		return false;
	}
	
	/**
	 * Funkcja sprawdzajaca czy Pacman jest w danej odleglosci(distance) od wezla; Przydaje sie do kasowania zmiany nastepnego kierunku, jezeli zmiana nastapila zbyt daleko od wezla
	 */
	public boolean isNodeNearEnough(final Node node)
	{
		Coordinate nodeCords = node.getCords();
		int distance = Constant.blockSize;
		
		switch(currentDirection)
		{
		case DOWN:
			if((nodeCords.getY() - cords.getY()) < (distance)) 
				return true;
			break;
		case UP:
			if((cords.getY() - nodeCords.getY()) < (distance)) 
				return true;
			break;
		case RIGHT:
			if((nodeCords.getX() - cords.getX()) < (distance)) 
				return true;
			break;
		case LEFT:
			if((cords.getX() - nodeCords.getX()) < (distance)) 
				return true;
			break;
		case NONE:
			return true;
		}
		return false;
	}
	
	/**
	 * W praktyce gracze często wściskają klawisz skrętu, gdy Pacman minie już dany węzeł;
	 * Funckja sprawdza czy wcisniecie klawisza nastapilo wystarczajaco blisko ostatniego wezla, aby cofnac Pacmana
	 */
	
	public boolean isNearLastNode(final Node node)
	{
		Coordinate nodeCords = node.getCords();
		int distance = 8;
		
		switch(currentDirection)
		{
		case DOWN:
			if((cords.getY() - nodeCords.getY()) < (distance) && (cords.getY() - nodeCords.getY()) !=0) return true;
			break;
		case UP:
			if((nodeCords.getY() - cords.getY()) < (distance) && (nodeCords.getY() - cords.getY()) !=0) return true;
			break;
		case RIGHT:
			if((cords.getX() - nodeCords.getX()) < (distance) && (cords.getX() - nodeCords.getX()) !=0) return true;
			break;
		case LEFT:
			if((nodeCords.getX() - cords.getX()) < (distance) && (nodeCords.getX() - cords.getX()) !=0) return true;
			break;
		case NONE:
			return false;
		}
		return false;
	}
	
	/**
	 * Sprzawdzenie, czy Pacman chce zmienić kierunek; return true jesli Pacman chce zmienic kierunek
	 */
	public boolean ifWantsNewDirection()
	{
		if(nextDirection == Constant.Direction.NONE) 
			return false;
		if(nextDirection == currentDirection) 
			return false;
		return true;
	}
	
	/**
	 *	Czy napotkalismy na sciane; zwraca true jesli pacman napotkal na sciane
	 */
	public boolean ifMeetWall()
	{
		Node target = getTarget();
		
		if(target == null)
			return true;
		
		return (target.getCords().equals(cords) && (target.getPath(currentDirection) == null));
	}
}
