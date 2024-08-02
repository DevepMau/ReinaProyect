package principal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Zona {
	
	PanelDeJuego pdj;
	Graphics2D g2;
	public Point pos;
	public int x, y;
	
	public Zona(Point pos, PanelDeJuego pdj) {
		this.pdj = pdj;
		this.pos = pos;
		this.x = (int)pos.getX();
		this.y = (int)pos.getY();
	}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		g2.setColor(Color.yellow);
		g2.drawRect((int)pos.getX(), (int)pos.getY(), pdj.tamañoDeBaldosa*2, pdj.tamañoDeBaldosa*2);
	}
	
	public void resaltar() {
		g2.setColor(Color.yellow);
		g2.fillRect((int)pos.getX(), (int)pos.getY(), pdj.tamañoDeBaldosa*2, pdj.tamañoDeBaldosa*2);
	}
	
	public Point getPos() {
		return pos;
	}
	
	public int getX() {
		return (int)pos.getX();
	}
	
	public int getY() {
		return (int)pos.getY();
	}
	
	public void setPos(int x, int y) {
		pos.setLocation(x, y);
	}
	
}
