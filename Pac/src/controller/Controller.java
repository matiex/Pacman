package controller;

import view.*;
import model.*;
import event.*;

import javax.swing.Timer;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** 
 *  Klasa kontrolera, odmierza czas (timer) i wywoluje odpowiednie funkcje na podstawie zdarzen z widoku
 */
public class Controller
{
	private LinkedBlockingQueue<ViewEvent> queue;
	private View view;
	private Model model;
	private HashMap<Class<? extends ViewEvent>, Action> actionMap;
	
	private Timer timer = new Timer(20, new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if(model.isAlive())
			 {
				 model.movePacman();
				 model.checkPoint();
				 model.moveGhosts();	
				 
				 if(!model.checkCollision())
				 	timer.stop();
			 }
			 
			 if(model.ifWin())
				 timer.stop();
		
			 view.updateView();
		}
	});
		
	/**
	 * 
	 */
	public Controller(final View view, final Model model, final LinkedBlockingQueue<ViewEvent> queue)
	{
		this.queue = queue;
		this.model = model;
		this.view = view;
		this.actionMap = new HashMap <Class<? extends ViewEvent>, Action>();
		
		actionMap.put(Down.class, new DownAction());
		actionMap.put(Up.class, new UpAction());
		actionMap.put(Left.class, new LeftAction());
		actionMap.put(Right.class, new RightAction());
		actionMap.put(StartGame.class, new StartGameAction());
	}
	 
	/**
	 * Wywoluje aplikacje do ciaglego dzialania
	 */
	public void play()
	{
		while(true)
		{
			try
			{
				ViewEvent event = queue.take();
				Action toExec = this.actionMap.get(event.getClass());
				toExec.execute();
			}
			catch(InterruptedException e)
			{
				System.err.println("Interrupt during: queue.take()!");
				return;
			}	 
		}		
	}
	 
	 /** 
	  * Abstrakcyjna klasa strategii obslugi zdarzen
	  */
	 abstract class Action
	 {
		 abstract void execute();	 
	 }
	 
	 /**
	  *	Rozpoczęcie nowej gry
	  */
	 class StartGameAction extends Action
	 {
		 void execute()
		 {
			 if(model.getLifes() == 0 || model.ifWin())
				 model.setNewGame();
			 
			 model.setAlive(true);
			 timer.start();
		 }
	 }
	 
	 /**
	  * Ruch w górę
	  */
	 class UpAction extends Action
	 {
		 void execute()
		 {
			 model.setPacmanNextDirection(Constant.Direction.UP);
		 }
	 }
	 
	 /**
	  * Ruch w dół
	  */
	 class DownAction extends Action
	 {
		 void execute()
		 {
			 model.setPacmanNextDirection(Constant.Direction.DOWN);
		 }
	 }
	 
	 /**
	  * Ruch w lewo
	  */
	 class LeftAction extends Action
	 {
		 void execute()
		 {
			 model.setPacmanNextDirection(Constant.Direction.LEFT);
		 }
	 }
	 
	 /**
	  *	Ruch w prawo
	  */
	 class RightAction extends Action
	 {
		 void execute()
		 {
			 model.setPacmanNextDirection(Constant.Direction.RIGHT);
		 }
	 }
	 
}