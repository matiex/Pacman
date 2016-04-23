package view;

import model.*;

import event.*;

import java.util.concurrent.BlockingQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *	Ramka, zawierajaca 2 panele - BoardView i InfoPanel; odpowiada za wylapywanie zdarzen od uzytkownika
 */
public class Container extends JFrame 
{
	private static final long serialVersionUID = -7729510720848698723L;
	private final Model model;
	private final BoardView boardView;
	private final InfoPanel infoPanel;
	private final BlockingQueue<ViewEvent> queue;
	
	public Container(final Model m, final BlockingQueue<ViewEvent> q)
	{	
		this.model = m;
		this.queue = q;
		
		setLayout(new BorderLayout());
		this.boardView = new BoardView(model, Constant.blockSize, Constant.frameWidth);
		this.infoPanel = new InfoPanel(model, Constant.blockSize, Constant.frameWidth);
		this.add(boardView, BorderLayout.CENTER);
		this.add(infoPanel, BorderLayout.SOUTH);
		
		this.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent event)
			{
				int key = event.getKeyCode();
				switch(key)
				{
					case Constant.up:
						add(new Up());
						break;
					case Constant.down:
						add(new Down());
						break;
					case Constant.left :
						add(new Left());
						break;
					case Constant.right :
						add(new Right());
						break;
					case Constant.start:
						add(new StartGame());
						break;
				}
			}
			
			@Override
			public void keyTyped(KeyEvent event) 
			{
			} 
			
			@Override
			public void keyReleased(KeyEvent event) 
			{
			}

		});
		
	    setLocation(400,0);
		setTitle("Pacman");
		setVisible(true); 	
		setResizable(false);
        setSize(model.getBoard().getWidth() + 2*Constant.frameWidth, model.getBoard().getHeight() + 2*Constant.blockSize);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}	
	
	public void add(final ViewEvent e)
	{
		queue.offer(e);
	}
}
