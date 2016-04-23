package model;

/**
 * Klasa reprezentujaca sciezki na mapie
 */
public class Path implements Comparable<Path>
{
	private final Node nodeFirst;
	private final Node nodeSecond;
	
	public Path(final Node n1, final Node n2)
	{
		this.nodeFirst = n1;
		this.nodeSecond = n2;		
	}
	
	public int compareTo(final Path p)
	{
		int x=0;
		
		if((x = this.nodeFirst.compareTo(p.getNodeFirst())) != 0) 
			return x;
		
		if((x = this.nodeSecond.compareTo(p.getNodeSecond())) != 0)
			return x;
		
		return 0;
	}
	
	public Coordinate getNodeFirstCoords()
	{
		return nodeFirst.getCords();
	}
	
	public Coordinate getNodeSecondCoords()
	{
		return nodeSecond.getCords();
	}
	
	public Node getNodeFirst()
	{
		return nodeFirst; 
	}
	public Node getNodeSecond() 
	{ 
		return nodeSecond;
	}
	
}
