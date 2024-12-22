package principal;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Zona {
	PanelDeJuego pdj;
	Graphics2D g2;
	public int x;
	public int y;
	public int id;
	
	public Zona(int id,int x, int y, PanelDeJuego pdj) {
		this.x = x;
		this.y = y;	
		this.id = id;
		this.pdj = pdj;
	}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
		if(id == 0) {
			g2.drawRect(x, y, pdj.tamañoDeBaldosa*3, pdj.tamañoDeBaldosa*4);
			g2.drawString("portrait", x+pdj.tamañoDeBaldosa-6, y+pdj.tamañoDeBaldosa*2+8);
		}
		else {
			g2.drawRect(x, y, pdj.tamañoDeBaldosa*2, pdj.tamañoDeBaldosa*2);
			g2.drawString(String.valueOf(id), x+pdj.tamañoDeBaldosa-8, y+pdj.tamañoDeBaldosa+8);
		}
		
	}
}
