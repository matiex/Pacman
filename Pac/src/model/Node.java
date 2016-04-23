package model;

import java.util.TreeMap;

/**
 * Klasa reprezentujaca wezly na mapie gry; posiada wspolrzedne i zbior sciezek (kluczem jest kierunek) 
 *  
 *  */
public class Node implements Comparable<Node>
{
	private Coordinate coords;
	private final TreeMap<Constant.Direction, Path> paths;
	
	public Node(Coordinate cords)
	{
		this.paths = new TreeMap<Constant.Direction, Path>();
		this.coords = cords;	
	}
	
	/**
	 * Zwraca wspolrzedne wezla
	 */
	public Coordinate getCords()
	{
		return coords;
	}
	
	/**
	 * Zwraca liczbe sciezek
	 */
	public int getPathNumber()
	{
		return paths.size();
	}
		
	/**
	 * Zwraca sciezke, ktora prowadzi w zadanym kierunku
	 */
	public Path getPath(final Constant.Direction direction)
	{
		return paths.get(direction);
	}
		
	@Override
	public int compareTo(Node n)
	{
		return this.coords.compareTo(n.getCords());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this) return true;
		if(!(o instanceof Node)) return false;
		
		Node node = (Node) o;
		
		if(this.coords == node.getCords()) return true;
		
		return false;
	}
	
	/**
	 * Zwraca sciezke o podanym indeksie, jezeli brak - null
	 */
	public Constant.Direction getNewDirection(final int indeks)
	{
		if(indeks > paths.size()) 
		{
			return null;
		}
		
		Constant.Direction direction = paths.firstKey();
		
		for(int i = 0; i < indeks; ++i)
		{
			direction = paths.higherKey(direction);
		}
		
		return direction;
	}
	
	/**
	 * Dodawanie sciezki do drzewa i przypisanie odpowiedniego klucza (kierunku)
	 */
	public void addPath(final Path p)
	{
		if(this.equals(p.getNodeFirst()))
		{
			if(this.coords.getX() == p.getNodeSecond().getCords().getX()) 
			{
				paths.put(Constant.Direction.DOWN, p);
			}
			else 
			{
				paths.put(Constant.Direction.RIGHT, p);
			}
		}
		else
		{
			if(this.coords.getX() == p.getNodeFirst().getCords().getX()) 
			{
				paths.put(Constant.Direction.UP, p);
			}
			else
			{
				paths.put(Constant.Direction.LEFT, p);
			}
		}
			
	}	
	
}
