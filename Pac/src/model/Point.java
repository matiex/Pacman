package model;
/**
 * Klasa reprezentujaca punkt. Punkt jest identyfikowany po wspolrzednych. Punkt zostaje odwiedzony, gdy wspolrzedne Pacmana sa rowne wspolrzednym punktu
 *
 */
public class Point
{
	private final Coordinate coords;
	private boolean visited;
	private boolean special;
	
	Point(final Coordinate cords)
	{
		this.coords = cords;
		this.visited = false;
		this.special = false;
	}
	
	Point(final Coordinate cords, final boolean setSpecial)
	{
		this.coords = cords;
		this.visited = false;
		this.special = setSpecial;
	}
	
	Point(final Coordinate cords, final boolean visit, final boolean setSpecial)
	{
		this.coords = cords;
		this.visited = visit;
		this.special = setSpecial;
	}
		
	/**
	 * @return Współrzedne punktu
	 */
	public Coordinate getCords() 
	{ 
		return coords;
	}
	
	/**
	 * Sprawdzenie czy punkt został już odwiedzony
	 * @return true - jeśli punkt został już odwiedzony. false w przeciwnym razie.
	 */
	public boolean ifVisited()
	{
		return visited;
	}
	
	public void setVisited(final boolean visit)
	{
		visited = visit;
	}
	
	public boolean ifSpecial()
	{
		return special;
	}
}
