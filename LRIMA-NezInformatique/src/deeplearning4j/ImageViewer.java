package deeplearning4j;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImageViewer extends JPanel {

	private Image img = null;
	private double width;
	private double height;
	
	public void setImage(Image img) {
		this.img = img;
		repaint();
		System.out.println(img);
		//
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ImageViewer() {
		setBackground(Color.white);
	}
	
	public void intialize(BufferedImage img, int w, int h) {
		this.img = img;
		this.width = w;
		this.height = h;
		repaint();
	}
	
	public void initialize(BufferedImage img, Rectangle bounds) {
		this.img = img;
		this.width = bounds.getWidth();
		this.height = bounds.getHeight();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if(img != null)
			g2d.drawImage(img, 0, 0, (int) width, (int) height, null);
	}
}
