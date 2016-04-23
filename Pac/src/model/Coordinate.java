package model;

/**
 * Klasa reprezentujaca wspolrzedne - polozenie obiektow w przestrzeni
 *
 */
public class Coordinate  implements Comparable<Coordinate>
{
	private int x;
	private int y;
	
	/**
	 * Zwraca wspolrzedna Y
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Zwraca wspolrzedna X
	 */
	public int getY() 
	{
		return y;
	}

	public Coordinate(Coordinate coordes)
	{
		this.x = coordes.getX();
		this.y = coordes.getY();
	}
	
	public Coordinate()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Coordinate(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int compareTo(Coordinate a)
	{
		if( this.x > a.getX() || (this.x == a.getX() && this.y > a.getY()))
			return 1;
		
		if(this.x < a.getX() || (this.x == a.getX() && this.y < a.getY())) 
			return -1;
	
		return 0;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Coordinate)) 	
		{
			return false;
		}
		
		Coordinate a = (Coordinate) o;
		
		if(this.x == a.getX() && this.y == a.getY()) 
		{
			return true;
		} 
		else
		{	
			return false;		
		}
	}
	
	/**
	 * Ustawia nowe współrzędne
	 */
	public void setCords(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Dodaje do wspolrzednych podane wartosci
	 */
	public void addValue(final int x, final int y)
	{
		this.x += x;
		this.y += y;
	}
	
}
