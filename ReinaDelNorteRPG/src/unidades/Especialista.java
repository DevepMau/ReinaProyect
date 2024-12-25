package unidades;

import java.awt.Color;
import java.awt.Graphics2D;

import principal.PanelDeJuego;
import principal.Zona;

public class Especialista extends Unidad {

	public Especialista(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.nombre = "Especialista";
		this.hp = 45;
		this.hpMax = 45;
		this.sp = 80;
		this.spMax = 80;
		this.atq = 8;
		this.def = 4;
		this.pcrt = 0;
	}

}