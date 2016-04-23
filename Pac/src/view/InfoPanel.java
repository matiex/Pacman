package view;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.*;

/**
 * Panel z liczba zyc i punktow
 */
public class InfoPanel extends JPanel
{
	private static final long serialVersionUID = -7729510720848698723L; 
	private final Image pacmanImage;
	private final Font DefaultFont;
	private final Model model;
	private final int height;
	private final int width;
	private final int fWidth;
	
	public InfoPanel(final Model m, final int blockSize, final int fWidth)
	{
		this.model = m;
		this.width = model.getBoardWidth() + blockSize;
		this.height = blockSize;
		this.fWidth = fWidth;
		this.pacmanImage = new ImageIcon("src/materials/pac.png").getImage();
		this.setPreferredSize(new Dimension(width, height));
		this.DefaultFont = new Font("Arial", Font.PLAIN, 20);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{    
		super.paintComponent(g);
		drawInfo(g);
	}
	/**
	 * Rysowanie liczby zyc i punktow
	 * 
	 */
	private void drawInfo(final Graphics g)
	{
		int lifes = model.getLifes();
		int pacmanSize = pacmanImage.getWidth(this);
		
		g.setColor(new Color(110, 35, 255));
		g.fillRect(0, 0, width, height);
		
		for(int i = 0; i < lifes; ++i)
		{
			g.drawImage(pacmanImage, fWidth + i*(pacmanSize + 4), (height - pacmanSize)/2 , this);
		}
		
		g.setFont(DefaultFont);
		g.setColor(Color.white);
		String score = "Wynik: " + model.getPointsCounter();
        g.drawString(score, width - 150, height/2 + fWidth );
    } 
}
