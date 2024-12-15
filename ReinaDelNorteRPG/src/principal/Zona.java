package principal;

import java.awt.Color;
import java.awt.Graphics2D;

public class Zona {
	PanelDeJuego pdj;
	Graphics2D g2;
	public int x;
	public int y;
	public int id;
	
	public Zona(int x, int y, PanelDeJuego pdj) {
		this.x = x;
		this.y = y;	
		this.pdj = pdj;
	}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		g2.setColor(Color.white);
		g2.drawRect(x, y, pdj.tamañoDeBaldosa*2, pdj.tamañoDeBaldosa*2);
	}
}
