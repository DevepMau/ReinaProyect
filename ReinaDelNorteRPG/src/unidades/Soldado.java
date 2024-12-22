package unidades;

import java.awt.Color;
import java.awt.Graphics2D;

import principal.PanelDeJuego;
import principal.Zona;

public class Soldado extends Unidad {

	public Soldado(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.nombre = "Soldado";
		this.hp = 40;
		this.hpMax = 40;
		this.sp = 0;
		this.spMax = 0;
		this.atq = 10;
		this.def = 5;
		this.pcrt = 0.5;
	}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		if(aliado) {
			g2.setColor(Color.orange);
		}
		else {
			g2.setColor(Color.PINK);
		}
		g2.fillRect(posX+24, posY-24, pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa*2);
	}

}
