/**
* Projekt na przedmiot: Programowanie Zdarzeniowe - gra "Pacman"
* 
* prowadzący: dr inż. Piotr Witoński
* 
* autor: Mateusz Byczkowski 4I1
* 
* semestr 2015/2016 Z
*/

package pac;

import java.io.FileNotFoundException;
import java.util.concurrent.LinkedBlockingQueue;

import controller.*;
import event.*;
import model.*;
import view.*;

public class Pac 
{
	/**
	 * klasa budujaca gre, zawiera panel, model, widok i kontroler
	 */
	public static void main(String[] args)
	{
		LinkedBlockingQueue<ViewEvent> queue = new LinkedBlockingQueue<ViewEvent>(128);
		try
		{
			Board board = new Board("src/materials/map");
			Model model = new Model(board);
			View view = new View(queue, model);
			
			Controller controller = new Controller(view, model, queue);
			controller.play();
		}
		catch(FileNotFoundException e)
		{
			System.err.println("ERROR: DO NOT FOUND MAP");
			return;
		}		
    }

}
