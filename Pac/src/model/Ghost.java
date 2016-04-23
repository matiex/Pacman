package model;

import java.util.Random;

/**
 *  Klasa reprezentujaca duchy - przeciwnikow Pacmana; ruch odbywa sie w losowe kierunki, jednak nie ma mozliwosci zawracania duchow
 *  Zawiera jego wspolrzedne na planszy i kierunek ruchu. 
 *  
 */
public class Ghost {

	private Coordinate coords; /** wspolrzedne ducha */
	private Constant.Direction direction;
	private Path path;
	private Path specialPath1; /** sciezka do skrajnego prawego wezla */
	private Path specialPath2; /** sciezka do skrajnego lewego wezla */
	private int speed;
	
	public Ghost(final Path path, final Coordinate cords, final Path path1, final Path path2) 
	{
		this.coords = new Coordinate(cords.getX(), cords.getY());
		this.direction = Constant.Direction.UP;
		this.speed = Constant.blockSize/18;
		this.path = path;
		this.specialPath1 = path1;
		this.specialPath2 = path2;
	}
	
	/**
	 * zwraca wspolrzedne ducha
	 */
	public Coordinate getCords() 
	{ 
		return coords;
	}
	
	/**
	 * zwraca kierunek, w którym porusza się duch
	 */
	public Constant.Direction getDirection() 
	{ 
		return direction; 
	}
	
	/**
	 * zwraca wezel do ktorego zmierza duch.
	 */
	public Node getTarget()
	{
		if(direction == Constant.Direction.UP || direction == Constant.Direction.LEFT)
		{	
			return path.getNodeFirst();
		}
		else if(direction == Constant.Direction.DOWN || direction == Constant.Direction.RIGHT)
		{
			return path.getNodeSecond();
		}
		else
		{
			if(path.getNodeFirst().getCords().equals(coords))
				return path.getNodeFirst();
			else 
				return path.getNodeSecond();
		}
	}
	
	/**
	 * Ustawienie nowego kierunku duchowi.
	 */
	public void setDirection(final Constant.Direction newDirection)
	{
		this.direction = newDirection;
	}	
	
	/**
	 * Ustawienie nowej ścieżki duchowi.
	 */
	public void setPath(final Path p)
	{
		this.path = p;
	}
	
	/**
	 * Ruch odbywa sie w losowe kierunki, jednak nie ma mozliwosci zawracania duchow
	 */
	public void move()
	{
		 Node node = getTarget();
		 Random rand = new Random();
		 
		 /** Przejscia na skrajnych wezlach */
			
			Coordinate coordRight = new Coordinate (17*Constant.blockSize, 9*Constant.blockSize);
			Coordinate coordLeft = new Coordinate (1*Constant.blockSize, 9*Constant.blockSize);
			
		 if ( coordRight.equals(coords))
		 {
		 			direction = Constant.Direction.RIGHT;
		 			
		 			path = specialPath1;
		 			
		 			coords = new Coordinate(1*Constant.blockSize + 2, 9*Constant.blockSize);
		 }
		 else if (coordLeft.equals(coords))
		 {
		 			direction = Constant.Direction.LEFT;
		 			
		 			path = specialPath2;
		 			
		 			coords = new Coordinate(17*Constant.blockSize - 2, 9*Constant.blockSize);
		 }
		 else if(direction == Constant.Direction.NONE)
		 {
				 direction = Constant.Direction.UP;
		 }
		 else if(node.getCords().equals(coords) && ( node.getCords().equals(new Coordinate(9*Constant.blockSize, 7*Constant.blockSize)) && (direction==Constant.Direction.RIGHT || direction == Constant.Direction.LEFT) ))
		 {
			 /** zabraniamy skrecac w miejsce startowe ghostow -> niech jedzie tak jak jechal */
			 
			 switch(direction)
			 {
			 case LEFT:
				path = node.getPath(Constant.Direction.LEFT);
				coords.addValue(-speed, 0);
				break;
			 case RIGHT:
				path = node.getPath(Constant.Direction.RIGHT);
				coords.addValue(speed, 0);
				break;
			 default:
				break;
			 }
		 }
		 else if(node.getCords().equals(coords)) /** jezeli dojechalismy do wezla na ktorym ma skrecic */
		 {
			 Constant.Direction newDirection;
			 
			 do
			 {
				 int nextPathNumber = rand.nextInt(node.getPathNumber());
				 newDirection = node.getNewDirection(nextPathNumber);
				 path = node.getPath(newDirection);
			 }
			 while(direction.opposed(newDirection));
			  
			 direction = newDirection;
		 }

		 switch(direction)
		 {
		 case UP: 
			coords.addValue(0, -speed); 
		 	break;
		 case DOWN:
			coords.addValue(0, speed);
		 	break;
		 case LEFT:
			coords.addValue(-speed, 0);
		 	break;
		 case RIGHT:
			coords.addValue(speed, 0);
		 	break;
		 case NONE:
		 	break;
		 }
		 
	}

}
