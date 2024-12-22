package unidades;

import java.awt.Color;
import java.awt.Graphics2D;

import principal.PanelDeJuego;
import principal.Zona;

public class Especialista extends Unidad {

	public Especialista(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.nombre = "Especialista";
		this.hp = 50;
		this.hpMax = 50;
		this.sp = 80;
		this.spMax = 80;
		this.atq = 8;
		this.def = 4;
		this.pcrt = 0;
	}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		if(aliado) {
			g2.setColor(Color.LIGHT_GRAY);
		}
		else {
			g2.setColor(Color.GREEN);
		}
		g2.fillRect(posX+24, posY-24, pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa*2);
	}

}