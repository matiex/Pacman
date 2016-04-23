package model;

/**
 * Klasa zawierajaca stale uzyte w programie
 */
public class Constant 
{
	/** rozmiar pojedynczego pola planszy */
	public final static int blockSize = 36;
	/** szerokosc ramki wokol planszy */
	public final static int frameWidth = 8;
	/** wartosc liczbowa lewej strzalki */
	public final static int left = 37;  
	public final static int up = 38;	  
	public final static int right = 39; 
	public final static int down = 40;   
	/** wartosc liczbowa entera */
	public final static int start = 10;  
	
	public enum Direction 
	{
		LEFT,
		RIGHT,
		UP,
		DOWN,
		NONE;
		
		/**
		 * Metoda sprawdzajaca czy podany kierunek jest przeciwny do zadanego w argumencie funkcji
		 * @return true - jesli zadany kierunek jest przeciwny, false w przeciwnym wypadku
		 */
		public boolean opposed(final Constant.Direction direction)
		{
			switch(this)
			{
			case UP:
				if(direction == Constant.Direction.DOWN) return true;
				break;
			case DOWN:
				if(direction == Constant.Direction.UP) return true;
				break;
			case LEFT:
				if(direction == Constant.Direction.RIGHT) return true;
				break;
			case RIGHT:
				if(direction == Constant.Direction.LEFT) return true;
				break;
			case NONE:
				return false;
			}
			
			return false;
		}
	}
}
