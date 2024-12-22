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

}
