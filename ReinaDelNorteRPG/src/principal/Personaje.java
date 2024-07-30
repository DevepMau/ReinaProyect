package principal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Personaje {
	
	PanelDeJuego pdj;
	Graphics2D g2;
	public Point pos;
	public int ancho, alto;
	boolean boo;
	
	public Personaje(boolean jugador, PanelDeJuego pdj) {
		this.pdj = pdj;
		ancho = alto = pdj.tamañoDeBaldosa;
		pos = new Point(0,0);
		boo = jugador;
	}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		if(boo) {
			g2.setColor(Color.cyan);
		}
		else {
			g2.setColor(Color.red);
		}
		g2.fillRect((int)pos.getX(), (int)pos.getY(), ancho, alto);
		
	}
	
	public void setPos(Point pos) {
		this.pos.setLocation(pos.getX()+pdj.tamañoDeBaldosa/2, pos.getY()+pdj.tamañoDeBaldosa/2);
	}

}
