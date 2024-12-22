package unidades;

import java.awt.Color;
import java.awt.Graphics2D;

import principal.PanelDeJuego;
import principal.Zona;

public class Elite extends Unidad{

	public Elite(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.nombre = "Elite";
		this.hp = 70;
		this.hpMax = 70;
		this.sp = 20;
		this.spMax = 20;
		this.atq = 15;
		this.def = 10;
		this.pcrt = 0.5;
	}
	
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

}