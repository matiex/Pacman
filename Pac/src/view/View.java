package view;

import event.*;
import model.*;
import javax.swing.SwingUtilities;
import java.util.concurrent.BlockingQueue;

/**
 * Klasa widoku
 *
 */
public class View 
{
    private Container frame;
    
	public View(final BlockingQueue<ViewEvent> queue, final Model model)
	{
		this.frame = new Container(model, queue);
	}
	
	public void updateView()
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				frame.repaint();
			}
		});	
	}	
}
