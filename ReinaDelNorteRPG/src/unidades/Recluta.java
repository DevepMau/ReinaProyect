package unidades;

import java.awt.Color;
import java.awt.Graphics2D;

import principal.PanelDeJuego;
import principal.Zona;

public class Recluta extends Unidad{

	public Recluta(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.nombre = "Recluta";
		this.hp = 20;
		this.hpMax = 20;
		this.sp = 0;
		this.spMax = 0;
		this.atq = 5;
		this.def = 2;
		this.pcrt = 0;
	}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		if(aliado) {
			g2.setColor(Color.cyan);
		}
		else {
			g2.setColor(Color.MAGENTA);
		}
		g2.fillRect(posX+24, posY-24, pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa*2);
	}

}
