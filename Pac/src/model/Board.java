package model;
import java.util.Map;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Scanner;

/**
 * Klasa reprezentujaca plansze gry.
 */
public class Board 
{
	private TreeMap<Coordinate, Node> nodes;
	private TreeMap<Coordinate, Point> points;
	private TreeSet<Path> paths;
	private final int height;
	private final int width;
	private final int blockSize;
	
	/**
	 * Konstruktor. Czyta wezly i sciezki z pliku konfiguracyjnego o nazwie podanej w argumencie wywolania 
	 * @throws FileNotFoundException - wyjatek jesli plik nie zostal znaleziony
	 */
	public Board(final String fileName) throws FileNotFoundException
	{
		this.nodes = new TreeMap<Coordinate, Node>();
		this.paths = new TreeSet<Path>();
		this.points = new TreeMap<Coordinate, Point>();
		this.blockSize = Constant.blockSize;
		File boardData = new File(fileName);
		Scanner input = new Scanner(boardData);
		
		height = input.nextInt()*blockSize;
		width = input.nextInt()*blockSize;
		
		LoadNodes(input);		
		LoadPaths(input);
	}
	
	/**
	 * zwraca szerokość planszy
	 */
	public int getWidth()
	{ 
		return width;
	}
	
	/**
	 * zwraca wysokość planszy
	 */
	public int getHeight() 
	{ 
		return height; 
	}
	
	/**
	 * zwraca węzeł o szukanych współrzędnych lub null jeśli taki węzeł nie istnieje
	 */
	public Node getNode(final Coordinate cords)
	{
		return nodes.get(cords);
	}
	
	/**
	 * zwraca zbior sciezek
	 */
	public TreeSet<Path> getPaths() 
	{ 
		return paths; 
	}
	
	/**
	 * zwraca zbior punktow (kluczem są wspolrzedne)
	 */
	public TreeMap<Coordinate, Point> getPoints() 
	{ 
		return points;
	}
	
	/**
	 * Zaladowanie wszystkich wezlow z pliku konfiguracyjnego zadanego w argumencie funkcji
	 */
	private void LoadNodes(final Scanner input)
	{
		int y;
		for(y = input.nextInt()*blockSize; y != 0; y = input.nextInt()*blockSize)
		{
			Coordinate cords = new Coordinate(input.nextInt()*blockSize, y);
			nodes.put(cords, new Node(cords));
		}
	}
	
	/**
	 * Zaladowanie wszystkich sciezek z pliku konfiguracyjnego zadanego w argumencie funkcji
	 */
	private void LoadPaths(final Scanner input)
	{
		int x,y,x2,y2;
		Node node1, node2;
		
		for(y = input.nextInt()*blockSize; y != 0; y = input.nextInt()*blockSize)
		{
			x = input.nextInt()*blockSize;
			y2 = input.nextInt()*blockSize;
			x2 = input.nextInt()*blockSize;
			
			node1 = nodes.get(new Coordinate(x,y));
			node2 = nodes.get(new Coordinate(x2,y2));
			
			Path p = new Path(node1, node2);
			paths.add(p);
			node1.addPath(p);
			node2.addPath(p);
			
			/** dodanie punktow */
			if(x != x2)
			{
				while(x <= x2)
				{						
					points.put(new Coordinate(x,y), new Point(new Coordinate(x,y)));
					x += blockSize;
				}
			}
			else
			{
				while(y <= y2)
				{
					/** pominiecie startowego pola ghostow*/
					if (x==(9*blockSize) && y==(8*blockSize)) 
					{
						break;
					}
					
					/** oznaczenie super punktów*/
					if ((x==(1*blockSize) && y==(2*blockSize)) || (x==(1*blockSize) && y==(14*blockSize)) || (x==(17*blockSize) && y==(2*blockSize)) || (x==(17*blockSize) && y==(14*blockSize)))
					{
						points.put(new Coordinate(x,y), new Point((new Coordinate(x,y)), true));
						y += blockSize;
						continue;
					}
					
					points.put(new Coordinate(x,y), new Point(new Coordinate(x,y)));
					y += blockSize;
				}
			}
		}
	}
	
	/**
	 * Sprawdzenie czy na podanych w argumencie wywolania wspolrzednych znajduje sie punkt; ustawia punkt na odwiedzony
	 * Zwraca 0 - gdy punkt sie nie znajduje na tych wspolrzednych, 1 - gdy zwykly punkt, 2 - gdy super punkt
	 */
	public int checkPoint(Coordinate cords)
	{
		Point point = points.get(cords);
		
		if(point != null && !point.ifVisited() && point.ifSpecial()) 
		{
			point.setVisited(true);
			return 2;
		}
		
		if(point != null && !point.ifVisited()) 
		{
			point.setVisited(true);
			return 1;
		}
		return 0;
	}
	
	/**
	 * zwraca true jesli na mapie nie znajduja sie juz zadne nieodwiedzone punkty
	 * 
	 */
	public boolean noPointsLeft()
	{
		for(Map.Entry<Coordinate, Point> entry : points.entrySet())
		{
			Point point = entry.getValue();
			
			if(point.ifVisited() == false) 
				return false;
		}	
		return true;
	}
	
	/**
	 * Ponowne ustawienie punktow na mapie
	 */
	public void reloadPoints()
	{
		for(Map.Entry<Coordinate, Point> entry : points.entrySet())
		{
			Point point = entry.getValue();
			point.setVisited(false);
		}
	}	

}
