package principal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Personaje {
	
	PanelDeJuego pdj;
	Graphics2D g2;
	public int ancho, alto;
	boolean boo;
	public int x, y;
	
	public Personaje(Zona zona, boolean jugador, PanelDeJuego pdj) {
		this.pdj = pdj;
		this.x = (int)zona.getPos().getX()+pdj.tamañoDeBaldosa/2;
		this.y = (int)zona.getPos().getY()+pdj.tamañoDeBaldosa/2;
		ancho = alto = pdj.tamañoDeBaldosa;
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
		g2.fillRect(x, y, ancho, alto);
		
	}
	
}
