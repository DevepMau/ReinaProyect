package unidades;

import java.awt.Color;
import java.awt.Graphics2D;

import principal.PanelDeJuego;
import principal.Zona;

public class Recluta extends Unidad{

	public Recluta(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.nombre = "Recluta";
		this.hp = 30;
		this.hpMax = 30;
		this.sp = 0;
		this.spMax = 0;
		this.atq = 7;
		this.def = 2;
		this.pcrt = 0;
	}

}
