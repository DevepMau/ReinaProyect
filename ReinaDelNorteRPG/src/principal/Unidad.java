package principal;

import java.awt.Color;
import java.awt.Graphics2D;

public class Unidad {
	PanelDeJuego pdj;
	Graphics2D g2;
	int posX = 0;
	int posY = 0;
	boolean aliado = false;
	
	public Unidad(Zona zona,boolean aliado, PanelDeJuego pdj) {
		posX = zona.x;
		posY = zona.y;
		this.pdj = pdj;
		this.aliado = aliado;
	}
	
	public void actualizar() {}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		if(aliado) {
			g2.setColor(Color.BLUE);
		}
		else {
			g2.setColor(Color.RED);
		}
		g2.fillRect(posX+24, posY-24, pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa*2);
	}
	
	public void posicionar(Zona zona) {
		posX = zona.x;
		posY = zona.y;
	}
}
